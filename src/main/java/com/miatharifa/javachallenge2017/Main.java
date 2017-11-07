package com.miatharifa.javachallenge2017;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import javax.xml.bind.DatatypeConverter;


public class Main {
    public static void main(String[] args) {
        try {

            String auth = args[0];
            System.out.println("Connecting with " + auth);
            WebSocketContainer webSocket = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("Authorization", Arrays.asList("Basic " + DatatypeConverter.printBase64Binary(auth.getBytes())));
                }
            };
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();

            webSocket.connectToServer(
                    com.miatharifa.javachallenge2017.client.ClientEndpoint.class, config, URI.create("ws://javachallenge.loxon.hu:8080/JavaChallenge2017/websocket"));

            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
