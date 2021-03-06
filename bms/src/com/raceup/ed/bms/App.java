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

package com.raceup.ed.bms;

import com.raceup.ed.bms.control.Bms;
import com.raceup.ed.bms.logging.Debugger;
import com.raceup.ed.bms.models.battery.Pack;
import com.raceup.ed.bms.models.stream.serial.ArduinoSerial;
import com.raceup.ed.bms.ui.Gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.raceup.ed.bms.utils.Os.setNativeLookAndFeelOrFail;

/**
 * App driver program
 * Run BmsGUI or simple BmsUtils monitor here
 */
class App extends Debugger {
    private Pack battery;
    private ArduinoSerial arduino;
    private Bms bms;
    private Gui ui;

    public App() {
        super("APP", true);
        setup();
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private void setup() {
        setNativeLookAndFeelOrFail();

        try {
            battery = new Pack(8, 3);
        } catch (Exception e) {
            logException(e);
        }

        try {
            arduino = new ArduinoSerial(115200);
        } catch (Exception e) {
            logException(e);
        }

        try {
            bms = new Bms(arduino, battery);
        } catch (Exception e) {
            logException(e);
        }

        try {
            ui = new Gui(bms);
            ui.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    ui.close();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            logException(e);
        }
    }

    // todo measure lag between when serial receives data and when updates
    // model and screen
    public void start() {
        try {
            ui.open();  // start frontend
            Thread thread = new Thread(ui);  // start thread
            thread.start();
        } catch (Exception e) {
            logException(e);
        }
    }
}
