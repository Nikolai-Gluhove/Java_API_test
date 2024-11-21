package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    Map<String, String> userDate = DateGenerator.getRegistrationDate();
    Map<String, String> authDate = new HashMap<>();
    String userId;
    Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", this.authDate);

    //Generate user
    @BeforeEach
    public void generateUser(){
        Response responseUserDate = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", this.userDate);
        this.userId = this.getStringFromJson(responseUserDate, "id");

        this.authDate.put("email", this.userDate.get("email"));
        this.authDate.put("password", this.userDate.get("password"));
    }

    //Login
    private void login(){
        this.responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", this.authDate);
    }

    @Test
    @DisplayName("This positive test check the user's editing")
    @Description("This test is logged by the user, changes firstName and verifies the changes made")
    public void testEditJustCreatedTest(){
        //LOGIN
        login();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editDate = new HashMap<>();
        editDate.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                editDate,
                this.getHeader(this.responseGetAuth, "x-csrf-token"),
                this.getCookie(this.responseGetAuth, "auth_sid"));

        //GET
        Response responseUserDate = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                this.getHeader(this.responseGetAuth, "x-csrf-token"),
                this.getCookie(this.responseGetAuth, "auth_sid"));

        //ASSERT
        Assertions.assertJsonByName(responseUserDate, "firstName", newName);
    }

    @Test
    @DisplayName("This test check editing the user's without authorization")
    @Description("This test attempts to change user data while not logged in")
    public void testEditWithoutAuth(){
        String newName = "Changed Name without authorization";
        Map<String, String> editDate = new HashMap<>();
        editDate.put("firstName", newName);

        Response responseEditWithoutAuth = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                editDate);

        //ASSERT
        Assertions.assertJsonByName(responseEditWithoutAuth, "error", "Auth token not supplied");
    }

    @Test
    @DisplayName("Testing editing users with authorization another user's")
    @Description("This test logs into the system, create a new user and attempts to change user with incorrect authorization")
    public void testEditAnotherUser(){
        //LOGIN
        login();

        //CREATE NEW USER
        Map<String, String> newUserDade = new HashMap<>();
        newUserDade = DateGenerator.getRegistrationDate();
        Response responseUserDate = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", newUserDade);
        String newUserId = this.getStringFromJson(responseUserDate, "id");

        //EDIT
        Map<String, String> editDate = new HashMap<>();
        editDate.put("firstName", "Changed Name with authorization another user's");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/"+newUserId,
                editDate,
                this.getHeader(this.responseGetAuth, "x-csrf-token"),
                this.getCookie(this.responseGetAuth, "auth_sid"));

        //ASSERT
        Assertions.assertJsonByName(responseEditUser, "error", "This user can only edit their own data.");
    }

    @Test
    @DisplayName("Test the email changed to incorrect")
    @Description("This test attempts changed email of user to email without @")
    public void testEditNotCorrectEmail(){
        //LOGIN
        login();

        //EDIT
        Map<String, String> editDate = new HashMap<>();
        editDate.put("email", "newEmailexample.com");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                editDate,
                this.getHeader(this.responseGetAuth, "x-csrf-token"),
                this.getCookie(this.responseGetAuth, "auth_sid"));

        //ASSERT
        Assertions.assertJsonByName(responseEditUser, "error","Invalid email format");
    }

    @Test
    @DisplayName("Check editing the firstName by one symbol")
    @Description("This test attempts change firstName of user's by on symbol")
    public void testEditOneSymbolName(){
        //LOGIN
        login();

        //EDIT
        Map<String, String> editDate = new HashMap<>();
        editDate.put("firstName", "a");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/"+userId,
                editDate,
                this.getHeader(this.responseGetAuth, "x-csrf-token"),
                this.getCookie(this.responseGetAuth, "auth_sid"));

        //ASSERT
        Assertions.assertJsonByName(responseEditUser, "error","The value for field `firstName` is too short");
    }






























}
