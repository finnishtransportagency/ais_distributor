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
import static org.junit.Assert.*;

public class AisRadioMsg2Test {

    @Test
    public void testParseSingleMessage() throws JSONException {

        String rawLine = "!BSVDM,1,1,,B,2D4c@c001bQb0gLR3472qj8420S5,0*66";

        List<String> expectedKeys = Arrays.asList(
                "Ext_timestamp",
                "Message ID",
                "Repeat indicator",
                "User ID",
                "Navigational status",
                "Rate of turn ROTAIS",
                "SOG",
                "Position accuracy",
                "Longitude",
                "Latitude",
                "COG",
                "True heading",
                "Time stamp",
                "Special manoeuvre indicator",
                "Spare",
                "RAIM-flag",
                "Communication state in hex");
        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine);
        String timeStamp = String.valueOf(msg.getTimestamp());

        List<String> expectedValues = Arrays.asList(
                timeStamp,
                "2",
                "1",
                "273338540",
                "0",
                "0",
                "10.6",
                "1",
                "23.158583",
                "59.502980",
                "74.3",
                "68",
                "2",
                "0",
                "0",
                "1",
                "8C5");


        assertThat(msg, instanceOf(AisRadioMsg2.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
        Assert.assertThat(msg.toListOfPublicParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray())); // all values public

        String expectedGeoJsonStr = GeoJsonTestUtil.getExpectedGeoJsonString(expectedKeys, expectedValues); // all values public
        JSONAssert.assertEquals(expectedGeoJsonStr, msg.toPublicGeoJsonDataString(), JSONCompareMode.STRICT);
    }
}
