package com.campus.client.ui;

import com.campus.client.service.FacilityService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FacilityInfoController {

    @FXML
    private Button facilityInfoBackButton;

    @FXML
    private Button facilityInfoConfirmButton;

    @FXML
    private ChoiceBox<String> chooseFacilityDropbox;

    @FXML
    private TextArea facilityInfoTextArea;

    private FacilityService facilityService;
    private Runnable onBack;

    private final ExecutorService uiWorker = Executors.newSingleThreadExecutor();

    public void setFacilityService(FacilityService facilityService){
        this.facilityService = facilityService;
        loadFacilitiesFromMcp();
    }

    public void setOnBack(Runnable onBack){
        this.onBack = onBack;
    }

    @FXML
    private void initialize(){
        facilityInfoConfirmButton.setDisable(true);
        facilityInfoTextArea.setText("Connecting to facility information...");
    }

    private void loadFacilitiesFromMcp(){
        if (facilityService == null){
            facilityInfoTextArea.setText("Facility service is not connected yet.");
            return;
        }

        facilityInfoConfirmButton.setDisable(true);
        facilityInfoTextArea.setText("Loading facilities from MCP server...");

        uiWorker.submit(() -> {
            try {
                Future<List<String>> futureIds = facilityService.getFacilityIds();
                List<String> facilityIds = futureIds.get();

                Platform.runLater(() -> {
                    chooseFacilityDropbox.getItems().setAll(facilityIds);

                    if(!facilityIds.isEmpty()){
                        chooseFacilityDropbox.setValue(facilityIds.get(0));
                        facilityInfoTextArea.setText("Choose a facility and click Confirm.");

                    } else {
                        facilityInfoTextArea.setText("No facilities found.");
                    }
                    facilityInfoConfirmButton.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(()-> {
                    facilityInfoTextArea.setText("Could not load facility information.\n\n" + "Make sure the MCP server is running. \n\n" + "Error: " + e.getMessage());
                    facilityInfoConfirmButton.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void facilityInfoConfirmButtonClicked(){
        if (facilityService == null) {
            facilityInfoTextArea.setText("Facility service is not connected yet.");
            return;
        }

        String selectedFacility = chooseFacilityDropbox.getValue();

        if(selectedFacility == null || selectedFacility.isBlank()){
            facilityInfoTextArea.setText("Please choose a facility first.");
            return;
        }

        facilityInfoConfirmButton.setDisable(true);
        facilityInfoTextArea.setText("Loading details for " + selectedFacility + "...");

        uiWorker.submit(() -> {
            try {
                Future<String> futureDetails = facilityService.getFacilityDetails(selectedFacility);
                String details = futureDetails.get();

                Platform.runLater(() -> {
                    facilityInfoTextArea.setText(details);
                    facilityInfoConfirmButton.setDisable(false);
                });
            } catch (Exception e){
                Platform.runLater(() -> {
                    facilityInfoTextArea.setText("Could not retrieve facility details.\n\n" + "Error: " + e.getMessage());
                    facilityInfoConfirmButton.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void facilityInfoBackButtonClicked(ActionEvent event){
        if (onBack != null){
            onBack.run();
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void shutdown() {
        uiWorker.shutdown();

        if(facilityService != null) {
            facilityService.shutdown();
        }
    }
}
