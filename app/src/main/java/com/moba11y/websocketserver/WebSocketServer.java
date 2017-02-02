package com.moba11y.websocketserver;

import android.util.SparseArray;

import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

/**
 * A wrapper around the WebSocketServer that changes the public API to something
 * more friendly for the pruposes I find useful for websockets within Android
 * applications.
 */
@SuppressWarnings("unused")
public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    @SuppressWarnings("WeakerAccess")
    public interface WebSocketServerCallbacks {
        void onWebSocketConnected(WebSocket webSocket);
        void onWebSocketClosed(WebSocket webSocket);
    }

    private final WebSocketServerCallbacks mWebSocketServerCallbacks;

    public WebSocketServer(InetSocketAddress address, WebSocketServerCallbacks callbacks) {
        super(address);
        mWebSocketServerCallbacks = callbacks;
    }

    private SparseArray<WebSocket> mWebSockets = new SparseArray<>();

    @Override
    public final void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        WebSocket webSocket = new WebSocket(conn);
        mWebSockets.put(conn.hashCode(), webSocket);
        mWebSocketServerCallbacks.onWebSocketConnected(webSocket);
    }

    @Override
    public final void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        mWebSocketServerCallbacks.onWebSocketClosed(mWebSockets.get(conn.hashCode()));
        mWebSockets.remove(conn.hashCode());
    }

    @Override
    public final void onMessage(org.java_websocket.WebSocket conn, String messageString) {

        try {
            Message message = new Message(messageString);

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
}
