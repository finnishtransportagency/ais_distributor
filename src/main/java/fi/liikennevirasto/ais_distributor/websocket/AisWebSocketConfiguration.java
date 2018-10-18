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

import fi.liikennevirasto.ais_distributor.configuration.AisDistributorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class AisWebSocketConfiguration implements WebSocketConfigurer {

    private static final String RAW_DATA_ENDPOINT = "/raw-data";
    private static final String PARSED_DATA_ENDPOINT = "/parsed-data";
    private static final String RAW_AND_PARSED_DATA_ENDPOINT = "/raw-and-parsed-data";

    private final AisDistributorProperties aisDistributorProperties;

    @Autowired
    public AisWebSocketConfiguration(AisDistributorProperties aisDistributorProperties) {
        this.aisDistributorProperties = aisDistributorProperties;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(aisRawDataSocketHandler(), RAW_DATA_ENDPOINT).setAllowedOrigins("*").addInterceptors(aisHandshakeInterceptor());
        registry.addHandler(aisParsedDataSocketHandler(), PARSED_DATA_ENDPOINT).setAllowedOrigins("*").addInterceptors(aisHandshakeInterceptor());
        registry.addHandler(aisRawAndParsedDataSocketHandler(), RAW_AND_PARSED_DATA_ENDPOINT).setAllowedOrigins("*").addInterceptors(aisHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor aisHandshakeInterceptor() {
        return new AisWebSocketHandshakeInterceptor(aisDistributorProperties);
    }

    @Bean
    public AisWebSocketHandler aisRawDataSocketHandler() {
        return new AisTextWebSocketHandler();
    }

    @Bean
    public AisWebSocketHandler aisParsedDataSocketHandler() {
        return new AisTextWebSocketHandler();
    }

    @Bean
    public AisWebSocketHandler aisRawAndParsedDataSocketHandler() {
        return new AisTextWebSocketHandler();
    }

}
