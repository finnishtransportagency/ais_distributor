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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fi.liikennevirasto.ais_distributor.model.AisRadioMsgParameters.*;

public abstract class AisRadioMsg {

    private Map<String, Object> parameters = new LinkedHashMap<>(); // maintains insertion order
    private List<String> rawDataParts;
    private String binaryMsg;
    private int readOffset = 0;
    private long timestamp;

    public AisRadioMsg(String binaryMsg, List<String> rawDataParts) {
        this.rawDataParts = rawDataParts;
        this.binaryMsg = binaryMsg;
        this.timestamp = new Date().getTime();

        add(EXT_TIMESTAMP, timestamp);
        add(MESSAGE_ID, getUnsignedInteger(6));
        add(REPEAT_INDICATOR, getUnsignedInteger(2));
        add(USER_ID, getUnsignedInteger(30));
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    protected final <T> void add(String name, T value) {
        parameters.put(name, value);
    }

    protected final BigDecimal getDecimalParam(String name) {
        return (BigDecimal) parameters.get(name);
    }

    protected final int getIntParam(String name) {
        return (int) parameters.get(name);
    }

    public final int getUserId() {
        return getIntParam(USER_ID);
    }

    protected Set<Map.Entry<String, Object>> getParameterEntrySet() {
        return parameters.entrySet();
    }

    public final List<String> toListOfKeys() {
        return new ArrayList<>(parameters.keySet());
    }

    public final List<String> toListOfParsedValues() {
        return toListOfValues(v -> v.getValue().toString());
    }

    public final List<String> toListOfPublicParsedValues() {
        return toListOfValues(v -> getPublicValue(v).toString());
    }

    private List<String> toListOfValues(Function<Map.Entry<String, Object>, String> mapper) {
        return getParameterEntrySet().stream().map(mapper).collect(Collectors.toList());
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
        return Ais6BitConverter.to6BitEncodedString(getNextSubstring(size)).trim();
    }

    protected String getHexString(int size) {
        return Long.toHexString(Long.parseLong(getNextSubstring(size), 2)).toUpperCase(); // long for the 42 bits of Vendor ID
    }

    protected String getDimensionOfShip30bits() {
        return "A=" + getUnsignedInteger(9) +
                ",B=" + getUnsignedInteger(9) +
                ",C=" + getUnsignedInteger(6) +
                ",D=" + getUnsignedInteger(6);
    }

    protected String getEta20bits() {
        return getEtaPart(4) + getEtaPart(5) + getEtaPart(5) + getEtaPart(6);
    }

    private String getEtaPart(int size) {
        return String.format("%02d", getUnsignedInteger(size));
    }

    @Override
    public String toString() {
        return toRawAndParsedDataString();
    }

    public final List<String> getRawDataPartsWithTimestamp(){
        List<String> allRawData = new ArrayList<>();
        for(String str:rawDataParts) {
            allRawData.add("\\s:002300000,c:" + this.timestamp + "*xx\\" + str);
        }
        return allRawData;
    }

    public final String toRawAndParsedDataString() {
        return "{\"data\":{\"raw\":" + toRawDataString() + ",\"parsed\":\"" + toParsedDataString() + "\"}}";
    }

    private String toRawDataString() {
        return "[\"\\s:002300000,c:" + this.timestamp + "*xx\\" + String.join("\",\"", rawDataParts) + "\"]";
    }

    public final String toParsedDataString() {
        return toDataString(v -> getParsedEntry(v.getKey(), v.getValue()));
    }

    public final String toPublicParsedDataString() {
        return toDataString(v -> getParsedEntry(v.getKey(), getPublicValue(v)));
    }

    public abstract String toPublicGeoJsonDataString();

    public abstract boolean isPositionMsg();

    private String toDataString(Function<Map.Entry<String, Object>, String> mapper) {
        return getParameterEntrySet().stream().map(mapper).collect(Collectors.joining("|"));
    }

    private String getParsedEntry(String key, Object value) {
        return key + "§" + value;
    }

    protected Object getPublicValue(Map.Entry<String, Object> v) { // default implementation
        return v.getValue();
    }
}
