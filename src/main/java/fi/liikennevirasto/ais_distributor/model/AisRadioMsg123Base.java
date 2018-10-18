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

public class AisRadioMsg123Base extends AisRadioMsg {

    private static final String NAVIGATIONAL_STATUS = "Navigational status";
    private static final String RATE_OF_TURN = "Rate of turn";

    public AisRadioMsg123Base(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);

        add(NAVIGATIONAL_STATUS, getUnsignedInteger(4));
        add(RATE_OF_TURN, getSignedInteger(8));
        add(SOG, getUnsignedDecimal(10, 10, 1));
        add(POSITION_ACCURACY, getUnsignedInteger(1));
        add(LONGITUDE, getSignedDecimal(28, 600000, 6));
        add(LATITUDE, getSignedDecimal(27, 600000, 6));
        add(COG, getUnsignedDecimal(12, 10, 1));
        add(TRUE_HEADING, getUnsignedInteger(9));
        add(TIME_STAMP, getUnsignedInteger(6));
        add(RESERVED_FOR_REGIONAL_APPLICATIONS, getUnsignedInteger(4));
        add(SPARE, getUnsignedInteger(1));
        add(RAIM_FLAG, getUnsignedInteger(1));
        add(COMMUNICATION_STATE, getUnsignedInteger(19));
    }
}
