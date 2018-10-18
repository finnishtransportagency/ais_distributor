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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AisTextWebSocketHandler extends TextWebSocketHandler implements AisWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisTextWebSocketHandler.class);

    private Map<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session.getId());
    }

    @Override
    public void broadcast(String message) {
        for (Map.Entry<String, WebSocketSession> entry : webSocketSessions.entrySet()) {
            try {
                entry.getValue().sendMessage(new TextMessage(message));
            } catch (IOException e) {
                LOGGER.error("Failed to send message '{}' to session '{}'", message, entry.getKey(), e);
            }
        }
    }

    @Override
    public int getWebSocketSessionCount() {
        return webSocketSessions.size();
    }

}
