package com.moba11y.websocketserver;

import com.chriscm.clog.CLog;
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

        CLog.v(type + ": " + data.toString());

        WebSocketEventListener webSocketEventListener = mWebSocketEventListeners.get(type);

        if (webSocketEventListener == null) {
            CLog.w("No Event Listener for event: " + type);
            return;
        }

        webSocketEventListener.onEvent(this, data);
    }
}
