package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class UserAuthTest extends BaseTestCase {


    @ParameterizedTest
    @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})
    public void testHeaderHomework(String userAgent){
        Response response = RestAssured
                .given()
                .header("user-agent", userAgent)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        String platform = getStringFromJson(response, "$", "platform");
        String browser = getStringFromJson(response, "$", "browser");
        String device = getStringFromJson(response, "$", "device");

        switch (userAgent){
            case "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30":
                Assertions.assertValueString("Mobile", platform);
                Assertions.assertValueString("No", browser);
                Assertions.assertValueString("Android", device);
                break;
            case "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1":
                Assertions.assertValueString("Mobile", platform);
                Assertions.assertValueString("Chrome", browser);
                Assertions.assertValueString("iOS", device);
                break;
            case "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)":
                Assertions.assertValueString("Googlebot", platform);
                Assertions.assertValueString("Unknown", browser);
                Assertions.assertValueString("Unknown", device);
                break;
            case "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0":
                Assertions.assertValueString("Web", platform);
                Assertions.assertValueString("Chrome", browser);
                Assertions.assertValueString("No", device);
                break;
            case "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1":
                Assertions.assertValueString("Mobile", platform);
                Assertions.assertValueString("No", browser);
                Assertions.assertValueString("iPhone", device);
                break;
            default:
                throw new IllegalArgumentException("Unexpected argument in user-agent: " + userAgent);
        }
    }
}
