package com.moba11y.websocketserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chrismcmeeking on 2/1/17.
 */
@SuppressWarnings("unused")
public class WebSocket {

    private Map<String, WebSocketEventListener> mWebSocketEventListeners = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    org.java_websocket.WebSocket mWebSocket;

    WebSocket(org.java_websocket.WebSocket webSocket) {
        mWebSocket = webSocket;
    }

    public void send(Message message) {
        mWebSocket.send(message.toString());
    }
    public interface WebSocketEventListener {
        void onEvent(WebSocket socket, JsonObject jsonObject);
    }

    public void addEventListener(final String eventName, WebSocketEventListener webSocketEventListener) {
        mWebSocketEventListeners.put(eventName, webSocketEventListener);
    }

    public void onMessage(final Message message) {
        final String type = message.getType();
        final JsonObject data = message.getData();

        mWebSocketEventListeners.get(type).onEvent(this, data);
    }
}