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

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class AisRadioMsg5Test {

    @Test
    public void testParseMultipartMessage() {

        String rawLine1 = "!AIVDM,2,1,0,A,58wt8Ui`g??r21`7S=:22058<v05Htp000000015>8OA;0sk,0*7B";
        String rawLine2 = "!AIVDM,2,2,0,A,eQ8823mDm3kP00000000000,2*5D";

        List<String> expectedKeys = Arrays.asList(
                "Message ID",
                "Repeat indicator",
                "User ID",
                "AIS version indicator",
                "IMO number",
                "Call sign",
                "Name",
                "Type of ship and cargo type",
                "Dimension of ship/reference for position",
                "Type of electronic position fixing device",
                "ETA",
                "Maximum present static draught",
                "Destination",
                "DTE",
                "Spare"
        );

        List<String> expectedValues = Arrays.asList(
                "5",
                "0",
                "603916439",
                "0",
                "439303422",
                "ZA83R",
                "ARCO AVON@@@@@@@@",
                "69",
                "237106251",
                "0",
                "244973",
                "13.2",
                "HOUSTON@@@@@@@@@@@",
                "0",
                "0");

        List<String> expectedPublicValues = Arrays.asList(
                "5",
                "0",
                "603916439",
                "0",
                "439303422",
                "ZA83R",
                "ARCO AVON@@@@@@@@",
                "60", // masked cargo type
                "237106251",
                "0",
                "244973",
                "13.2",
                "HOUSTON@@@@@@@@@@@",
                "0",
                "0");

        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLine1, rawLine2);
        assertThat(msg, instanceOf(AisRadioMsg5.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
        Assert.assertThat(msg.toListOfPublicParsedValues(), IsIterableContainingInOrder.contains(expectedPublicValues.toArray()));

        try {
            msg.toPublicGeoJsonDataString();
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok, not a position message
        }
    }
}
