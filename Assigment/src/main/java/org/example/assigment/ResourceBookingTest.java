package org.example.assigment;

import java.util.HashMap;
import java.util.Map;


public class ResourceBookingTest {

    public static void main(String[] args) {
        // Replace with our actual server URL
        String serverUrl = "http://localhost:8080";


        try (CampusMcpClient client = new CampusMcpClient(serverUrl)) {

            System.out.println("Connecting to server...");
            client.connect();
            System.out.println("Connection successful!");

            //  Check what tools the server actually has
            System.out.println("\nAvailable Tools:");
            client.listTools().forEach(tool -> System.out.println("- " + tool.name()));

            // Prepare the data to save in the txt file
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("filename", "test_booking.txt");
            bookingData.put("content", "Room 101 booked for study session.");

            //  Call the server tool to save the file
            // Note: "write_file" is a placeholder. 
            System.out.println("\nAttempting to save booking...");
            String response = client.callTool("write_file", bookingData);

            System.out.println("Server Response: " + response);

        } catch (Exception e) {
            System.err.println("Failed to connect or save data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}