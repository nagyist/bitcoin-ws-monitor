package com.shekhar.bitcoinapp.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import net.minidev.json.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.shekhar.bitcoinapp.server.Broadcaster;

@ClientEndpoint(configurator = BitcoinClientConfigurator.class)
public class BitcoinWebSocketClientEndpoint {

    private static final String MTGOX_URL = "ws://websocket.mtgox.com/mtgox";
    protected static final String MTGOX_TRADES_CHANNEL = "dbf1dee9-4f2e-4a08-8cb7-748919a71b21";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Broadcaster broadcaster;

    public BitcoinWebSocketClientEndpoint(Broadcaster broadcaster) {
        logger.info("Constructor called...");
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info(message);
        String channel = JsonPath.compile("$.channel").read(message);

        String primary = null;
        try {
            primary = JsonPath.compile("$.trade.primary").read(message);
        } catch (Exception ex) {
            // ignore
        }

        if (MTGOX_TRADES_CHANNEL.equals(channel) && "Y".equals(primary)) {
            JSONObject trade = JsonPath.compile("$.trade").read(message);
            this.broadcaster.broadcast(trade.toString());

            logger.info("Published trade: " + trade);
        } else {
            logger.info("Message ignored...");
        }
    }

    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
    }

    public void start() {

        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this, new URI(MTGOX_URL));
        } catch (IOException | DeploymentException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
