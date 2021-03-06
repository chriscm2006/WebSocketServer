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

import android.util.SparseArray;

import com.chriscm.clog.CLog;
import com.google.gson.JsonObject;

import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * A wrapper around the WebSocketServer that changes the public API to something
 * more friendly for the pruposes I find useful for websockets within Android
 * applications.
 */
@SuppressWarnings("unused")
public abstract class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    public abstract void onWebSocketConnected(WebSocket webSocket);
    public abstract void onWebSocketClosed(WebSocket webSocket);

    private ArrayList<Validator> mValidators = new ArrayList<>();

    public WebSocketServer(InetSocketAddress address) {
        super(address);
    }

    private SparseArray<WebSocket> mWebSockets = new SparseArray<>();

    @Override
    public final void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        WebSocket webSocket = new WebSocket(conn);
        mWebSockets.put(conn.hashCode(), webSocket);
        onWebSocketConnected(webSocket);
    }

    @Override
    public final void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        onWebSocketClosed(mWebSockets.get(conn.hashCode()));
        mWebSockets.remove(conn.hashCode());
    }

    @Override
    public final void onMessage(org.java_websocket.WebSocket conn, String messageString) {

        try {
            Message message = new Message(messageString, new Validator() {

                @Override
                public boolean validate(JsonObject jsonObject) throws ValidatorException {
                    for (Validator validator : mValidators) {
                        if (!validator.validate(jsonObject)) return false;
                    }

                    return true;
                }

            });

            WebSocket webSocket = mWebSockets.get(conn.hashCode());
            webSocket.onMessage(message);
        } catch (Message.MessageException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onError(org.java_websocket.WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    public void addValidator(Validator validator) {
        mValidators.add(validator);
    }

    public void removeValidator(Validator validator) {
        mValidators.remove(validator);
    }
}
