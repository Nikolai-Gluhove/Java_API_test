package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDateNotAuth(){
        Response responseUserDate = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserDate, "username");

        String[] unexpectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonNasNotFields(responseUserDate, unexpectedFields);
    }

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
}
