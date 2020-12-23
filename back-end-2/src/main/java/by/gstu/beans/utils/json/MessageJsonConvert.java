package by.gstu.beans.utils.json;

import by.gstu.beans.Message;
import com.google.gson.*;

import javax.websocket.*;
import java.lang.reflect.Type;

public class MessageJsonConvert implements JsonSerializer<Message>{

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonMessage = new JsonObject();

        jsonMessage.addProperty("id", message.getId());
        jsonMessage.addProperty("message", message.getMessage());
        jsonMessage.addProperty("nickname", message.getUser().getNickname());

        return jsonMessage;
    }
}
