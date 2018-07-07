/*
 *  Copyright 2016-2018 Race Up Electric Division
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.raceup.ed.bms.models.stream.bms;

import org.json.JSONObject;

/**
 * Generic data coming from arduino
 */
public class BmsData {
    final String value;  // value of bms of segment
    private final String type;  // type of data
    private final int bms;  // number of bms broadcasting value
    public static final int BMS_DEVICE_PER_SEGMENT = 8;
    private static final String TYPE_KEY = "type";
    private static final String BMS_KEY = "BMS";
    private static final String VALUE_KEY = "value";
    private static final String VOLTAGE_KEY = "voltage";
    private static final String TEMPERATURE_KEY = "temperature";

    /**
     * Create and set params of new data
     *
     * @param type    type of data
     * @param value   value of bms of segment
     */
    public BmsData(String type, String bms, String value) {
        this.type = type;
        this.bms = Integer.parseInt(bms);
        this.value = value;
    }

    /**
     * Parse raw data
     *
     * @param root json data
     */
    public BmsData(JSONObject root) {
        this(
                root.getString(TYPE_KEY),
                root.getString(BMS_KEY),
                root.getString(VALUE_KEY)
        );
    }

    /**
     * Check if current data is a bms log
     *
     * @return True iff data represents a log
     */
    public boolean isStatusType() {
        return false;  // todo ask gorgo
    }

    /**
     * Check if current data is a battery bms value
     *
     * @return True iff data is a value
     */
    public boolean isValueType() {
        return (type.startsWith(VOLTAGE_KEY) ||
                type.startsWith(TEMPERATURE_KEY));
    }

    /**
     * Getter for type
     *
     * @return data type
     */
    String getType() {
        return type;
    }

    /**
     * Getter for bms
     *
     * @return data bms
     */
    public int getBms() {
        return bms;
    }
}
