// Java
package com.microproject.microproject;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class MainController {

    @FXML
    private TableView<?> instructionQueueTable;

    @FXML
    private TableView<?> reservationStationsTable;

    @FXML
    private TableView<?> registerFileTable;

    @FXML
    private TableView<?> cacheTable;

    @FXML
    private void handleLoadInstructions() {
        // Code to load instructions from file or user input
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    // Methods to update tables each cycle
}