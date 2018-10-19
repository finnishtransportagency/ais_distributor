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
package fi.liikennevirasto.ais_distributor.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.liikennevirasto.ais_distributor.model.GeoJsonFeatureObject;
import fi.liikennevirasto.ais_distributor.model.GeoJsonPointGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class GeoJsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoJsonParser.class);

    public static String writeToGeoJsonString(BigDecimal longitude, BigDecimal latitude, Map<String, Object> properties) {
        try {
            return objectMapper.writeValueAsString(new GeoJsonFeatureObject(new GeoJsonPointGeometry(longitude, latitude), properties));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize JSON", e);
            return null;
        }
    }
}
