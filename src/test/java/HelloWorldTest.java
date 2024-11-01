
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import java.lang.Thread;


public class HelloWorldTest {

    @Test
    public void testRestAssured() throws InterruptedException {

        JsonPath jsonFirstResponse = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();


        String token = jsonFirstResponse.getString("token");
        int seconds = jsonFirstResponse.getInt("seconds");

        JsonPath jsonTest;
        for (int i = 0; i < 2; i++) {
            jsonTest = RestAssured
                    .given()
                    .queryParam("token", token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            String status = jsonTest.getString("status");
            switch (i){
                case 0:
                    if (status.equals("Job is NOT ready")){
                        System.out.println("Status is correct");
                        Thread.sleep(seconds * 1000);
                        break;
                    }
                    System.out.println("Status is NOT correct");
                    Thread.sleep(seconds * 1000);
                    break;

                case 1:
                    if(status.equals("Job is ready")){
                        System.out.println("Status is correct");
                        break;
                    }
                    System.out.println("Status is NOT correct");
                    break;
            }
        }
    }
}
