package model;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Database {
    public static void updateJson() {
        File file = new File("src/main/resources/info.json");
        try {
            Gson gson = new Gson();
            JSONArray jsonToArray = new JSONArray();
            JSONParser parser = new JSONParser();
            for (User user : User.users) {
                String jsonParser = gson.toJson(user);
                JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
                jsonToArray.add(jsonMakeObject);
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonToArray.toString());
            fileWriter.close();
        }
        catch (Exception ignored) {}
    }
    public static ArrayList<User> setArrayOfUsers() {
        ArrayList<User> users = new ArrayList<>();
        String json = changeJsonToString();
        try {
            Gson gson = new Gson();
            JSONParser parser = new JSONParser();
            JSONArray jsonToArray = new JSONArray();
            if (json != null) jsonToArray = (JSONArray) parser.parse(json);
            for (Object jsonValue : jsonToArray) users.add(gson.fromJson(jsonValue.toString(),User.class));
        }
        catch (Exception ignored) {}
        for (User user : users) {
            User.isDelayed.put(user,false);
            User.loginDelays.put(user,-15);
        }
        return users;
    }

    public static String changeJsonToString() {
        File file = new File("src/main/resources/info.json");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException ignored) {
            return null;
        }
    }
}
