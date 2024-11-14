package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa"+timestamp+"@example.com";
    }

    public static Map<String, String> getRegistrationDate(){
        Map<String, String> date = new HashMap<>();
        date.put("email", DateGenerator.getRandomEmail());
        date.put("password", "123");
        date.put("username", "learnqa");
        date.put("firstName", "learnqa");
        date.put("lastName", "learnqa");

        return date;
    }

    public static Map<String, String> getRegistrationDate(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = DateGenerator.getRegistrationDate();

        Map<String, String> userDate = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys){
            if (nonDefaultValues.containsKey(key)){
                userDate.put(key, nonDefaultValues.get(key));
            } else {
                userDate.put(key, defaultValues.get(key));
            }
        }
        return userDate;
    }
}
