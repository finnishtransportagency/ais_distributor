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
package fi.liikennevirasto.ais_distributor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "ais")
public class AisDistributorProperties {

    private Connector connector;
    private Distributor distributor;

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public static class Connector {

        private String aisDataEndpoint;

        public String getAisDataEndpoint() {
            return aisDataEndpoint;
        }

        public void setAisDataEndpoint(String aisDataEndpoint) {
            this.aisDataEndpoint = aisDataEndpoint;
        }

    }

    public static class Distributor {

        private List<Endpoint> endpoints;
        private List<Credentials> credentials;

        public List<Endpoint> getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(List<Endpoint> endpoints) {
            this.endpoints = endpoints;
        }

        public List<Credentials> getCredentials() {
            return credentials;
        }

        public void setCredentials(List<Credentials> credentials) {
            this.credentials = credentials;
        }

        public static class Endpoint {

            private String url;
            private String dataBean;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDataBean() {
                return dataBean;
            }

            public void setDataBean(String dataBean) {
                this.dataBean = dataBean;
            }

        }

        public static class Credentials {

            private String username;
            private String password;
            private List<Endpoint> allowedEndpoints;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public List<Endpoint> getAllowedEndpoints() {
                return allowedEndpoints;
            }

            public void setAllowedEndpoints(List<Endpoint> allowedEndpoints) {
                this.allowedEndpoints = allowedEndpoints;
            }

            public static class Endpoint {

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

            }

        }

    }

}
