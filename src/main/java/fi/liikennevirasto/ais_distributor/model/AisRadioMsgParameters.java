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

public final class AisRadioMsgParameters {

    private AisRadioMsgParameters() {
    }

    public static final String AIS_VERSION_INDICATOR = "AIS version indicator";
    public static final String ALTITUDE_GNSS = "Altitude (GNSS)";
    public static final String ALTITUDE_SENSOR = "Altitude sensor";
    public static final String ASSIGNED_MODE_FLAG = "Assigned mode flag";
    public static final String CALL_SIGN = "Call sign";
    public static final String CLASS_B_BAND_FLAG = "Class B band flag";
    public static final String CLASS_B_DISPLAY_FLAG = "Class B display flag";
    public static final String CLASS_B_DSC_FLAG = "Class B DSC flag";
    public static final String CLASS_B_MESSAGE_22_FLAG = "Class B Message 22 flag";
    public static final String CLASS_B_UNIT_FLAG = "Class B unit flag";
    public static final String COG = "COG";
    public static final String COMMUNICATION_STATE = "Communication state";
    public static final String COMMUNICATION_STATE_SELECTOR_FLAG = "Communication state selector flag";
    public static final String DESTINATION = "Destination";
    public static final String DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION = "Dimension of ship/reference for position";
    public static final String DTE = "DTE";
    public static final String ETA = "ETA";
    public static final String IMO_NUMBER = "IMO number";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String MAXIMUM_PRESENT_STATIC_DRAUGHT = "Maximum present static draught";
    public static final String NAME = "Name";
    public static final String NAVIGATIONAL_STATUS = "Navigational status";
    public static final String PART_NUMBER = "Part number";
    public static final String POSITION_ACCURACY = "Position accuracy";
    public static final String POSITION_LATENCY = "Position latency";
    public static final String RAIM_FLAG = "RAIM-flag";
    public static final String RATE_OF_TURN = "Rate of turn ROTAIS";
    public static final String SOG = "SOG";
    public static final String SPARE = "Spare";
    public static final String SPARE2 = "Spare (2)";
    public static final String SPARE3 = "Spare (3)";
    public static final String SPECIAL_MANOEUVRE_INDICATOR = "Special manoeuvre indicator";
    public static final String TIME_STAMP = "Time stamp";
    public static final String TRUE_HEADING = "True heading";
    public static final String TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE = "Type of electronic position fixing device";
    public static final String TYPE_OF_SHIP_AND_CARGO_TYPE = "Type of ship and cargo type";
    public static final String VENDOR_ID = "Vendor ID";

    public static final String MESSAGE_ID = "Message ID";
    public static final String REPEAT_INDICATOR = "Repeat indicator";
    public static final String USER_ID = "User ID";
}
