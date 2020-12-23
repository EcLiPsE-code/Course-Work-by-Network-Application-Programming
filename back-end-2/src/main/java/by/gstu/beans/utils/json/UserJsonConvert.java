package by.gstu.beans.utils.json;

import by.gstu.beans.User;
import com.google.gson.*;

import javax.websocket.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserJsonConvert implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty("id", user.getId());
        jsonUser.addProperty("fullName", user.getFullName());
        jsonUser.addProperty("email", user.getEmail());
        jsonUser.addProperty("nickname", user.getNickname());
        jsonUser.addProperty("role", user.getRole().toString());
        jsonUser.addProperty("password", user.getPassword());

        List<JsonObject> messages = user.getMessages().stream()
                .map(message -> {
                    JsonObject jsonMessage = new JsonObject();

                    jsonMessage.addProperty("id", message.getId());
                    jsonMessage.addProperty("message", message.getMessage());
                    jsonMessage.addProperty("userId", message.getUser().getId());

                    return jsonMessage;
                }).collect(Collectors.toList());
        jsonUser.add("messages", jsonSerializationContext.serialize(messages));

        return jsonUser;
    }
}
