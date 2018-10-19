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
package fi.liikennevirasto.ais_distributor.model;

import fi.liikennevirasto.ais_distributor.util.GeoJsonParser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static fi.liikennevirasto.ais_distributor.model.AisRadioMsgParameters.LATITUDE;
import static fi.liikennevirasto.ais_distributor.model.AisRadioMsgParameters.LONGITUDE;

public abstract class AisPositionMsg extends AisRadioMsg {

    public AisPositionMsg(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);
    }

    @Override
    public final String toPublicGeoJsonDataString() {
        return GeoJsonParser.writeToGeoJsonString(getDecimalParam(LONGITUDE), getDecimalParam(LATITUDE), getGeoJsonProperties());
    }

    @Override
    public final boolean isPositionMsg() {
        return true;
    }

    private Map<String, Object> getGeoJsonProperties() {
        Map<String, Object> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Object> v : getParameterEntrySet()) {
            if (!skipFromPropertyList(v.getKey())) {
                ret.put(v.getKey(), getPublicValue(v));
            }
        }
        return ret;
    }

    private boolean skipFromPropertyList(String name) {
        return LONGITUDE.equals(name) || LATITUDE.equals(name);
    }
}
