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

import fi.liikennevirasto.ais_distributor.util.Ais6BitConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AisRadioMsg {

    protected static final String SOG = "SOG";
    protected static final String POSITION_ACCURACY = "Position accuracy";
    protected static final String LONGITUDE = "Longitude";
    protected static final String LATITUDE = "Latitude";
    protected static final String COG = "COG";
    protected static final String TIME_STAMP = "Time stamp";
    protected static final String RESERVED_FOR_REGIONAL_APPLICATIONS = "Reserved for regional applications";
    protected static final String DTE = "DTE";
    protected static final String SPARE = "Spare";
    protected static final String RAIM_FLAG = "RAIM-flag";
    protected static final String COMMUNICATION_STATE = "Communication state";
    protected static final String RESERVED_FOR_REGIONAL_OR_LOCAL_APPLICATIONS = "Reserved for regional or local applications";
    protected static final String TRUE_HEADING = "True heading";

    private static final String MESSAGE_ID = "Message ID";
    private static final String REPEAT_INDICATOR = "Repeat indicator";
    private static final String USER_ID = "User ID";

    private Map<String, Object> parameters = new LinkedHashMap<>(); // maintains insertion order
    private List<String> rawDataParts;
    private String binaryMsg;
    private int readOffset = 0;

    public AisRadioMsg(String binaryMsg, List<String> rawDataParts) {
        this.rawDataParts = rawDataParts;
        this.binaryMsg = binaryMsg;

        add(MESSAGE_ID, getUnsignedInteger(6));
        add(REPEAT_INDICATOR, getUnsignedInteger(2));
        add(USER_ID, getUnsignedInteger(30));
    }

    protected <T> void add(String name, T value) {
        parameters.put(name, value);
    }

    private String getNextSubstring(int size) {
        String ret = binaryMsg.substring(readOffset, readOffset + size);
        readOffset += size;
        return ret;
    }

    protected int getUnsignedInteger(int size) {
        return Integer.parseInt(getNextSubstring(size), 2);
    }

    protected int getSignedInteger(int size) {
        int UPPER_LIMIT = 2 << (size - 2);
        int ret = getUnsignedInteger(size);
        return ret >= UPPER_LIMIT ? ret - (UPPER_LIMIT << 1) : ret;
    }

    protected BigDecimal getUnsignedDecimal(int size, long divisor, int scale) {
        return getDecimal(getUnsignedInteger(size), divisor, scale);
    }

    protected BigDecimal getSignedDecimal(int size, long divisor, int scale) {
        return getDecimal(getSignedInteger(size), divisor, scale);
    }

    private BigDecimal getDecimal(int intVal, long divisor, int scale) {
        return BigDecimal.valueOf(intVal).divide(BigDecimal.valueOf(divisor), scale, BigDecimal.ROUND_HALF_UP);
    }

    protected String getStringValue(int size) {
        return Ais6BitConverter.to6BitEncodedString(getNextSubstring(size));
    }

    public final List<String> getRawDataParts() {
        return rawDataParts;
    }

    @Override
    public String toString() {
        return toRawAndParsedDataString();
    }

    public final String toRawAndParsedDataString() {
        return "{\"data\": {\"raw\": " + toRawDataString() + ", \"converted\": \"" + toParsedDataString() + "\"}}";
    }

    private String toRawDataString() {
        return "[\"" + String.join("\", \"", rawDataParts) + "\"]";
    }

    public final String toParsedDataString() {
        return parameters.entrySet().stream().map(v -> v.getKey() + ": " + v.getValue()).collect(Collectors.joining(", "));
    }

    public final List<String> toListOfKeys() {
        return new ArrayList<>(parameters.keySet());
    }

    public final List<String> toListOfParsedValues() {
        return parameters.values().stream().map(Object::toString).collect(Collectors.toList());
    }
}
