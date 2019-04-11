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

public class AisRadioMsg9Test {

    @Test
    public void testParseSingleMessage() throws JSONException {

        String rawLine = "!AIVDO,1,1,,A,95M2oQ@41Tr4L4H@eRvQ;2h20000,0*0D";

        List<String> expectedKeys = Arrays.asList(
                "Ext_timestamp",
                "Message ID",
                "Repeat indicator",
                "User ID",
                "Altitude (GNSS)",
                "SOG",
                "Position accuracy",
                "Longitude",
                "Latitude",
                "COG",
                "Time stamp",
                "Altitude sensor",
                "Spare",
                "DTE",
                "Spare (2)",
                "Assigned mode flag",
                "RAIM-flag",
                "Communication state selector flag",
                "Communication state in hex");
        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine);
        String timeStamp = String.valueOf(msg.getTimestamp());

        List<String> expectedValues = Arrays.asList(
                timeStamp,
                "9",
                "0",
                "366000005",
                "16",
                "100",
                "1",
                "-82.916460",
                "29.205750",
                "30.0",
                "11",
                "0",
                "0",
                "1",
                "0",
                "0",
                "0",
                "0",
                "0");


        System.out.println("************************************************** TESTS START **************************************************");
        System.out.println("getRawDataPartsWithTimestamp: Raakadata tekstinä\n" + msg.getRawDataPartsWithTimestamp().get(0));
        System.out.println("\ntoRawAndParsedDataString: Raakadata ja purettu data tekstinä\n" + msg.toRawAndParsedDataString());
        System.out.println("\ntoParsedDataString: Purettu data tekstinä\n" + msg.toParsedDataString());
        System.out.println("\ntoPublicParsedDataString: Suodatettu data tekstinä\n" + msg.toPublicParsedDataString());
        System.out.println("\ntoPublicGeoJsonDataString: Suodatettu data GeoJSON-formaatissa \n" + msg.toPublicGeoJsonDataString());
        assertThat(msg, instanceOf(AisRadioMsg9.class));

        //Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        //Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
        //Assert.assertThat(msg.toListOfPublicParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray())); // all values public

        //String expectedGeoJsonStr = GeoJsonTestUtil.getExpectedGeoJsonString(expectedKeys, expectedValues); // all values public
        //JSONAssert.assertEquals(expectedGeoJsonStr, msg.toPublicGeoJsonDataString(), JSONCompareMode.STRICT);
    }
}
