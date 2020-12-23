package by.gstu.beans.utils.json;

import by.gstu.beans.Message;
import by.gstu.beans.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class UtilFactory {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageJsonConvert())
            .registerTypeAdapter(User.class, new UserJsonConvert())
            .create();

    public static String convertHttpRequestToString(HttpServletRequest request){
        try (BufferedReader reader = request.getReader()) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObject convertHttpRequestToObject(HttpServletRequest request){
        return GSON.fromJson(convertHttpRequestToString(request), JsonObject.class);
    }
}
