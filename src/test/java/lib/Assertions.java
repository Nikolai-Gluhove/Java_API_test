package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    //проверка наличия атрибудат в json
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    //проверка длины строки
    public static void assertStringLength(String message){
        assertTrue(message.length() > 15, "Length message less 15 symbols");
    }

    //проверка значения cookie
    public static void assertValueCookie(Response Response, String name, String expect){
        assertEquals(expect, Response.getCookie(name), "The value in cookie doesn't match expect value");
    }

    //проверка значения header
    public static void assertValueHeader(String value, String expect){
        assertEquals(value, expect, "The value in header doesn't match expect value");

    }
}
