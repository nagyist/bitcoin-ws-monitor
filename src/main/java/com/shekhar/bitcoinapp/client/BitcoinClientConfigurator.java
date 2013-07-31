package com.shekhar.bitcoinapp.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.HandshakeResponse;

public class BitcoinClientConfigurator extends Configurator {
    
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Origin", Arrays.asList("http://websocket.mtgox.com"));
    }

    @Override
    public void afterResponse(HandshakeResponse hr) {
        System.out.println("Handshake response " + hr);
        super.afterResponse(hr);
    }
}
