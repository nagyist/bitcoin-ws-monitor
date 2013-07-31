package com.shekhar.bitcoinapp.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/bitcoin")
public class BitcointWebSocketServerEndpoint implements Broadcaster {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    public BitcointWebSocketServerEndpoint() {
        logger.info("Constructor called...");
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Open session ..." + session.getId());
        SESSIONS.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Message ... " + message);
    }

    @OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
    }

    @Override
    public void broadcast(String message) {
        for (Session session : SESSIONS) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
