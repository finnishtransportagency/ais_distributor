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
package fi.liikennevirasto.ais_distributor.client;

import fi.liikennevirasto.ais_distributor.controller.AisDistributor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class AisWebSocketClient extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisWebSocketClient.class);

    private final AisDistributor aisDistributor;

    public AisWebSocketClient(URI serverUri, AisDistributor aisDistributor) {
        super(serverUri);
        this.aisDistributor = aisDistributor;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        connectBlocking(10, TimeUnit.SECONDS);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("Connected to AIS Connector");
    }

    @Override
    public void onMessage(String message) {
        aisDistributor.distribute(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.info("Closed connection to AIS Connector (code: {}, reason: '{}')", code, reason);
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.error("Failed to connect to AIS Connector", ex);
    }

}
