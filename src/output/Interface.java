package output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;

public class Interface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        //Group root = new Group();
        Scene mainScene = new Scene(new Group(), 310, 350);
        stage.setScene(mainScene);
        stage.setTitle("Weather Station");

        GridPane mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        mainGrid.setVgap(25);
        mainGrid.setHgap(25);
        mainScene.setRoot(mainGrid);

        // Country Name
        TextField country = new TextField();
        country.setPromptText("Country Name");
        country.setPrefColumnCount(20);
        GridPane.setConstraints(country, 0, 0);
        mainGrid.getChildren().add(country);
        country.setPrefHeight(20);

        // City Name
        TextField city = new TextField();
        GridPane.setConstraints(city, 0, 1);
        mainGrid.getChildren().add(city);
        city.setPromptText("City Name");
        city.setPrefColumnCount(20);
        city.setPrefHeight(20);

        // Button 'Search'
        Button searchBtn = new Button("Search");
        mainGrid.getChildren().add(searchBtn);
        GridPane.setConstraints(searchBtn, 0, 4);
        GridPane.setHalignment(searchBtn, HPos.RIGHT);

        stage.show();

        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String countryValue = country.getText();
                String cityValue = city.getText();

                try {
                    String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + cityValue + ","
                            + countryValue+"&APPID=7a7620706be88fd95da0167b0f625f24";

                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() == 404) { // wrong country/city name -- doesn't find the web page
                        JOptionPane.showMessageDialog(null, "Wrong country/city name", "Error 404", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader( new InputStreamReader((conn.getInputStream())) );

                    Scene tempScene = new Scene(new Group(), 310, 350);
                    stage.setScene(tempScene);

                    GridPane tempGrid = new GridPane();
                    tempGrid.setPadding(new Insets(10, 10, 10, 10));
                    tempGrid.setVgap(25);
                    tempGrid.setHgap(25);
                    tempScene.setRoot(tempGrid);

                    // Info Labels
                    Label title = new Label(cityValue+", "+countryValue);
                    title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
                    title.setWrapText(true); // nao funciona god knows why lul
                    GridPane.setConstraints(title, 0, 0);
                    tempGrid.getChildren().add(title);
                    Label lblTemperature = new Label();

                    Label lblTemp_min = new Label();
                    Label lblTemp_max = new Label();
                    Label lblHumidity = new Label();
                    Label lblPressure = new Label();

                    /*
                    lblHumidity.setPrefHeight(20);
                    lblPressure.setPrefHeight(20);
                    lblTemp_max.setPrefHeight(20);
                    lblTemperature.setPrefHeight(20);
                    lblTemp_min.setPrefHeight(20);
                    */

                    // Temperature
                    GridPane.setConstraints(lblTemperature, 0, 1);
                    GridPane.setConstraints(lblTemp_min, 0, 2);
                    GridPane.setConstraints(lblTemp_max, 0, 3);
                    tempGrid.getChildren().add(lblTemperature);
                    tempGrid.getChildren().add(lblTemp_min);
                    tempGrid.getChildren().add(lblTemp_max);

                    // Humidity
                    GridPane.setConstraints(lblHumidity, 0, 4);
                    tempGrid.getChildren().add(lblHumidity);

                    // Pressure
                    GridPane.setConstraints(lblPressure, 0, 5);
                    tempGrid.getChildren().add(lblPressure);

                    String output;
                    String json = "";
                    while ( (output = br.readLine()) != null ){
                        json += output;
                        System.out.println(json);
                    }

                    conn.disconnect();

                    ObjectMapper objMapper = new ObjectMapper();
                    JsonNode rootNode = objMapper.readTree(json);
                    JsonNode mainNode = rootNode.get("main");
                    Weather weather = objMapper.treeToValue(mainNode, Weather.class);


                    lblTemperature.setText("Temperature: " + weather.getTemp());
                    lblTemp_max.setText("Max temperature: " + weather.getTemp_max());
                    lblTemp_min.setText("Min temperature: " + weather.getTemp_min());
                    lblHumidity.setText("Humidity: " + weather.getHumidity());
                    lblPressure.setText("Pressure: " + weather.getPressure());

                    // Button 'Back' -> return to main menu
                    Button backBtn = new Button("Back");
                    tempGrid.getChildren().add(backBtn);
                    GridPane.setConstraints(backBtn, 0, 7);
                    GridPane.setHalignment(backBtn, HPos.LEFT);

                    backBtn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            stage.setScene(mainScene);
                        }
                    });

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        });
    }
}