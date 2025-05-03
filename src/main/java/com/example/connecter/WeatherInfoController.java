package com.example.connecter;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



public class WeatherInfoController {

    @FXML
    public VBox weatherCardContainer;
    public FlowPane cardContainer;

    @FXML
    private TextField locationField;

    @FXML private TextField searchField;
    
    @FXML private LineChart<String, Number> weatherChart;

    private final String API_KEY = "657d8525cf4f6b1ffcfd1af08f485494"; // Replace with your OpenWeatherMap API key

    @FXML
    public void initialize() {
        List<String> defaultCities = Arrays.asList("London", "NewYork", "Tokyo","India");
        for (String city : defaultCities) {
            fetchAndDisplayWeather(city);
        }
    }

    @FXML
    private void onSearchClicked() {
        String city = locationField.getText().trim();
        if (!city.isEmpty()) {
            fetchAndDisplayWeather(city);
        }
    }

    private void fetchAndDisplayWeather(String city) {
        new Thread(() -> {
            try {
                String urlStr = "https://api.openweathermap.org/data/2.5/forecast?q=" + city +
                        "&appid=" + API_KEY + "&units=metric";

                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(new InputStreamReader(conn.getInputStream()));
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                JSONObject json = new JSONObject(response);
                String cityName = json.getJSONObject("city").getString("name");

                // Get forecast list
                org.json.JSONArray forecastList = json.getJSONArray("list");
                System.out.println("Data points received for " + cityName + ": " + forecastList.length());

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(cityName);

                for (int i = 0; i < forecastList.length(); i += 8) { // 8 = 1 per day (3-hour intervals)
                    JSONObject obj = forecastList.getJSONObject(i);
                    String date = obj.getString("dt_txt");
                    double temp = obj.getJSONObject("main").getDouble("temp");
                    String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");

                    series.getData().add(new XYChart.Data<>(date, temp));

                    if (i == 0) {
                        WeatherInfo info = new WeatherInfo(cityName, temp, description);
                        javafx.application.Platform.runLater(() -> addCard(info));
                    }
                }

                javafx.application.Platform.runLater(() -> weatherChart.getData().add(series));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void addCard(WeatherInfo info) {
        VBox card = new VBox(5);
        card.getStyleClass().add("weather-card");

        Label cityLabel = new Label(info.getCity());
        cityLabel.setStyle("-fx-font-weight: bold");

        Label tempLabel = new Label(String.format("%.1f °C", info.getTemperature()));
        Label descLabel = new Label(info.getDescription());

        card.getChildren().addAll(cityLabel, tempLabel, descLabel);
        cardContainer.getChildren().add(card);
    }

    private void updateChart(WeatherInfo info) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(info.getCity());
        series.getData().add(new XYChart.Data<>(info.getCity(), info.getTemperature()));

        weatherChart.getData().add(series);
    }
}
