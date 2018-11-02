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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AisWebSocketClientHealthCheck {

    private static final Marker FATAL = MarkerFactory.getMarker("FATAL");
    private static final Logger LOGGER = LoggerFactory.getLogger(AisWebSocketClientHealthCheck.class);

    private final AisWebSocketClient aisWebSocketClient;

    @Autowired
    public AisWebSocketClientHealthCheck(AisWebSocketClient aisWebSocketClient) {
        this.aisWebSocketClient = aisWebSocketClient;
    }

    @Scheduled(
            initialDelayString = "${ais.distributor.connector-web-socket.reconnect-initial-delay}",
            fixedDelayString = "${ais.distributor.connector-web-socket.reconnect-fixed-delay}"
    )
    public void reconnect() {
        if (!aisWebSocketClient.isOpen()) {
            try {
                LOGGER.info("Reconnecting to Connector");
                aisWebSocketClient.reconnectBlocking();
            } catch (InterruptedException e) {
                LOGGER.error(FATAL, "Failed reconnect to Connector", e);
            }
        }
    }

}
