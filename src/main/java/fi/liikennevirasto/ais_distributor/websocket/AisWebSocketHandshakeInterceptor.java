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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class AisWebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisWebSocketHandshakeInterceptor.class);

    private static final String USERNAME = "username";
    private static final String PASSWD = "passwd";

    private final AisDistributorProperties aisDistributorProperties;

    public AisWebSocketHandshakeInterceptor(AisDistributorProperties aisDistributorProperties) {
        this.aisDistributorProperties = aisDistributorProperties;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        String endpoint = ((ServletServerHttpRequest) request).getServletRequest().getRequestURI();
        String username = ((ServletServerHttpRequest) request).getServletRequest().getParameter(USERNAME);
        String password = ((ServletServerHttpRequest) request).getServletRequest().getParameter(PASSWD);

        LOGGER.info("'{}' is authenticating against '{}'", username, endpoint);

        if (credentialsOk(endpoint, username, password)) {
            LOGGER.info("'{}' has authenticated against '{}'", username, endpoint);
            return true;
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            LOGGER.info("'{}' failed to authenticate against '{}'", username, endpoint);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {}

    private boolean credentialsOk(String endpoint, String username, String password) {
        Optional<AisDistributorProperties.Distributor.Credentials> credentials = aisDistributorProperties.getDistributor().getCredentials()
                .stream()
                .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
                .findFirst();

        return credentials.map(
                c -> c.getAllowedEndpoints()
                    .stream()
                    .anyMatch(e -> e.getUrl().equals(endpoint))
                )
                .orElse(false);
    }

}
