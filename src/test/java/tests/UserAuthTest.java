package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.HashMap;
import java.util.Map;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    private final eDomen domen = eDomen.DEV;
    private final eUri uriLogs = eUri.LogsUSER;
    private final eUri uriAuth = eUri.AuthUSER;


    @BeforeEach
    public void loginUser(){
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", "vinkotov@example.com");
        authDate.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                domen.getDomen()+ uriLogs.getUri(),
                authDate);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthTest(){
        Response responseCheckAuth = apiCoreRequests.makeGetRequest(
                domen.getDomen()+uriAuth.getUri(),
                this.header,
                this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("This test checks authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAutUser(String condition){

        if(condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWitCookie(
                    domen.getDomen()+uriAuth.getUri(),
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    domen.getDomen()+uriAuth.getUri(),
                    this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not know: " + condition);
        }
    }
}
