package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    //Юзер не авторизован
    @Test
    public void testGetUserDateNotAuth(){
        Response responseUserDate = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
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
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();


        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserDate = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
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

       Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);

       String header = this.getHeader(responseGetAuth, "x-csrf-token");
       String cookie = this.getCookie(responseGetAuth, "auth_sid");

       Response responseUserDate = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/3", header, cookie);
       String[] expectedFields = {"firstName", "lastName", "email"};

       Assertions.assertJsonHasNotFields(responseUserDate, expectedFields);

   }
}
