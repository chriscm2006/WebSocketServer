package com.moba11y.websocketserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Stores messages as JSON objects, and ensures incoming messages have the expected properties.
 */
@SuppressWarnings("unused")
class Message {

    private final JsonObject mJsonObject;

    private enum Property {TYPE, DATA}

    class MessageException extends Exception {
        MessageException(String message) {
            super(message);
        }

        MessageException(Exception e) { super(e);}
    }

    Message(String type, JsonElement data) {
        mJsonObject = new JsonObject();
        mJsonObject.addProperty(Property.TYPE.name().toLowerCase(), type);
        mJsonObject.add(Property.DATA.name().toLowerCase(), data);
    }

    Message(String message) throws MessageException {
        mJsonObject = new JsonParser().parse(message).getAsJsonObject();

        for (Property property : Property.values()) {
            if (!mJsonObject.has(property.name().toLowerCase())) {
                throw new MessageException("Resulting JSON Object did not have expected property: " + property.name().toLowerCase());
            }
        }
    }

    String getType() {
        return mJsonObject.get("type").getAsString();
    }

    JsonObject getData() {
        JsonElement element = mJsonObject.get("data");

        if (element.isJsonNull()) {
            return null;
        } else {
            return element.getAsJsonObject();
        }
    }
}
