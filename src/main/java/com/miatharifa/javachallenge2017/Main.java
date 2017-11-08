package com.miatharifa.javachallenge2017;

import com.miatharifa.javachallenge2017.client.ClientEndpoint;
import com.miatharifa.javachallenge2017.game.GameModel;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.HandshakeResponse;
import javax.websocket.WebSocketContainer;
import javax.xml.bind.DatatypeConverter;


public class Main {
    public static void main(String[] args) {
        try {
            String token = args[0];
            String auth = "miathari:" + token;
            System.out.println("Connecting with " + auth);
            WebSocketContainer webSocket = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("Authorization", Arrays.asList("Basic " + DatatypeConverter.printBase64Binary(auth.getBytes())));
                }

                @Override
                public void afterResponse(HandshakeResponse hr){
                    System.out.println("Handshake successful");
                }
            };

            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();

            GameModel gameModel = new GameModel();

            ClientEndpoint clientEndpoint = new ClientEndpoint(gameModel::initialize, gameModel::updateState);

            webSocket.connectToServer(clientEndpoint, config, URI.create("ws://javachallenge.loxon.hu:8080/JavaChallenge2017/websocket"));

            System.out.println("Sleeping");

            while(true) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
