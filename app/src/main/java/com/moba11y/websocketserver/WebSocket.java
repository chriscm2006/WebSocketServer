/*
 *    Copyright 2017 Chris McMeeking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        final String dataString = (data == null) ? "null" : data.toString();

        CLog.v(type + ": " + dataString);

        WebSocketEventListener webSocketEventListener = mWebSocketEventListeners.get(type);

        if (webSocketEventListener == null) {
            CLog.w("No Event Listener for event: " + type);
            return;
        }

        webSocketEventListener.onEvent(this, data);
    }
}
