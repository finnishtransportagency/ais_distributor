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

import java.util.List;

public class AisRadioMsg19 extends AisRadioMsg {

    private static final String NAME = "Name";
    private static final String TYPE_OF_SHIP_AND_CARGO_TYPE = "Type of ship and cargo type";
    private static final String DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION = "Dimension of ship/reference for position";
    private static final String TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE = "Type of electronic position fixing device";

    public AisRadioMsg19(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);

        add(RESERVED_FOR_REGIONAL_OR_LOCAL_APPLICATIONS, getUnsignedInteger(8));
        add(SOG, getUnsignedDecimal(10, 10, 1));
        add(POSITION_ACCURACY, getUnsignedInteger(1));
        add(LONGITUDE, getSignedDecimal(28, 600000, 6));
        add(LATITUDE, getSignedDecimal(27, 600000, 6));
        add(COG, getUnsignedDecimal(12, 10, 1));
        add(TRUE_HEADING, getUnsignedInteger(9));
        add(TIME_STAMP, getUnsignedInteger(6));
        add(RESERVED_FOR_REGIONAL_APPLICATIONS, getUnsignedInteger(4));
        add(NAME, getStringValue(120));
        add(TYPE_OF_SHIP_AND_CARGO_TYPE, getUnsignedInteger(8));
        add(DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, getUnsignedInteger(30));
        add(TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, getUnsignedInteger(4));
        add(RAIM_FLAG, getUnsignedInteger(1));
        add(DTE, getUnsignedInteger(1));
        add(SPARE, getUnsignedInteger(5));
    }
}
