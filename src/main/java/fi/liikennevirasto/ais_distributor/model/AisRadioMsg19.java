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

import fi.liikennevirasto.ais_distributor.util.AisRadioMsgFilter;

import java.util.List;
import java.util.Map;

import static fi.liikennevirasto.ais_distributor.model.AisRadioMsgParameters.*;

public class AisRadioMsg19 extends AisPositionMsg implements AisShipTypeMsg {

    public AisRadioMsg19(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);

        add(SPARE, getUnsignedInteger(8));
        add(SOG, getUnsignedDecimal(10, 10, 1));
        add(POSITION_ACCURACY, getUnsignedInteger(1));
        add(LONGITUDE, getSignedDecimal(28, 600000, 6));
        add(LATITUDE, getSignedDecimal(27, 600000, 6));
        add(COG, getUnsignedDecimal(12, 10, 1));
        add(TRUE_HEADING, getUnsignedInteger(9));
        add(TIME_STAMP, getUnsignedInteger(6));
        add(SPARE2, getUnsignedInteger(4));
        add(NAME, getStringValue(120));
        add(TYPE_OF_SHIP_AND_CARGO_TYPE, getUnsignedInteger(8));
        add(DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, getDimensionOfShip30bits());
        add(TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, getUnsignedInteger(4));
        add(RAIM_FLAG, getUnsignedInteger(1));
        add(DTE, getUnsignedInteger(1));
        add(ASSIGNED_MODE_FLAG, getUnsignedInteger(1));
        add(SPARE3, getUnsignedInteger(4));
    }

    @Override
    public int getShipAndCargoType() {
        return getIntParam(TYPE_OF_SHIP_AND_CARGO_TYPE);
    }

    @Override
    protected Object getPublicValue(Map.Entry<String, Object> v) {
        Object value = v.getValue();
        return TYPE_OF_SHIP_AND_CARGO_TYPE.equals(v.getKey()) ?
                AisRadioMsgFilter.getMaskedShipAndCargoType((int) value) : value;
    }
}
