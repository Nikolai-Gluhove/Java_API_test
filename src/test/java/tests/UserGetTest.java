package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    private final eDomen domen = eDomen.DEV;
    private final eUri uriUser = eUri.USER;
    private final eUri uriLogs = eUri.LogsUSER;


    //Юзер не авторизован
    @Test
    public void testGetUserDateNotAuth(){
        Response responseUserDate = RestAssured
                .get(domen.getDomen()+ uriUser.getUri()+"2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserDate, "username");

        String[] unexpectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNotFields(responseUserDate, unexpectedFields);
    }

    //Юзер авторизован
    @Test
    public void testGetUserDetailAuthAsSameUser(){
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", "vinkotov@example.com");
        authDate.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authDate)
                .post(domen.getDomen()+uriLogs.getUri())
                .andReturn();


        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserDate = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get(domen.getDomen()+ uriUser.getUri()+"2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserDate, expectedFields);
    }

    //Юзер авторизован но запрашивает данные другого пользователя
   @DisplayName("Get another user's date")
   @Description("This test check the date another user")
   @Test
   public void testGetAntherUserDate(){
       Map<String, String> authDate = new HashMap<>();
       authDate.put("email", "vinkotov@example.com");
       authDate.put("password", "1234");

       Response responseGetAuth = apiCoreRequests.makePostRequest(domen.getDomen()+uriLogs.getUri(), authDate);

       String header = this.getHeader(responseGetAuth, "x-csrf-token");
       String cookie = this.getCookie(responseGetAuth, "auth_sid");

       Response responseUserDate = apiCoreRequests.makeGetRequest(domen.getDomen()+uriUser.getUri()+"3", header, cookie);
       String[] expectedFields = {"firstName", "lastName", "email"};

       Assertions.assertJsonHasNotFields(responseUserDate, expectedFields);

   }
}
