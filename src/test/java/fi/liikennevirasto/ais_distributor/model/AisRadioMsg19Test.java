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
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class AisRadioMsg19Test {

    @Test
    public void testParseMultipartMessage() throws JSONException {

        String rawLine1 = "!AIVDM,2,1,0,B,C8u:8C@t7@TnGCKfm6Po`e6N`:Va0L2J;06HV50JV?SjBPL3,0*28";
        String rawLine2 = "!AIVDM,2,2,0,B,11RP,0*17";

        List<String> expectedKeys = Arrays.asList(
                "Ext_timestamp",
                "Message ID",
                "Repeat indicator",
                "User ID",
                "Spare",
                "SOG",
                "Position accuracy",
                "Longitude",
                "Latitude",
                "COG",
                "True heading",
                "Time stamp",
                "Spare (2)",
                "Name",
                "Type of ship and cargo type",
                "Dimension of ship/reference for position",
                "Type of electronic position fixing device",
                "RAIM-flag",
                "DTE",
                "Assigned mode flag",
                "Spare (3)"
        );

        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine1, rawLine2);
        String timeStamp = String.valueOf(msg.getTimestamp());

        List<String> expectedValues = Arrays.asList(
                timeStamp,
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
                "A=7,B=6,C=2,D=3",
                "1",
                "0",
                "1",
                "0",
                "0");

        List<String> expectedPublicValues = Arrays.asList(
                timeStamp,
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
                "37", // masked cargo type (for 30-39 & 50-59 the same as in authenticated stream)
                "A=7,B=6,C=2,D=3",
                "1",
                "0",
                "1",
                "0",
                "0");


        assertThat(msg, instanceOf(AisRadioMsg19.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
        Assert.assertThat(msg.toListOfPublicParsedValues(), IsIterableContainingInOrder.contains(expectedPublicValues.toArray()));

        String expectedGeoJsonStr = GeoJsonTestUtil.getExpectedGeoJsonString(expectedKeys, expectedPublicValues);
        JSONAssert.assertEquals(expectedGeoJsonStr, msg.toPublicGeoJsonDataString(), JSONCompareMode.STRICT);
    }
}
