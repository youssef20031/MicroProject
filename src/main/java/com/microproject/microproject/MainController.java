package com.microproject.microproject;

import com.microproject.microproject.model.CacheEntry;
import com.microproject.microproject.model.InstructionStatus;
import com.microproject.microproject.model.RegisterStatus;
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
    private TableColumn<InstructionStatus, String> opcodeColumn;

    @FXML
    private TableColumn<InstructionStatus, String> destinationColumn;

    @FXML
    private TableColumn<InstructionStatus, String> source1Column;

    @FXML
    private TableColumn<InstructionStatus, String> source2Column;
    
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
    private ObservableList<RegisterStatus> registerStatusList;

    @FXML
    private TableView<RegisterStatus> registerFileTable;

    @FXML
    private TableColumn<RegisterStatus, String> regNameColumn;

    @FXML
    private TableColumn<RegisterStatus, String> regValueColumn;

    @FXML
    private TableView<CacheEntry> cacheTable;

    @FXML
    private TableColumn<CacheEntry, Integer> cacheAddressColumn;

    @FXML
    private TableColumn<CacheEntry, Double> cacheDataColumn;

    private ObservableList<CacheEntry> cacheEntries;


    private TomasuloSimulator simulator;

    @FXML
    private void initialize() {
        instructionStatusList = FXCollections.observableArrayList();
        reservationStationEntries = FXCollections.observableArrayList();
        registerStatusList = FXCollections.observableArrayList(); // Initialize ObservableList

        // Initialize Instruction Queue Table Columns
        instructionColumn.setCellValueFactory(new PropertyValueFactory<>("instruction"));
        opcodeColumn.setCellValueFactory(new PropertyValueFactory<>("opcode"));
        source1Column.setCellValueFactory(new PropertyValueFactory<>("source1"));
        source2Column.setCellValueFactory(new PropertyValueFactory<>("source2"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));

        instructionQueueTable.setItems(instructionStatusList);
        instructionStatusList.add(new InstructionStatus("LOAD", "LW", "R1", "R2"));
        instructionStatusList.add(new InstructionStatus("ADD", "ADD.D", "R3", "R4"));


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

        regNameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        regValueColumn.setCellValueFactory(new PropertyValueFactory<>("registerValue"));

        registerFileTable.setItems(registerStatusList); // Bind ObservableList to TableView

        cacheEntries = FXCollections.observableArrayList();
        cacheAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cacheDataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        cacheTable.setItems(cacheEntries);


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

        reservationStationEntries.clear();
        reservationStationEntries.addAll(simulator.getReservationStationEntries());

        updateRegisterFileUI();

        updateCacheUI();
    }
    private void updateRegisterFileUI() {
        registerStatusList.clear();
        registerStatusList.addAll(simulator.getRegisterStatuses());
    }
    private void updateCacheUI() {
        cacheEntries.clear();
        cacheEntries.addAll(simulator.getCacheEntries());
    }
}