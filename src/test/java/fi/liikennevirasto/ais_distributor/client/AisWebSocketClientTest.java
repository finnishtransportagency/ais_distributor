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
package fi.liikennevirasto.ais_distributor.client;

import fi.liikennevirasto.ais_distributor.util.AisConnectionDetails;
import org.java_websocket.client.WebSocketClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AisWebSocketClientTest {

    private static final int PORT = SocketUtils.findAvailableTcpPort();

    @Autowired
    private WebSocketClient webSocketClient;

    private static TestAisWebSocketServer aisConnector;

    @BeforeClass
    public static void beforeClass() {
        aisConnector = new TestAisWebSocketServer("localhost", PORT);
        aisConnector.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        aisConnector.stop(30000);
    }

    @Test
    public void connectToCollector() throws Exception {
        assertEquals(true, webSocketClient.isOpen());
    }

    @TestConfiguration
    static class AisWebSocketClientTestConfiguration {

        @Bean
        public AisConnectionDetails aisConnectionDetails() {
            return new AisConnectionDetails(new MockEnvironment()
                    .withProperty("user", "user")
                    .withProperty("passwd", "passwd")
                    .withProperty("address", "localhost")
                    .withProperty("port", String.valueOf(PORT)));
        }

    }

}
