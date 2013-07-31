package com.shekhar.bitcoinapp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.shekhar.bitcoinapp.client.BitcoinWebSocketClientEndpoint;
import com.shekhar.bitcoinapp.server.BitcointWebSocketServerEndpoint;

@WebListener
public class WsConfigListener implements ServletContextListener {

    private BitcoinWebSocketClientEndpoint wsClient;

    public WsConfigListener() {
        this.wsClient = new BitcoinWebSocketClientEndpoint(new BitcointWebSocketServerEndpoint());
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        wsClient.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // NO-OP
    }
}
