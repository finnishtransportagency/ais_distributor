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

import fi.liikennevirasto.ais_distributor.model.AisRadioMsg;
import fi.liikennevirasto.ais_distributor.util.AisRadioMsgFilter;
import fi.liikennevirasto.ais_distributor.util.AisRadioMsgParser;
import fi.liikennevirasto.ais_distributor.websocket.AisWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AisDistributor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisDistributor.class);

    private final AisWebSocketHandler aisRawDataSocketHandler;
    private final AisWebSocketHandler aisParsedDataSocketHandler;
    private final AisWebSocketHandler aisRawAndParsedDataSocketHandler;
    private final AisWebSocketHandler aisPublicParsedDataSocketHandler;
    private final AisWebSocketHandler aisPublicGeoJsonDataSocketHandler;

    private String cachedFirstPart = null;

    @Autowired
    public AisDistributor(AisWebSocketHandler aisRawDataSocketHandler, AisWebSocketHandler aisParsedDataSocketHandler,
                          AisWebSocketHandler aisRawAndParsedDataSocketHandler, AisWebSocketHandler aisPublicParsedDataSocketHandler,
                          AisWebSocketHandler aisPublicGeoJsonDataSocketHandler) {
        this.aisRawDataSocketHandler = aisRawDataSocketHandler;
        this.aisParsedDataSocketHandler = aisParsedDataSocketHandler;
        this.aisRawAndParsedDataSocketHandler = aisRawAndParsedDataSocketHandler;
        this.aisPublicParsedDataSocketHandler = aisPublicParsedDataSocketHandler;
        this.aisPublicGeoJsonDataSocketHandler = aisPublicGeoJsonDataSocketHandler;
    }

    public void distribute(String data) {
        LOGGER.debug("Raw data: {}", data);

        if (AisRadioMsgParser.isSupportedMessageType(data)) {
            if (cachedFirstPart != null && !isSecondPartOfMultipartRadioMessage(data)) {
                discardCachedFirstPart();
            }

            AisRadioMsgParser.validateChecksum(data);

            if (isReadyForParsing(data)) {
                parseAndBroadcast(data);
            }

        } else {
            if (cachedFirstPart != null) {
                discardCachedFirstPart();
            }
        }
    }

    private void parseAndBroadcast(String data) {
        AisRadioMsg msg = parse(data);
        if (msg != null) {
            broadcastToAuthenticatedClients(msg);

            if (AisRadioMsgFilter.allowedInPublicStream(msg)) {
                broadcastToPublicClients(msg);
            }
        }
    }

    private void broadcastToAuthenticatedClients(AisRadioMsg msg) {
        msg.getRawDataParts().forEach(aisRawDataSocketHandler::broadcast);
        aisParsedDataSocketHandler.broadcast(msg.toParsedDataString());
        aisRawAndParsedDataSocketHandler.broadcast(msg.toRawAndParsedDataString());
    }

    private void broadcastToPublicClients(AisRadioMsg msg) {
        aisPublicParsedDataSocketHandler.broadcast(msg.toPublicParsedDataString());
        if (msg.isPositionMsg()) {
            String geoJsonStr = msg.toPublicGeoJsonDataString();
            if (geoJsonStr != null) {
                aisPublicGeoJsonDataSocketHandler.broadcast(geoJsonStr);
            }
        }
    }

    private void discardCachedFirstPart() {
        LOGGER.info("Discarded multipart message orphan first part");
        cachedFirstPart = null;
    }

    private boolean isReadyForParsing(String data) {

        if (!AisRadioMsgParser.isMultipartRadioMessage(data) || firstPartInCacheAndThisIsSecondPart(data)) {
            return true;
        }

        if (isFirstPart(data)) {
            cachedFirstPart = data; // cache first part now
        } else {
            LOGGER.info("Discarded multipart message {}", isSecondPart(data) ? "orphan second part" : "with part number > 2");
        }
        return false;
    }

    private AisRadioMsg parse(String data) {
        AisRadioMsg msg;
        if (cachedFirstPart == null) {
            msg = AisRadioMsgParser.parseToAisRadioMessage(data);
        } else {
            msg = AisRadioMsgParser.parseToAisRadioMessage(cachedFirstPart, data);
            cachedFirstPart = null;
        }
        return msg;
    }

    private boolean isFirstPart(String data) {
        return AisRadioMsgParser.getPartNumber(data) == 1;
    }

    private boolean isSecondPart(String data) {
        return AisRadioMsgParser.getPartNumber(data) == 2;
    }

    private boolean isSecondPartOfMultipartRadioMessage(String data) {
        return AisRadioMsgParser.isMultipartRadioMessage(data) && isSecondPart(data);
    }

    private boolean firstPartInCacheAndThisIsSecondPart(String data) {
        return cachedFirstPart != null && isSecondPart(data);
    }
}
