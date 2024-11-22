package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Delete user with id = 2")
    @Description("This test tries to delete the user with id = 2")
    public void testDeleteUser2(){
        //logs
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("email", "vinkotov@example.com");
        userAuth.put("password", "1234");
        Response responseAuth = logs(userAuth);

        //delete
        Response responseDelete = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/2",
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    //Delete users with authorization
    @Test
    @DisplayName("Delete user with authorization")
    @Description("This test creates a user, logs in as him and to delete it")
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
                "https://playground.learnqa.ru/api/user/"+userDate.get("id"),
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "success", "!");
    }

    //Delete user without authorization
    @Test
    @DisplayName("Delete user without authorization")
    @Description("This test creates two users, logs in as first and to delete second")
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
                "https://playground.learnqa.ru/api/user/"+secondUserDate.get("id"),
                this.getHeader(responseAuth, "x-csrf-token"),
                this.getCookie(responseAuth, "auth_sid"));

        //assert
        Assertions.assertJsonByName(responseDelete, "error", "This user can only delete their own account.");

    }

    //Logs
    private Response logs(Map<String, String> userAuth){
        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userAuth);
        return responseAuth;
    }

    //Create user
    private Map<String, String> createUser(){
        Map<String, String> userDate = DateGenerator.getRegistrationDate();
        Response responseUserDate = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userDate);
        userDate.put("id", this.getStringFromJson(responseUserDate, "id"));
        return userDate;
    }
}
