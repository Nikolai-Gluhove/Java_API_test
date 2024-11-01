
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

public class HelloWorldTest {

    @Test
    public void testRestAssured(){
        String URL = "https://playground.learnqa.ru/api/long_redirect";

        int counter = 0;
        Response response;
        while (true) {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(URL)
                    .andReturn();

            String responseLocation = response.getHeader("Location");

            if (responseLocation == null){
                break;
            }
            counter++;
            URL = responseLocation;
        }

        System.out.println("Count redirect = "+ counter);
        System.out.println("Endpoint = "+ URL);
    }
}
