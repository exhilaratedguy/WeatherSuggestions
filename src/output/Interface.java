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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Interface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Group root = new Group();
        Scene scene = new Scene(root, 310, 350);
        stage.setScene(scene);
        stage.setTitle("Weather Station");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(25);
        grid.setHgap(25);
        scene.setRoot(grid);

        // Country Name
        TextField country = new TextField();
        country.setPromptText("Country Name");
        country.setPrefColumnCount(20);
        GridPane.setConstraints(country, 0, 0);
        grid.getChildren().add(country);
        country.setPrefHeight(20);
        // City Name
        TextField city = new TextField();
        GridPane.setConstraints(city, 0, 1);
        grid.getChildren().add(city);
        city.setPromptText("City Name");
        city.setPrefColumnCount(20);
        city.setPrefHeight(20);
        // Button Part
        Button btn = new Button();
        grid.getChildren().add(btn);
        btn.setText("Search");
        GridPane.setConstraints(btn, 0, 4);
        GridPane.setHalignment(btn, HPos.RIGHT);

        stage.show();

        btn.setOnAction(new EventHandler<ActionEvent>() {
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

                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader( new InputStreamReader((conn.getInputStream())) );

                    grid.getChildren().clear();

                    // Temperature
                    Label lblTemperature = new Label();
                    Label lblHumidity = new Label();
                    Label lblPressure = new Label();
                    GridPane.setConstraints(lblTemperature, 0, 0);
                    grid.getChildren().add(lblTemperature);

                    // Humidity
                    GridPane.setConstraints(lblHumidity, 0, 1);
                    grid.getChildren().add(lblHumidity);

                    // Pressure
                    GridPane.setConstraints(lblPressure, 0, 2);
                    grid.getChildren().add(lblPressure);

                    String output;
                    String json = "";
                    while ( (output = br.readLine()) != null ){
                        json += output;
                        //System.out.println(json);
                    }

                    conn.disconnect();

                    ObjectMapper objMapper = new ObjectMapper();
                    JsonNode rootNode = objMapper.readTree(json);
                    JsonNode mainNode = rootNode.get("main");
                    Weather weather = objMapper.treeToValue(mainNode, Weather.class);

                    lblTemperature.setText("Temperature:" + weather.getTemp());
                    lblHumidity.setText("Humidity:" + weather.getHumidity());
                    lblPressure.setText("Pressure:" + weather.getPressure());
                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        });
    }
}