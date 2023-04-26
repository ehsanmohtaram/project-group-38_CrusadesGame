package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TradeAdapter implements JsonSerializer<Trade>, JsonDeserializer<Trade> {
    public JsonElement serialize(Trade trade, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("resourceType", trade.getResourceType().name());
        jsonObject.addProperty("resourceAmount", trade.getResourceAmount());
        jsonObject.addProperty("userSender", trade.getUserSender().getUserName());
        jsonObject.addProperty("userReceiver", trade.getUserReceiver().getUserName());
        jsonObject.addProperty("price", trade.getPrice());
        jsonObject.addProperty("id", trade.getId());
        jsonObject.addProperty("massage", trade.getMassage());
        return jsonObject;
    }

    @Override
    public Trade deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ResourceType resourceType = ResourceType.valueOf(jsonObject.get("resourceType").getAsString().toUpperCase());
        Integer resourceAmount = jsonObject.get("resourceAmount").getAsInt();
        User userSender = User.getUserByUsername(jsonObject.get("userSender").getAsString());
        User userReceiver = User.getUserByUsername(jsonObject.get("userReceiver").getAsString());
        Integer price = jsonObject.get("price").getAsInt();
        Integer id = jsonObject.get("id").getAsInt();
        String massage = jsonObject.get("massage").getAsString();
        return new Trade(resourceType, resourceAmount,price,userSender,userReceiver,massage,id);
    }
}
