package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .when()
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '"+email+"' already exists");
    }

    @Test
    public void testCreateUserSuccessfully(){
        String email = DateGenerator.getRandomEmail();

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .when()
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
