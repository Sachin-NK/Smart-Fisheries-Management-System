package com.example.connecter;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class WeatherInfoController {

    @FXML
    public VBox weatherCardContainer;
    public HBox cardContainer;
    public Button closebtn;
    public TextField locationField1;

    @FXML
    private TextField locationField;

    @FXML
    private TextField searchField;

    @FXML
    private LineChart<String, Number> weatherChart;

    //private final String API_KEY = "6a9e1864-2d15-11f0-a953-0242ac130003-6a9e18be-2d15-11f0-a953-0242ac130003"; // Replace with your OpenWeatherMap API key

    @FXML
    public void initialize() {
        List<String> defaultCities = Arrays.asList("London", "NewYork", "Tokyo", "India");
        for (String city : defaultCities) {
            fetchAndDisplayOceanData(5.95, 80.55);
            fetchAndDisplayOceanData(35.6, 139.75);
        }
    }

    @FXML
    private void onSearchClicked() {
        try {
            double lat = Double.parseDouble(locationField.getText().trim());
            double lng = Double.parseDouble(locationField1.getText().trim());

            fetchAndDisplayOceanData(lat, lng);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for latitude or longitude.");
            // Optionally show alert here
        }
    }
}
