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

public class AisRadioMsg24BTest {

    @Test
    public void testSingleMessage() {

        String rawLinePartB = "!AIVDO,1,1,,B,H1c2;qDTijklmno31<<C970`43<1,0*28";

        List<String> expectedKeys = Arrays.asList(
                "Message ID",
                "Repeat indicator",
                "User ID",
                "Part number",
                "Type of ship and cargo type",
                "Vendor ID",
                "Call sign",
                "Dimension of ship/reference for position",
                "Type of electronic position fixing device",
                "Spare"
        );

        List<String> expectedValues = Arrays.asList(
                "24",
                "0",
                "112233445",
                "1",
                "36",
                "1234567",
                "CALLSIG",
                "10502348",
                "0",
                "1");

        List<String> expectedPublicValues = Arrays.asList(
                "24",
                "0",
                "112233445",
                "1",
                "36", // masked cargo type (for 30-39 & 50-59 the same as in authenticated stream)
                "1234567",
                "CALLSIG",
                "10502348",
                "0",
                "1");

        AisRadioMsg msg = AisRadioMsgParser.parseToAisRadioMessage(rawLinePartB);
        assertThat(msg, instanceOf(AisRadioMsg24B.class));

        Assert.assertThat(msg.toListOfKeys(), IsIterableContainingInOrder.contains(expectedKeys.toArray()));
        Assert.assertThat(msg.toListOfParsedValues(), IsIterableContainingInOrder.contains(expectedValues.toArray()));
        Assert.assertThat(msg.toListOfPublicParsedValues(), IsIterableContainingInOrder.contains(expectedPublicValues.toArray()));

        try {
            msg.toPublicGeoJsonDataString();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // ok, not a position message
        }
    }
}
