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
package fi.liikennevirasto.ais_distributor.controller;

import fi.liikennevirasto.ais_distributor.client.AisWebSocketClient;
import fi.liikennevirasto.ais_distributor.configuration.AisDistributorProperties;
import fi.liikennevirasto.ais_distributor.util.AisConnectionDetails;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AisDistributorConfiguration {

    private final AisDistributorProperties aisDistributorProperties;

    @Autowired
    public AisDistributorConfiguration(AisDistributorProperties aisDistributorProperties) {
        this.aisDistributorProperties = aisDistributorProperties;
    }

    @Bean
    public AisConnectionDetails aisConnectionDetails(Environment env) {
        return new AisConnectionDetails(env, aisDistributorProperties.getConnector().getAisDataEndpoint());
    }

    @Bean
    public WebSocketClient webSocketClient(AisConnectionDetails aisConnectionDetails, AisDistributor aisDistributor) {
        WebSocketClient webSocketClient = new AisWebSocketClient(aisConnectionDetails.getUri(), aisDistributor);

        webSocketClient.setConnectionLostTimeout(0); //TODO ping pong

        return webSocketClient;
    }

}
