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


package com.raceup.ed.bms.gui.frame.data;

import com.raceup.ed.bms.stream.bms.data.BmsValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Frame containing raw data from BMS
 */
public class DataFrame extends JPanel {
    private BmsDevice[] bmsDevices;  // battery segments

    public DataFrame(int[] numberOfCellsPerSegment) {
        super();
        setup(numberOfCellsPerSegment);
    }

    /**
     * Update value of cell
     *
     * @param data new data (coming from arduino)
     */
    public void updateCellValue(BmsValue data) {
        if (data.isTemperature()) {
            int[] cellToUpdate = new int[]{data.getCell() / 3 * 3, data
                    .getCell() / 3 * 3 + 1, data.getCell() / 3 * 3 + 2};
            for (int cell : cellToUpdate) {
                if (cell < segments[data.getSegment()].bmsDevices.length) {  //
                    // update only bmsDevices that exist
                    try {
                        segments[data.getSegment()].setTemperatureOfCell
                                (cell, data.getValue());
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }
        } else if (data.isVoltage()) {
            segments[data.getSegment()].setVoltageOfCell(data.getCell(),
                    data.getValue());
        }
    }

    /**
     * Setup gui and widgets
     *
     * @param numberOfCellsPerSegment list of number of bmsDevices per segment
     */
    private void setup(int[] numberOfCellsPerSegment) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  // add
        // components vertically
        segments = new Segment[numberOfCellsPerSegment.length];
        for (int i = 0; i < numberOfCellsPerSegment.length; i++) {  // open
            // all segments
            segments[i] = new Segment(numberOfCellsPerSegment[i]);

            add(new JLabel("Segment " + Integer.toString(i + 1)));  // label
            // with name of segment
            add(segments[i], BorderLayout.AFTER_LAST_LINE);  // add segments
            // in panel
            add(Box.createRigidArea(new Dimension(0, 10)));  // add spacing
        }

        setClickListeners();  // on mouse click -> open panel dialog
    }

    /**
     * Set listeners on mouse clicks
     */
    private void setClickListeners() {
        for (int s = 0; s < segments.length; s++) {
            for (int c = 0; c < segments[s].bmsDevices.length; c++) {
                final int cellNumber = c;
                final int segmentNumber = s;
                segments[s].bmsDevices[c].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            // left button
                            segments[segmentNumber].bmsDevices[cellNumber]
                                    .showDialog(
                                            "Cell " + Integer.toString
                                                    (cellNumber +
                                                    1) + " of segment " +
                                                    Integer
                                                    .toString(segmentNumber
                                                            + 1)
                            );
                        } else if (SwingUtilities.isRightMouseButton
                                (mouseEvent)) {  // right button
                            segments[segmentNumber].showDialog(
                                    "segment " + Integer.toString
                                            (segmentNumber + 1)
                            );
                        }
                    }
                });  // set onclick mouse event
            }
        }
    }
}
