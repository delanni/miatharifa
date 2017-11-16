package com.miatharifa.javachallenge2017;

import com.miatharifa.javachallenge2017.game.PlayerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.miatharifa.javachallenge2017.players.DumbPlayer;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.HandshakeResponse;
import javax.websocket.WebSocketContainer;
import javax.xml.bind.DatatypeConverter;

import static com.miatharifa.javachallenge2017.game.ParseState.KILLED;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
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

            boolean withUi = false;
            if (args.length > 1) {
                withUi = Boolean.valueOf(args[1]);
            }
            PlayerModel playerModel = new PlayerModel(withUi);

            webSocket.connectToServer(playerModel, config, URI.create("ws://javachallenge.loxon.hu:8080/JavaChallenge2017/websocket"));

            log.info("Sleeping");

            while(playerModel.state != KILLED) {
                Thread.sleep(10000);
            }
            System.out.println("Final score" + playerModel.player.gameModel.standings);
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
