/*-
 * ====================================START=======================================
 * ais-distributor
 * -----
 * Copyright (C) 2018 Digia
 * -----
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * =====================================END========================================
 */
package fi.liikennevirasto.ais_distributor.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "user=user",
        "passwd=passwd",
        "address=localhost",
        "port=8080",
        "jasypt.encryptor.password=password"
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AisWebSocketTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AisWebSocketHandler aisRawDataSocketHandler;

    @Autowired
    private AisWebSocketHandler aisParsedDataSocketHandler;

    @Autowired
    private AisWebSocketHandler aisRawAndParsedDataSocketHandler;

    @Test
    public void testRawDataWebSocketRead() throws Exception {
        testWebSocketRead("/raw-data", aisRawDataSocketHandler);
    }

    @Test
    public void testRawDataWebSocketAuthentication() throws Exception {
        testWebSocketAuthentication("/raw-data");
    }

    @Test
    public void testParsedDataWebSocketRead() throws Exception {
        testWebSocketRead("/parsed-data", aisParsedDataSocketHandler);
    }

    @Test
    public void testParsedDataWebSocketAuthentication() throws Exception {
        testWebSocketAuthentication("/parsed-data");
    }

    @Test
    public void testRawAndParsedDataWebSocketRead() throws Exception {
        testWebSocketRead("/raw-and-parsed-data", aisRawAndParsedDataSocketHandler);
    }

    @Test
    public void testRawAndParsedDataWebSocketAuthentication() throws Exception {
        testWebSocketAuthentication("/raw-and-parsed-data");
    }

    private void testWebSocketRead(String endpoint, AisWebSocketHandler aisWebSocketHandler) throws Exception {
        String expectedAisData = "!ABVDM,1,1,,A,1P000Oh1IT1svTP2r:43grwb05q4,0*01";
        AtomicReference<String> actualAisData = new AtomicReference<>();

        WebSocketClient webSocketClient = null;
        try {
            webSocketClient = new WebSocketClient(createUrl(endpoint, "username", "password")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) { }
                @Override
                public void onMessage(String message) {
                    actualAisData.set(message);
                }
                @Override
                public void onClose(int code, String reason, boolean remote) {}
                @Override
                public void onError(Exception ex) {}
            };
            webSocketClient.connectBlocking(30, TimeUnit.SECONDS);
            for (int i = 0; aisWebSocketHandler.getWebSocketSessionCount() == 0; i++) {
                Thread.sleep(100);
                if (i > 100) throw new Exception("Timeout");
            }
            aisWebSocketHandler.broadcast(expectedAisData);
        } finally {
            if (webSocketClient != null) {
                webSocketClient.closeBlocking();
            }
        }

        assertEquals(expectedAisData, actualAisData.get());
    }

    public void testWebSocketAuthentication(String endpoint) throws Exception {
        String expectedCloseReason = "Invalid status code received: 401 Status line: HTTP/1.1 401 ";
        AtomicReference<String> actualCloseReason = new AtomicReference<>();

        WebSocketClient webSocketClient = null;
        try {
            webSocketClient = new WebSocketClient(createUrl(endpoint, "username", "invalid")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {}
                @Override
                public void onMessage(String message) {}
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    actualCloseReason.set(reason);
                }
                @Override
                public void onError(Exception ex) {}
            };
            webSocketClient.connectBlocking(30, TimeUnit.SECONDS);
        } finally {
            if (webSocketClient != null) {
                webSocketClient.closeBlocking();
            }
        }

        assertEquals(expectedCloseReason, actualCloseReason.get());
    }

    private URI createUrl(String path, String username, String password) throws URISyntaxException {
        return new URI("ws://localhost:" + port + path + "?username=" + username + "&passwd=" + password);
    }

}
