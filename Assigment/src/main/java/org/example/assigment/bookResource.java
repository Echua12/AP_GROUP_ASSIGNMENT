package org.example.assigment;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx. scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;


public class bookResource extends Application implements Initializable{
    private String selectedFacility;
    private LocalDate selectedDate;

    private List<String> selectedTimes = new ArrayList<>();
    private List<Button> selectedTimeButtons = new ArrayList<>();
    private List<Button> allTimeButtons;
    private Map<String, Button> timeButtonMap = new HashMap<>();
    private List<String> originalButtonTexts = new ArrayList<>();
    private Set<Integer> closedSlots = new HashSet<>();
    private Set<Integer> bookedSlots = new HashSet<>();

    private File file = new File("./src", "bookings.txt");

    private String studentId = "0395181";
    private String startTime;
    private String endTime;

    @Override
    public void start(Stage stg) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(bookResource.class.getResource("BookResourcePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stg.setScene(scene);
        stg.show();
    }

    @FXML
    Label bookResourcePageLabel, facilityLabel, facilityNotiLabel, roomNotiLabel, roomLabel;
    @FXML
    Button bookResourceBackButton;
    @FXML
    Button time1Button, time2Button, time3Button, time4Button, time5Button, time6Button, time7Button, time8Button, time9Button, time10Button, time11Button, time12Button, time13Button, time14Button, time15Button, time16Button;
    @FXML
    Button bookButton;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> facilityDropbox;
    @FXML
    ComboBox<String> roomDropbox;
    @FXML
    ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        facilityNotiLabel.setVisible(true);
        facilityNotiLabel.setManaged(true);

        roomNotiLabel.setVisible(false);
        roomNotiLabel.setManaged(false);

        scrollPane.setVisible(false);
        scrollPane.setManaged(false);

        roomLabel.setVisible(false);
        roomLabel.setManaged(false);

        roomDropbox.setVisible(false);
        roomDropbox.setManaged(false);

        facilityDropbox.getItems().addAll(
                "Discussion Room",
                "Group Study Room",
                "Computer Lab",
                "Study Pod",
                "Basketball Court"
        );

        facilityDropbox.setValue(null);
        roomDropbox.setValue(null);

        facilityDropbox.setPromptText("Choose Facility");
        roomDropbox.setPromptText("Choose Room");

        // Listen for changes
        facilityDropbox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            handleFacilitySelected(newValue);
        });

        roomDropbox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
            handleRoomSelected(newValue);
        });

        allTimeButtons = List.of(
                time1Button,
                time2Button,
                time3Button,
                time4Button,
                time5Button,
                time6Button,
                time7Button,
                time8Button,
                time9Button,
                time10Button,
                time11Button,
                time12Button,
                time13Button,
                time14Button,
                time15Button,
                time16Button
        );

        for(Button button : allTimeButtons){
            originalButtonTexts.add(button.getText());
        }

        timeButtonMap.put("7 am", time1Button);
        timeButtonMap.put("8 am", time2Button);
        timeButtonMap.put("9 am", time3Button);
        timeButtonMap.put("10 am", time4Button);
        timeButtonMap.put("11 am", time5Button);
        timeButtonMap.put("12 pm", time6Button);
        timeButtonMap.put("1 pm", time7Button);
        timeButtonMap.put("2 pm", time8Button);
        timeButtonMap.put("3 pm", time9Button);
        timeButtonMap.put("4 pm", time10Button);
        timeButtonMap.put("5 pm", time11Button);
        timeButtonMap.put("6 pm", time12Button);
        timeButtonMap.put("7 pm", time13Button);
        timeButtonMap.put("8 pm", time14Button);
        timeButtonMap.put("9 pm", time15Button);
        timeButtonMap.put("10 pm", time16Button);

        datePicker.setValue(LocalDate.now());

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
//            updateBookedSlots();
            refreshTimeButtons();
        });
    }

    private void handleFacilitySelected(String facility){
        clearSelectedTimes();

        if(facility == null){
            return;
        }

        facilityNotiLabel.setVisible(false);
        facilityNotiLabel.setManaged(false);

        roomLabel.setVisible(true);
        roomLabel.setManaged(true);

        roomDropbox.setVisible(true);
        roomDropbox.setManaged(true);

        roomNotiLabel.setVisible(true);
        roomNotiLabel.setManaged(true);

        scrollPane.setVisible(false);
        scrollPane.setManaged(false);

        updateRooms(facility);

        refreshTimeButtons();
    }

    private void updateRooms(String facility){
        roomDropbox.getItems().clear();
        roomDropbox.setValue(null);

        if(facility == null){
            return;
        }

        switch(facility){
            case "Discussion Room":
                roomDropbox.getItems().addAll(
                        "D9A.01",
                        "D9A.02",
                        "D9A.03",
                        "D9B.01",
                        "D9B.02",
                        "D9B.03",
                        "E9A.01",
                        "E9A.02",
                        "E9A.03",
                        "E9B.01",
                        "E9B.02",
                        "E9B.03"
                );
                break;
            case "Group Study Room":
                roomDropbox.getItems().addAll(
                        "E7.01",
                        "E7.02",
                        "E7.03",
                        "E7.04",
                        "E7.05",
                        "E7.06",
                        "E7.07"
                );
                break;
            case "Computer Lab":
                roomDropbox.getItems().addAll(
                        "C7.01",
                        "C7.02",
                        "C7.03",
                        "C7.04",
                        "C7.05",
                        "C7.06",
                        "C7.07",
                        "C7.08",
                        "C7.09",
                        "C7.10",
                        "C7.11",
                        "C7.12",
                        "C7.13",
                        "C7.14"
                );
                break;
            case "Study Pod":
                roomDropbox.getItems().addAll(
                        "KA-P1",
                        "KA-P2",
                        "KA-P3",
                        "KA-P4",
                        "KA-P5",
                        "KA-P6",
                        "KA-P7",
                        "KA-P8",
                        "KA-P9",
                        "KA-P10",
                        "KA-P11",
                        "KA-P12",
                        "KA-P13",
                        "KA-P14",
                        "KA-P15",
                        "KA-P16",
                        "KA-P17",
                        "KA-P18",
                        "KA-P19",
                        "KA-P20"
                );
                break;
            case "Basketball Court":
                roomDropbox.getItems().addAll(
                        "SP-B1",
                        "SP-B2"
                );
        }
    }

    @FXML
    void bookResourceBackButtonClicked(){

    }

    @FXML
    void bookResourceConfirmButtonClicked(){
        selectedFacility = facilityDropbox.getValue();
    }

    @FXML
    void bookButtonClicked() throws IOException {
        selectedDate = datePicker.getValue();
        String finalTimes = getMergedTimeString();
        String chosenFacility = facilityDropbox.getValue();
        String chosenRoom = roomDropbox.getValue();


        System.out.println(selectedDate);
        System.out.println(finalTimes);
        System.out.println(chosenFacility);
        System.out.println(chosenRoom);

        if (startTime == null || endTime == null) {
            showAlert("Error", "Please select a time slot.", 1);
            return;
        }

        PrintWriter pw = new PrintWriter(new FileWriter(file, true));

        pw.print(chosenRoom + "," + chosenFacility + "," + studentId + "," + selectedDate + "," + startTime + "," + endTime);
        pw.println();

        pw.close();

        showAlert("Booking Successful", "Your booking is successful", 2);

        resetBookingForm();
    }

    @FXML
    void timeButtonClicked(ActionEvent event){
//        System.out.println("Selected count = " + selectedTimeButtons.size());

        Button clickedButton = (Button) event.getSource();

        // User clicked an already selected button -> deselect
        if (selectedTimeButtons.contains(clickedButton)) {
            selectedTimeButtons.remove(clickedButton);
            clickedButton.setStyle("");

//            enableAvailableButtons();
//            re-enable everything
            for(Button button : allTimeButtons){
                if(!bookedSlots.contains(allTimeButtons.indexOf(button)) &&
                    !closedSlots.contains(allTimeButtons.indexOf(button))){
                    button.setDisable(false);
                }
            }

            if(selectedTimeButtons.size() == 1){
                Button selected = selectedTimeButtons.get(0);
                int index = allTimeButtons.indexOf(selected);

                for(int i=0; i<allTimeButtons.size(); i++){
                    if(i == index){
                        continue;
                    }

                    boolean isAdjacent = (i == index - 1 || i == index + 1);

                    if(!isAdjacent || bookedSlots.contains(i) || closedSlots.contains(i)){
                        allTimeButtons.get(i).setDisable(true);
                    }
                }
            }

            String finalTimes = getMergedTimeString();
            System.out.println(finalTimes);
            return;
        }

        // Maximum 2 hours
        if (selectedTimeButtons.size() == 2) {
            showAlert("Booking Limit", "Maximum booking duration is 2 consecutive hours.", 1);
            return;
        }

        // Second selection must be consecutive
        if (selectedTimeButtons.size() == 1) {

            Button firstButton = selectedTimeButtons.get(0);

            int firstIndex = allTimeButtons.indexOf(firstButton);
            int secondIndex = allTimeButtons.indexOf(clickedButton);

            if (Math.abs(firstIndex - secondIndex) != 1) {
                showAlert(
                        "Invalid Selection",
                        "Time has to be chosen consecutively.",
                        1
                );
                return;
            }
        }

        // Add selection
        selectedTimeButtons.add(clickedButton);

        clickedButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white;"
        );

        // Disable remaining buttons after 2 selections
        if (selectedTimeButtons.size() == 2) {
            disableRemainingButtons();
        }

        String finalTimes = getMergedTimeString();
        System.out.println(finalTimes);
    }

    private void disableRemainingButtons(){
        for(Button button : allTimeButtons){
            if(!selectedTimeButtons.contains(button)){
                button.setDisable(true);
            }
        }
    }

    private void enableAvailableButtons(){
        // First restore booking status
//        updateBookedSlotsWithoutReset();
        refreshTimeButtons();

        // Enable only non-booked buttons
        for(Button button : allTimeButtons){
            if(!button.getText().contains("BOOKED") && !button.getText().contains("CLOSED")){
                button.setDisable(false);
            }
        }

        if(selectedTimeButtons.size() == 1){
            Button selected = selectedTimeButtons.get(0);

            int index = allTimeButtons.indexOf(selected);

            for(int i=0; i<allTimeButtons.size(); i++){
                if(i == index){
                    continue;
                }

                if(i == index + 1 &&
                        !allTimeButtons.get(i).getText().contains("BOOKED")){
                    continue;
                }

                if(i == index - 1 &&
                        !allTimeButtons.get(i).getText().contains("BOOKED")){
                    continue;
                }

                allTimeButtons.get(i).setDisable(true);
            }
        }
    }

    private String getMergedTimeString(){
        if(selectedTimeButtons.isEmpty()){
            return "";
        }

        if(selectedTimeButtons.size() == 1){
            String singleTime = selectedTimeButtons.get(0).getText();

            startTime = singleTime.split("-")[0].trim();
            endTime = singleTime.split("-")[1].trim();

            return startTime + "-" + endTime;
        }

        selectedTimeButtons.sort(
                (a,b) ->
                        Integer.compare(
                                allTimeButtons.indexOf(a),
                                allTimeButtons.indexOf(b)
                        )
        );

        String first = selectedTimeButtons.get(0).getText();
        String second = selectedTimeButtons.get(1).getText();

        startTime = first.split("-")[0];
        endTime = second.split("-")[1];

        startTime = startTime.trim();
        endTime = endTime.trim();

        return startTime + "-" + endTime;
    }

    private void updateBookedSlots(){
        bookedSlots.clear();

        String room = roomDropbox.getValue();
        LocalDate date = datePicker.getValue();

        if(room == null || date == null){
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");

                if(!parts[0].equals(room)){
                    continue;
                }

                if(!LocalDate.parse(parts[3]).equals(date)){
                    continue;
                }

                int start = getTimeIndex(parts[4].trim());
                int end = getTimeIndex(parts[5].trim());

                for(int i=start; i<end; i++){
                    bookedSlots.add(i);
                }

//                String bookedRoom = parts[0];
//                LocalDate bookedDate = LocalDate.parse(parts[3]);
//
//                if(!bookedRoom.equals(room) || !bookedDate.equals(date)){
//                    continue;
//                }
//
//                String bookedStart = parts[4].trim();
//                String bookedEnd = parts[5].trim();
//
//                disabledBookedRange(bookedStart, bookedEnd);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void disabledBookedRange(String start, String end){
        int startIndex = getTimeIndex(start);
        int endIndex = getTimeIndex(end);

        for(int i=startIndex; i<endIndex; i++){
            Button button = allTimeButtons.get(i);

            button.setDisable(true);

            button.setStyle(
                    "-fx-background-color: #d9534f;" +
                    "-fx-text-fill: white;"
            );

//            button.setText(button.getText() + "\nBOOKED");
        }
    }

    private int getTimeIndex(String time){
        switch (time) {
            case "7 am": return 0;
            case "8 am": return 1;
            case "9 am": return 2;
            case "10 am": return 3;
            case "11 am": return 4;
            case "12 pm": return 5;
            case "1 pm": return 6;
            case "2 pm": return 7;
            case "3 pm": return 8;
            case "4 pm": return 9;
            case "5 pm": return 10;
            case "6 pm": return 11;
            case "7 pm": return 12;
            case "8 pm": return 13;
            case "9 pm": return 14;
            case "10 pm": return 15;
            default: return -1;
        }
    }

    private void resetTimeButtons(){
        for(Button button : allTimeButtons){
            button.setDisable(false);
            button.setStyle("");

            button.setText(button.getText().replace("\nBOOKED", ""));
            button.setText(button.getText().replace("\nCLOSED", ""));
        }
    }

//    private boolean isClosedButton(Button button){
//        return button.getText().contains("CLOSED");
//    }

    private void handleRoomSelected(String room){
        clearSelectedTimes();

        if(room == null){
            return;
        }

        roomNotiLabel.setVisible(false);
        roomNotiLabel.setManaged(false);

        scrollPane.setVisible(true);
        scrollPane.setManaged(true);

//        updateBookedSlots();
        refreshTimeButtons();
    }

    private void updateBookedSlotsWithoutReset(){
        String room = roomDropbox.getValue();
        LocalDate date = datePicker.getValue();

        if(room == null || date == null){
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");

                if(!parts[0].equals(room)){
                    continue;
                }

                if(!LocalDate.parse(parts[3]).equals(date)){
                    continue;
                }

                disabledBookedRange(parts[4].trim(), parts[5].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearSelectedTimes(){
        selectedTimeButtons.clear();

        startTime = null;
        endTime = null;

        for(Button button: allTimeButtons){
            if(!button.getText().contains("BOOKED") && !button.getText().contains("CLOSED")){
                button.setStyle("");
            }
        }
    }

    private void applyFacilityOperatingHours(String facility){
//        for(int i=0; i<allTimeButtons.size(); i++){
//            Button button = allTimeButtons.get(i);
//
//            button.setVisible(true);
//            button.setManaged(true);
//
//            button.setDisable(false);
//            button.setStyle("");
//
//            button.setText(originalButtonTexts.get(i));
//        }

        closedSlots.clear();

        if(facility == null){
            return;
        }

        int startIndex = 0;
        int endIndex = 15;

        switch (facility){
            case "Discussion Room":
                startIndex = 1;
                endIndex = 13;
                break;
            case "Computer Lab":
                startIndex = 1;
                endIndex = 14;
                break;
            case "Group Study Room":
                startIndex = 2;
                endIndex = 12;
                break;
            case "Study Pod":
                startIndex = 0;
                endIndex = 15;
                break;
            case "Basketball Court":
                startIndex = 0;
                endIndex = 14;
        }

//        for(int i=0; i<allTimeButtons.size(); i++){
//            if(i < startIndex || i > endIndex){
//                allTimeButtons.get(i).setDisable(true);
//                allTimeButtons.get(i).setText(allTimeButtons.get(i).getText() + "\nCLOSED");
//                allTimeButtons.get(i).setStyle(
//                        "-fx-background-color: #808080;" +
//                                "-fx-text-fill: white;"
//                );
//            }
//        }

        for(int i=0; i<allTimeButtons.size(); i++){
            if(i<startIndex || i>endIndex){
                closedSlots.add(i);
            }
        }
    }

    private void refreshTimeButtons(){
        clearSelectedTimes();
        resetTimeButtons();
        applyFacilityOperatingHours(facilityDropbox.getValue());
        updateBookedSlots();
        renderButtons();
    }

    private void renderButtons(){
        for(int i=0; i<allTimeButtons.size(); i++){
            Button button = allTimeButtons.get(i);

            button.setDisable(false);
            button.setStyle("");

            if(closedSlots.contains(i)){
                button.setDisable(true);
                button.setText(button.getText() + "\nCLOSED");
                button.setStyle("-fx-background-color: #808080; -fx-text-fill: white;");
                continue;
            }

            if(bookedSlots.contains(i)){
                button.setDisable(true);
                button.setText(button.getText() + "\nBOOKED");
                button.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
            }
        }
    }

    private void showAlert(String title, String msg, int cases){
        if(cases == 1){
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);

            alert.showAndWait();
        }else if(cases == 2){
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);

            alert.showAndWait();
        }
    }

    private void resetBookingForm() {
        // Reset facility and room
        facilityDropbox.getSelectionModel().clearSelection();

        roomDropbox.getItems().clear();
        roomDropbox.getSelectionModel().clearSelection();

        facilityDropbox.setPromptText("Choose Facility");
        roomDropbox.setPromptText("Choose Room");

        // Reset date
        datePicker.setValue(LocalDate.now());

        // Reset selected times
        selectedTimeButtons.clear();

        // Reset all time buttons
        for (Button button : allTimeButtons) {
            button.setStyle("");
            button.setDisable(false);
        }

        // Restore initial page state
        facilityNotiLabel.setVisible(true);
        facilityNotiLabel.setManaged(true);

        roomNotiLabel.setVisible(false);
        roomNotiLabel.setManaged(false);

        scrollPane.setVisible(false);
        scrollPane.setManaged(false);

        roomLabel.setVisible(false);
        roomLabel.setManaged(false);

        roomDropbox.setVisible(false);
        roomDropbox.setManaged(false);

        facilityDropbox.setValue(null);
        roomDropbox.setValue(null);

        facilityDropbox.setPromptText("Choose Facility");
        roomDropbox.setPromptText("Choose Room");

        // Reset stored values
        selectedFacility = null;
        selectedDate = null;
        startTime = null;
        endTime = null;
    }

    public static void main(String[] srg){
        Application.launch();
    }

}
