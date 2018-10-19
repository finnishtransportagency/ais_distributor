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
package fi.liikennevirasto.ais_distributor.util;

import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;

public class AisConnectionDetails {
    private static final String USER_ARG = "user";
    private static final String PASSWD_ARG = "passwd";
    private static final String ADDRESS_ARG = "address";
    private static final String PORT_ARG = "port";

    private final URI uri;

    public AisConnectionDetails(Environment env) {
        String user = env.getProperty(USER_ARG);
        String passwd = env.getProperty(PASSWD_ARG);
        String address = env.getProperty(ADDRESS_ARG);
        Integer port = toInteger(env.getProperty(PORT_ARG));

        this.uri = createUri(user, passwd, address, port);
    }

    private URI createUri(String user, String passwd, String address, Integer port) {
        try {
            return new URI("ws", null, address, port, null, "username=" + user + "&passwd=" + passwd, null);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public URI getUri() {
        return uri;
    }

    private Integer toInteger(String port) {
        try {
            return Integer.valueOf(port);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
