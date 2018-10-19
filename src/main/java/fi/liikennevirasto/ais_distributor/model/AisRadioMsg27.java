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

public class AisRadioMsg27 extends AisPositionMsg {

    public AisRadioMsg27(String binaryMsg, List<String> rawDataParts) {
        super(binaryMsg, rawDataParts);

        add(POSITION_ACCURACY, getUnsignedInteger(1));
        add(RAIM_FLAG, getUnsignedInteger(1));
        add(NAVIGATIONAL_STATUS, getUnsignedInteger(4));
        add(LONGITUDE, getSignedDecimal(18, 600, 6));
        add(LATITUDE, getSignedDecimal(17, 600, 6));
        add(SOG, getUnsignedInteger(6));
        add(COG, getUnsignedInteger(9));
        add(POSITION_LATENCY, getUnsignedInteger(1));
        add(SPARE, getUnsignedInteger(1));
    }
}
