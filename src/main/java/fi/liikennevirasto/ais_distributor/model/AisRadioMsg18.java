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

import static fi.liikennevirasto.ais_distributor.model.AisRadioMsgParameters.*;

public class AisRadioMsg18 extends AisPositionMsg {

    public AisRadioMsg18(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);

        add(SPARE, getUnsignedInteger(8));
        add(SOG, getUnsignedDecimal(10, 10, 1));
        add(POSITION_ACCURACY, getUnsignedInteger(1));
        add(LONGITUDE, getSignedDecimal(28, 600000, 6));
        add(LATITUDE, getSignedDecimal(27, 600000, 6));
        add(COG, getUnsignedDecimal(12, 10, 1));
        add(TRUE_HEADING, getUnsignedInteger(9));
        add(TIME_STAMP, getUnsignedInteger(6));
        add(SPARE2, getUnsignedInteger(2));
        add(CLASS_B_UNIT_FLAG, getUnsignedInteger(1));
        add(CLASS_B_DISPLAY_FLAG, getUnsignedInteger(1));
        add(CLASS_B_DSC_FLAG, getUnsignedInteger(1));
        add(CLASS_B_BAND_FLAG, getUnsignedInteger(1));
        add(CLASS_B_MESSAGE_22_FLAG, getUnsignedInteger(1));
        add(ASSIGNED_MODE_FLAG, getUnsignedInteger(1));
        add(RAIM_FLAG, getUnsignedInteger(1));
        add(COMMUNICATION_STATE_SELECTOR_FLAG, getUnsignedInteger(1));
        add(COMMUNICATION_STATE, getUnsignedInteger(19));
    }
}
