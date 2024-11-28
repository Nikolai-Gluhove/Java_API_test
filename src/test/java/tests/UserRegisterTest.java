package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Epic("Create user case")
@Feature("Create user")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    Random random = new Random();

    private final eDomen domen = eDomen.DEV;
    private final eUri uri = eUri.USER;

    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate = DateGenerator.getRegistrationDate(userDate);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .when()
                .post(domen.getDomen()+uri.getUri())
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '"+email+"' already exists");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserSuccessfully(){
        Map<String, String> userDate = new HashMap<>();
        userDate = DateGenerator.getRegistrationDate();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .when()
                .post(domen.getDomen()+uri.getUri())
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test check create user without sing")
    @DisplayName("Create user without sing")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithoutSing(){
       Map<String, String> userDate = new HashMap<>();
       userDate.put("email", "learnqaexample.com");
       userDate = DateGenerator.getRegistrationDate(userDate);

       Response responseCreateUserWithoutSing = apiCoreRequests.makePostRequest(domen.getDomen()+uri.getUri(), userDate);

       Assertions.assertResponseTextEquals(responseCreateUserWithoutSing, "Invalid email format");
    }

    @DisplayName("Create user without param")
    @Description("This test check create user without one param")
    @ParameterizedTest()
    @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithoutParam(String param){
        Map<String, String> userDate = DateGenerator.getRegistrationDate(param);;

        Response responseWithoutParam = apiCoreRequests.makePostRequest(domen.getDomen()+uri.getUri(), userDate);

        Assertions.assertResponseTextEquals(responseWithoutParam, "The following required params are missed: "+param);
    }


    @DisplayName("Create a user with a short name")
    @Description("This test check the create of a user whit a one-character name")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserShortName(){
        String firstName = "a";
        Map<String, String> userDate = new HashMap<>();
        userDate.put("firstName", firstName);
        userDate = DateGenerator.getRegistrationDate(userDate);

        Response responseWishShortName = apiCoreRequests.makePostRequest(domen.getDomen()+uri.getUri(), userDate);

        Assertions.assertResponseCodeEquals(responseWishShortName, 400);
        Assertions.assertResponseTextEquals(responseWishShortName, "The value of 'firstName' field is too short");
    }

    @DisplayName("Create a user with a long name")
    @Description("This test check the create of a user with a 250-character name")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateWithLongName(){
        String firstName = "";
        for (int i = 0; i < 251; i++){
            char ch = (char) (random.nextInt(26) + 'a');
            firstName = firstName + ch;
        }
        Map<String, String> userDate = new HashMap<>();
        userDate.put("firstName", firstName);
        userDate = DateGenerator.getRegistrationDate(userDate);

        Response responseWithLongName = apiCoreRequests.makePostRequest(domen.getDomen()+uri.getUri(), userDate);

        Assertions.assertResponseCodeEquals(responseWithLongName, 400);
        Assertions.assertResponseTextEquals(responseWithLongName, "The value of 'firstName' field is too long");
    }

}


