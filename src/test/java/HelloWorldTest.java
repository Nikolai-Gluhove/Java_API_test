import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class HelloWorldTest {

    @Test
    public void testFor200(){
       Response response = RestAssured
               .given()
               .when()
               .get("https://playground.learnqa.ru/api/map")
               .andReturn();

       assertEquals(200, response.getStatusCode(), "Unexpected status code");
    }

    @Test
    public void testFor404(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();

        assertEquals(404, response.getStatusCode(), "Unexpected status code");
    }
}
