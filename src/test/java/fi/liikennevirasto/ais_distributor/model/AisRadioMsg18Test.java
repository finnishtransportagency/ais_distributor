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

import fi.liikennevirasto.ais_distributor.util.AisRadioMsgParser;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class AisRadioMsg18Test {

    @Test
    public void testParseSingleMessage() {

        String rawLine = "!ABVDM,1,1,2,A,BCKKGKP000GOK>`UwwmiOwr5kP06,0*7C";

        List<String> expectedKeys = Arrays.asList(
                "Message ID",
                "Repeat indicator",
                "User ID",
                "Reserved for regional or local applications",
                "SOG",
                "Position accuracy",
                "Longitude",
                "Latitude",
                "COG",
                "True heading",
                "Time stamp",
                "Reserved for regional applications",
                "Spare",
                "RAIM-flag",
                "Communication state selector flag",
                "Communication state");

        List<String> expectedValues = Arrays.asList(
                "18",
                "1",
                "230086510",
                "0",
                "0.0",
                "0",
                "20.526768",
                "60.074662",
                "181.5",
                "511",
                "52",
                "2",
                "14",
                "0",
                "1",
                "393222");

        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine);
        assertThat(msg, instanceOf(AisRadioMsg18.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
    }
}
