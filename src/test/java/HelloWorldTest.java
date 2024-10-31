import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured(){
        Map<String, String> headers = new HashMap<>();
        headers.put("muHeader1", "myValue1");
        headers.put("muHeader2", "myValue2");

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

        String locationHeaders = response.getHeader("Location");
        System.out.println(locationHeaders);
    }
}
