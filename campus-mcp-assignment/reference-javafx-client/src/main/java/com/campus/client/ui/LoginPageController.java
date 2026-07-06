package com.campus.client.ui;

import com.campus.client.App;

import java.util.*;
import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.campus.client.mcp.CampusMcpClient;

//unsure if i need these :(
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author User
 */
public class LoginPageController {
    private static CampusMcpClient mcp;
    
    @FXML
    TextField studentIdInput, studentPasswordInput;
    @FXML
    Button loginButton;
    @FXML   
    Label errorLabel;
    
    
    @FXML
    public void loginClick(ActionEvent event) {
        /*  
            1. Validates input
            2. Verify Student login:
                2.1 Connects with the MCP server (request for student.txt)
                2.2 Compiles entries from student.txt into a map or something
                2.3 Looks through student ids and matches with the student id and password
                2.4 Invoke setRoot("dashboard")
            3. Incorrect Login details
                3.1 display "incorrect login details"
                3.2 clear the textfields
                3.3 return to this loginpage
        */
        
        if (verifyLogin()) {
//            App.switchMainScreensOnButtonPress((Button)event.getSource());
        }
        
    }
    
    public void loginFailedScreen(String message) {
        errorLabel.setText(message);
        
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///LOGIC
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public boolean verifyLogin() { //returns true if login was successful
        //Validates input
        //Verifies student login
        //returns true or based on its status
        
        //NOTE: Has the power to invoke error screens
        
        String id = "";
        String password = "";
        
        if (studentIdInput.getText().isEmpty()) {
            //display "missing student id"
            loginFailedScreen("Please input your Taylor's Student ID");
        } else if (studentPasswordInput.getText().isEmpty()) {
            loginFailedScreen("Please input your Student Account Password");
        } else {
            id = studentIdInput.getText().toLowerCase();
            password = studentPasswordInput.getText().toLowerCase();
        }
            
        Map<String, String> studentIdsAndPasswords = new HashMap<>(); //get the MCP server student details, and store them here
        
        String[] studentIds = (String[]) studentIdsAndPasswords.keySet().toArray();
        for (int i = 0; i < studentIdsAndPasswords.size(); i++) {
            if (id.equals(studentIds[i].toLowerCase()) && password.equals(studentIdsAndPasswords.get(studentIds[i]))) {
                return true;
            }
        }
        
        loginFailedScreen("Incorrect Student ID or Password");
        return false;
    }
    
    /**
     * Simply gets the reference of the mcp (after connection during runtime)
     * 
     */
    public static void bind(CampusMcpClient mcp) {
        LoginPageController.mcp = mcp;
    }
    
    
}
