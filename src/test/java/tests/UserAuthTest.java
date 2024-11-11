package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;


public class UserAuthTest extends BaseTestCase {
    String header;

    @Test
    public void testHeaderHomework(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        header = getHeader(response, "x-secret-homework-header");
        Assertions.assertValueHeader(header, "Some secret value");
    }
}
