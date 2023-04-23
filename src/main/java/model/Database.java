package model;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.stream.Collectors;

public class Database {
    public static void addUser(String u,String p,String n,String e,String s, Integer question, String answer) {
        String json = null;
        File file = new File("info.json");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            json = reader.lines().collect(Collectors.joining());
        } catch (IOException ignored) {}
        try {
            Gson gson = new Gson();
            User user = new User(u,n,p,e,s,question, answer);
            String jsonParser = gson.toJson(user);
            JSONParser parser = new JSONParser();
            JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
            JSONArray jsonToArray = new JSONArray();
            if (json != null) jsonToArray = (JSONArray) parser.parse(json);
            jsonToArray.add(jsonMakeObject);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonToArray.toString());
            fileWriter.close();
        }
        catch (Exception ignored) {}
    }

}
