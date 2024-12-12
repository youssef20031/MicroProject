package com.microproject.microproject;

import com.microproject.microproject.model.InstructionStatus;
import com.microproject.microproject.model.ReservationStationEntry;
import com.microproject.microproject.simulator.TomasuloSimulator;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

public class MainController {

    @FXML
    private TableView<InstructionStatus> instructionQueueTable;

    @FXML
    private TableColumn<InstructionStatus, String> instructionColumn;

    @FXML
    private TableColumn<InstructionStatus, Integer> issueCycleColumn;

    @FXML
    private TableColumn<InstructionStatus, Integer> startExecCycleColumn;

    @FXML
    private TableColumn<InstructionStatus, Integer> endExecCycleColumn;

    @FXML
    private TableColumn<InstructionStatus, Integer> writeBackCycleColumn;

    @FXML
    private TableView<ReservationStationEntry> reservationStationsTable;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsNameColumn;

    @FXML
    private TableColumn<ReservationStationEntry, Boolean> rsBusyColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsOpColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsVjColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsVkColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsQjColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsQkColumn;

    @FXML
    private TableColumn<ReservationStationEntry, String> rsDestColumn;

    private ObservableList<InstructionStatus> instructionStatusList;
    private ObservableList<ReservationStationEntry> reservationStationEntries;

    private TomasuloSimulator simulator;

    @FXML
    private void initialize() {
        instructionStatusList = FXCollections.observableArrayList();
        reservationStationEntries = FXCollections.observableArrayList();

        // Initialize Instruction Queue Table Columns
        instructionColumn.setCellValueFactory(new PropertyValueFactory<>("instruction"));
        issueCycleColumn.setCellValueFactory(new PropertyValueFactory<>("issueCycle"));
        startExecCycleColumn.setCellValueFactory(new PropertyValueFactory<>("startExecutionCycle"));
        endExecCycleColumn.setCellValueFactory(new PropertyValueFactory<>("endExecutionCycle"));
        writeBackCycleColumn.setCellValueFactory(new PropertyValueFactory<>("writeBackCycle"));

        instructionQueueTable.setItems(instructionStatusList);

        // Initialize Reservation Stations Table Columns
        rsNameColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStationName"));
        rsBusyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));
        rsOpColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        rsVjColumn.setCellValueFactory(new PropertyValueFactory<>("vj"));
        rsVkColumn.setCellValueFactory(new PropertyValueFactory<>("vk"));
        rsQjColumn.setCellValueFactory(new PropertyValueFactory<>("qj"));
        rsQkColumn.setCellValueFactory(new PropertyValueFactory<>("qk"));
        rsDestColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));

        reservationStationsTable.setItems(reservationStationEntries);

        simulator = new TomasuloSimulator();

        // Load initial data if needed
        //updateUI();
    }

    @FXML
    private void handleLoadInstructions(ActionEvent event) {
        // Implement loading instructions from file or user input
        // Example:
        // simulator.loadInstructions("path/to/instructions.txt");
        // After loading, update the UI
        updateUI();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleNextCycle(ActionEvent event) {
        simulator.nextCycle();
        updateUI();
    }

    public void setSimulator(TomasuloSimulator simulator) {

        this.simulator = simulator;
        updateUI();
    }

    private void updateUI() {
        instructionStatusList.clear();
        instructionStatusList.addAll(simulator.getInstructionStatuses());

        reservationStationEntries.clear();
        reservationStationEntries.addAll(simulator.getReservationStationEntries());
    }
}