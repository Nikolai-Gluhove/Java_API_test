package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private final eDomen domen = eDomen.DEV;
    private final eUri uriUser = eUri.USER;
    private final eUri uriLogs = eUri.LogsUSER;

    @Test
    @DisplayName("Delete user with id = 2")
    @Description("This test tries to delete the user with id = 2")
    @Severity(SeverityLevel.MINOR)
    public void testDeleteUser2(){
        //logs
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("email", "vinkotov@example.com");
        userAuth.put("password", "1234");
        Response responseAuth = logs(userAuth);

        //delete
        Response responseDelete = apiCoreRequests.makeDeleteRequest(
                domen.getDomen()+ uriUser.getUri()+"2",
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    //Delete users with authorization
    @Test
    @DisplayName("Delete user with authorization")
    @Description("This test creates a user, logs in as him and to delete it")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUserWithAuthorization(){
        //create
       Map<String, String> userDate = createUser();

       //logs
       Map<String, String> userAuth = new HashMap<>();
       userAuth.put("email", userDate.get("email"));
       userAuth.put("password", userDate.get("password"));
       Response responseAuth = logs(userAuth);

        //delete
        Response responseDelete = apiCoreRequests.makeDeleteRequest(
                domen.getDomen()+ uriUser.getUri()+userDate.get("id"),
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "success", "!");
    }

    //Delete user without authorization
    @Test
    @DisplayName("Delete user without authorization")
    @Description("This test creates two users, logs in as first and to delete second")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUserWithoutAuthorization(){
        //create
        Map<String, String> firstUserDate = createUser();
        Map<String, String> secondUserDate = createUser();

        //log
        Map<String, String> firstUserAuth = new HashMap<>();
        firstUserAuth.put("email", firstUserDate.get("email"));
        firstUserAuth.put("password", firstUserDate.get("password"));
        Response responseAuth= logs(firstUserAuth);

        //delete
        Response responseDelete = apiCoreRequests.makeDeleteRequest(
                domen.getDomen()+ uriUser.getUri()+secondUserDate.get("id"),
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "error", "This user can only delete their own account.");

    }

    //Logs
    private Response logs(Map<String, String> userAuth){
        Response responseAuth = apiCoreRequests.makePostRequest(domen.getDomen()+uriLogs.getUri(), userAuth);
        return responseAuth;
    }

    //Create user
    private Map<String, String> createUser(){
        Map<String, String> userDate = DateGenerator.getRegistrationDate();
        Response responseUserDate = apiCoreRequests.makePostRequest(domen.getDomen()+uriUser.getUri(), userDate);
        userDate.put("id", this.getStringFromJson(responseUserDate, "id"));
        return userDate;
    }
}
