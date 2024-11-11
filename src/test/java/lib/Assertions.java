package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertStringLength(String message){
        assertTrue(message.length() > 15, "Length message less 15 symbols");
    }

    public static void assertValueCookie(Response Response, String name, String expect){
        assertEquals(expect, Response.getCookie(name), "The value in cookie doesn't match expect value");
    }
}
