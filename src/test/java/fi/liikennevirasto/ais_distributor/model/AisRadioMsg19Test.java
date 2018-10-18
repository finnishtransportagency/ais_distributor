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

public class AisRadioMsg19Test {

    @Test
    public void testParseMultipartMessage() {

        String rawLine1 = "!AIVDM,2,1,0,B,C8u:8C@t7@TnGCKfm6Po`e6N`:Va0L2J;06HV50JV?SjBPL3,0*28";
        String rawLine2 = "!AIVDM,2,2,0,B,11RP,0*17";

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
                "Name",
                "Type of ship and cargo type",
                "Dimension of ship/reference for position",
                "Type of electronic position fixing device",
                "RAIM-flag",
                "DTE",
                "Spare"
        );

        List<String> expectedValues = Arrays.asList(
                "19",
                "0",
                "601000013",
                "15",
                "2.9",
                "0",
                "32.199530",
                "-29.837480",
                "89.0",
                "90",
                "12",
                "15",
                "TEST NAME CLSB MSG19",
                "37",
                "14704771",
                "1",
                "0",
                "1",
                "0");

        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine1, rawLine2);
        assertThat(msg, instanceOf(AisRadioMsg19.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
    }
}
