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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeoJsonTestUtil {

    public static String getExpectedGeoJsonString(List<String> expectedKeys, List<String> expectedValues) {
        int longIdx = expectedKeys.indexOf("Longitude");
        int latIdx = expectedKeys.indexOf("Latitude");
        Set<Integer> idxToSkip = new HashSet<>(Arrays.asList(longIdx, latIdx));
        return getGeoJsonString(expectedKeys, expectedValues, longIdx, latIdx, idxToSkip);
    }

    private static String getGeoJsonString(List<String> keys, List<String> values, int longIdx, int latIdx, Set<Integer> idxToSkip) {
        return "{\"type\":\"Feature\"," +
                "\"geometry\":{" + getGeoJsonGeometry(values, longIdx, latIdx) + "}," +
                "\"properties\":{" + getGeoJsonProperties(keys, values, idxToSkip) + "}}";
    }

    private static String getGeoJsonGeometry(List<String> values, int longIdx, int latIdx) {
        return "\"type\":\"Point\",\"coordinates\":[" + values.get(longIdx) + "," + values.get(latIdx) + "]";
    }

    private static String getGeoJsonProperties(List<String> keys, List<String> values, Set<Integer> idxToSkip) {
        StringBuilder sb = new StringBuilder();
        int addedCount = 0;
        for (int i = 0; i < keys.size(); i++) {
            if (!idxToSkip.contains(i)) {
                if (addedCount > 0) {
                    sb.append(",");
                }
                sb.append(getQuotedString(keys.get(i)));
                sb.append(":");
                sb.append(getValueToAppend(values.get(i)));
                addedCount++;
            }
        }
        return sb.toString();
    }

    private static String getValueToAppend(String str) {
        return isNumeric(str) ? str : getQuotedString(str);
    }

    private static String getQuotedString(String str) {
        return "\"" + str + "\"";
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
