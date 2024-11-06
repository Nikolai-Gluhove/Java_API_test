import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldTest {

    @Test
    public void testRestAssured() throws InterruptedException {
        //Парроли
        HashMap<Integer, String> password = new HashMap<>();
        password.put(0, "123456");
        password.put(1, "123456789");
        password.put(2, "qwerty");
        password.put(3, "password");
        password.put(4, "1234567");
        password.put(5, "12345678");
        password.put(6, "iloveyou");
        password.put(7, "111111");
        password.put(8, "123123");
        password.put(9, "abc123");
        password.put(10, "qwerty123");
        password.put(11, "1q2w3e4r");
        password.put(12, "admin");
        password.put(13, "qwertyuiop");
        password.put(14, "654321");
        password.put(15, "555555");
        password.put(16, "lovely");
        password.put(17, "7777777");
        password.put(18, "welcome");
        password.put(19, "888888");
        password.put(20, "princess");
        password.put(21, "dragon");
        password.put(22, "password1");
        password.put(23, "123qwe");
        password.put(24, "12345");

        //параметры тела

        for (int i = 0; i < password.size(); i++) {
            //запрос на получение авторизационного cookie
            Response responseAutorization = RestAssured
                    .given()
                    .when()
                    .body("{\"login\":\"super_admin\", \"password\":\""+password.get(i)+"\"}")
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String autorizationCookie = responseAutorization.getCookie("auth_cookie");

            //запрос для проверки пароля
           Response checkAutoriz = RestAssured
                   .given()
                   .when()
                   .cookie("auth_cookie="+autorizationCookie)
                   .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                   .andReturn();

           String conten = checkAutoriz.getBody().asString();
           if (!conten.contains("You are NOT authorized")){
               System.out.println("Password = "+autorizationCookie+" - "+conten);
               break;
           }
        }
    }
}
