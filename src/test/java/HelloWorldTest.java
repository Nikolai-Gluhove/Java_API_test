import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Nikolai");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.get("answer");

        if(answer == null){
            System.out.println("The key 'answer' is absent");
        }else {
            System.out.println(answer);
        }
    }
}
