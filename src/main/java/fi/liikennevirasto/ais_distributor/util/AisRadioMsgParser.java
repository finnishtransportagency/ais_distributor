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

import fi.liikennevirasto.ais_distributor.model.AisRadioMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AisRadioMsgParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AisRadioMsgParser.class);
    private static final String PARSER_CLASS_BASE_NAME = "fi.liikennevirasto.ais_distributor.model.AisRadioMsg";
    private static final String SUPPORTED_MESSAGE_TYPE_REGEX = "!..VDM";
    private static final Set<String> SUPPORTED_MESSAGE_CLASS_SUFFIXES = new HashSet<>(Arrays.asList("1", "2", "3", "5", "9", "18", "19", "24A", "24B", "27"));

    private enum RAW_LINE_COLUMN {
        MESSAGE_TYPE, TOTAL_NUMBER_OF_SENTENCES_NEEDED, SENTENCE_NUMBER,
        SEQUENTIAL_MESSAGE_IDENTIFIER, AIS_CHANNEL, RADIO_MSG,
        NUMBER_OF_FILL_BITS_AND_CHECK_VALUE
    }

    public static boolean isSupportedMessageType(String data) {
        try {
            String msgType = getColumnValue(RAW_LINE_COLUMN.MESSAGE_TYPE, data);
            if (!msgType.matches(SUPPORTED_MESSAGE_TYPE_REGEX)) {
                LOGGER.info("Unsupported message type: {}", msgType);
                return false;
            }
            if (data.split(",").length < 6) {
                LOGGER.info("Too few columns: {}", data);
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.info("Unsupported message structure: {}", data);
            return false;
        }
    }

    public static AisRadioMsg parseToAisRadioMessage(String rawLine) {
        return parseToAisRadioMessage(Collections.singletonList(rawLine), getBinaryMsg(rawLine));
    }

    public static AisRadioMsg parseToAisRadioMessage(String firstRawLine, String secondRawLine) {
        return parseToAisRadioMessage(Arrays.asList(firstRawLine, secondRawLine), getBinaryMsg(firstRawLine).concat(getBinaryMsg(secondRawLine)));
    }

    private static AisRadioMsg parseToAisRadioMessage(List<String> rawLines, String binaryMsg) {
        String msgClassSuffix = getMessageClassSuffix(binaryMsg);

        if (!SUPPORTED_MESSAGE_CLASS_SUFFIXES.contains(msgClassSuffix)) {
            LOGGER.debug("Unsupported message: {}", msgClassSuffix);
            return null;
        }

        try {
            return (AisRadioMsg) Class.forName(PARSER_CLASS_BASE_NAME + msgClassSuffix)
                    .getDeclaredConstructor(String.class, List.class)
                    .newInstance(binaryMsg, rawLines);
        } catch (ClassNotFoundException e) {
            LOGGER.info("Parser class not available for message {}", msgClassSuffix);
            return null;
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            LOGGER.error("Parsing failed for {}", rawLines);
            return null;
        }
    }

    public static boolean isMultipartRadioMessage(String rawLine) {
        return Integer.parseInt(getColumnValue(RAW_LINE_COLUMN.TOTAL_NUMBER_OF_SENTENCES_NEEDED, rawLine)) > 1;
    }

    public static int getPartNumber(String rawLine) {
        return Integer.parseInt(getColumnValue(RAW_LINE_COLUMN.SENTENCE_NUMBER, rawLine));
    }

    public static void validateChecksum(String rawLine) {
        String calculated = calculateChecksum(rawLine);
        String received = getColumnValue(RAW_LINE_COLUMN.NUMBER_OF_FILL_BITS_AND_CHECK_VALUE, rawLine).split("\\*")[1];

        String info = "[calculated=" + calculated + ", received=" + received + ", sentence=" + rawLine + "]";
        if (!calculated.equals(received)) {
            LOGGER.error("Wrong checksum {}", info);
        } else {
            LOGGER.debug("Checksum ok {}", info);
        }
    }

    private static String calculateChecksum(String rawLine) {
        int sum = 0;
        for (int i = 1; i < rawLine.length(); i++) {
            char ch = rawLine.charAt(i);
            if (ch == '*') {
                break;
            }
            sum = sum == 0 ? ch : sum ^ ch;
        }

        return String.format("%02X", sum);
    }

    private static String getColumnValue(RAW_LINE_COLUMN column, String rawLine) {
        return rawLine.split(",")[column.ordinal()];
    }

    private static String getBinaryMsg(String rawLine) {
        return Ais6BitConverter.to6Bit(getColumnValue(RAW_LINE_COLUMN.RADIO_MSG, rawLine));
    }

    private static String getMessageClassSuffix(String binaryMsg) {
        int msgId = Integer.parseInt(binaryMsg.substring(0, 6), 2);
        String partStr = getPartIdString(binaryMsg, msgId);
        return Integer.toString(msgId) + partStr;
    }

    private static String getPartIdString(String binaryMsg, int msgId) {
        if (msgId == 24) {
            return Integer.parseInt(binaryMsg.substring(38, 40), 2) == 1 ? "B" : "A";
        }
        return "";
    }
}
