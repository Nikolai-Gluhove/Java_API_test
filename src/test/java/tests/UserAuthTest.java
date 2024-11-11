package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Map;


public class UserAuthTest extends BaseTestCase {
    String cookie;

    @Test
    public void testHomeWorkCookie(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        response.prettyPrint();

        cookie = getCookie(response, "HomeWork");
        Assertions.assertValueCookie(response, "HomeWork", "hw_value");
    }
}
