import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured(){
        JsonPath response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String secondMessage = response.get("messages[1].message");
        System.out.println(secondMessage);
    }
}
