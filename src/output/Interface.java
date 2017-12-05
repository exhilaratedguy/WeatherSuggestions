package output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.MysqlConnect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javax.swing.*;

public class Interface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        StackPane root = new StackPane();

        Scene firstScene = new Scene(root, 310, 350);
        firstScene.setRoot(root);
        stage.setScene(firstScene);
        stage.setTitle("WeatherSuggestions");

        // VBox to have all the buttons easily centered
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);

        // BorderPane to easily center Text on top
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));

        root.getChildren().addAll(bPane, vBox);

        Text wel = new Text("Welcome!");
        wel.setFont(Font.font("Verdana", FontWeight.BLACK, 30));
        bPane.setTop(wel);
        BorderPane.setAlignment(wel, Pos.CENTER);

        stage.show();

        // Button 'guest'
        Button guestBtn = new Button("GUEST");
        guestBtn.setFont(Font.font(Font.getDefault().getName(), FontWeight.NORMAL, 17));
        // Button 'log in'
        Button loginBtn = new Button("LOG IN");
        loginBtn.setFont(Font.font(Font.getDefault().getName(), FontWeight.NORMAL, 17));
        // Button 'register'
        Button regBtn = new Button("REGISTER");
        regBtn.setFont(Font.font(Font.getDefault().getName(), FontWeight.NORMAL, 17));

        vBox.getChildren().addAll(guestBtn, loginBtn, regBtn);

        // ADICIONAR FORECAST DO DIA ???
        guestBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene mainScene = new Scene(new Group(), 310, 350);
                stage.setScene(mainScene);

                GridPane mainGrid = new GridPane();
                mainGrid.setPadding(new Insets(10));
                mainGrid.setVgap(20);
                mainGrid.setHgap(20);
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
                city.setPromptText("City Name");
                GridPane.setConstraints(city, 0, 1);
                mainGrid.getChildren().add(city);
                city.setPrefColumnCount(20);
                city.setPrefHeight(20);

                // Button 'Search'
                Button searchBtn = new Button("Search");
                mainGrid.getChildren().add(searchBtn);
                GridPane.setConstraints(searchBtn, 0, 6);
                GridPane.setHalignment(searchBtn, HPos.RIGHT);

                // Button 'Back'
                Button backBtn = new Button("Back");
                mainGrid.getChildren().add(backBtn);
                GridPane.setConstraints(backBtn, 0, 6);
                GridPane.setHalignment(backBtn, HPos.LEFT);

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

                backBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stage.setScene(firstScene);
                    }
                });
            }
        });

        // REGISTER - DONE
        regBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene regScene = new Scene(new Group(), 310, 350);
                stage.setScene(regScene);

                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10));
                grid.setVgap(20);
                grid.setHgap(20);
                regScene.setRoot(grid);

                // Name
                TextField name = new TextField();
                name.setPromptText("Name");
                GridPane.setConstraints(name, 0, 0);
                name.setPrefColumnCount(20);
                name.setPrefHeight(20);

                // Email
                TextField email = new TextField();
                email.setPromptText("Email");
                GridPane.setConstraints(email, 0, 1);
                email.setPrefColumnCount(20);
                email.setPrefHeight(20);

                // Password
                TextField pw = new TextField();
                pw.setPromptText("Password");
                GridPane.setConstraints(pw, 0, 2);
                pw.setPrefColumnCount(20);
                pw.setPrefHeight(20);

                // Button 'Register'
                Button reggBtn = new Button("Register");
                GridPane.setConstraints(reggBtn, 0, 5);
                GridPane.setHalignment(reggBtn, HPos.RIGHT);

                // Button 'Back'
                Button backBtn = new Button("Back");
                GridPane.setConstraints(backBtn, 0, 5);
                GridPane.setHalignment(backBtn, HPos.LEFT);

                grid.getChildren().addAll(name, email, pw, reggBtn, backBtn);

                backBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stage.setScene(firstScene);
                    }
                });

                reggBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String query = "SELECT * FROM Users WHERE email IS '" + email.getText() + "';";
                        MysqlConnect db = new MysqlConnect();

                        // if this email is already registered
                        if(db.isUser(query))
                        {
                            JOptionPane.showMessageDialog(null, "A user with this email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // if it doesn't already exist, create User and go back to the main menu
                        else
                        {
                            db.insertUser(name.getText(), email.getText(), pw.getText());
                            JOptionPane.showMessageDialog(null, "User registered with success.");
                            stage.setScene(firstScene);
                        }
                    }
                });
            }
        });

        // LOGIN - INCOMPLETE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene logScene = new Scene(new Group(), 310, 350);
                stage.setScene(logScene);

                GridPane logGrid = new GridPane();
                logGrid.setPadding(new Insets(10));
                logGrid.setVgap(20);
                logGrid.setHgap(20);
                logScene.setRoot(logGrid);

                // Email
                TextField email = new TextField();
                email.setPromptText("Email");
                email.setPrefColumnCount(20);
                email.setPrefHeight(20);
                GridPane.setConstraints(email, 0, 0);

                // Password
                TextField pw = new TextField();
                pw.setPromptText("Password");
                pw.setPrefColumnCount(20);
                pw.setPrefHeight(20);
                GridPane.setConstraints(pw, 0, 1);

                // Button 'Log in'
                Button logInBtn = new Button("Log in");
                GridPane.setConstraints(logInBtn, 0, 6);
                GridPane.setHalignment(logInBtn, HPos.RIGHT);

                // Button 'Back'
                Button backBtn = new Button ("Back");
                GridPane.setConstraints(backBtn, 0, 6);
                GridPane.setHalignment(backBtn, HPos.LEFT);

                logGrid.getChildren().addAll(email, pw, logInBtn, backBtn);

                backBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stage.setScene(firstScene);
                    }
                });

                logInBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        MysqlConnect db = new MysqlConnect();
                        String query = "SELECT * FROM Users WHERE email IS '" + email.getText() +"' AND password IS '" + pw.getText() + "';";

                        // if the user does not exist
                        if(!db.isUser(query))
                        {
                            JOptionPane.showMessageDialog(null,"This user does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        } // ELSE BOOLEAN DE LOGGED IN A TRUE ???????????????

                        // root
                        StackPane rootPane = new StackPane();
                        rootPane.setPadding(new Insets(10));

                        // new Scene
                        Scene loggedInScene = new Scene(rootPane, 310, 350);
                        loggedInScene.setRoot(rootPane);
                        stage.setScene(loggedInScene);

                        // BorderPane to be able to set a Text centered at the top of the window
                        BorderPane borderPane = new BorderPane();
                        borderPane.setPadding(new Insets(10));
                        // 'Hello <name>'
                        Text name = new Text("Hello " + db.getName(email.getText()) + "!");
                        name.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
                        borderPane.setTop(name);
                        BorderPane.setAlignment(name, Pos.CENTER);

                        //GridPane
                        GridPane gridPane = new GridPane();
                        gridPane.setPadding(new Insets(10));
                        gridPane.setVgap(20);
                        gridPane.setHgap(20);

                        // Country Name
                        TextField countryField = new TextField();
                        countryField.setPromptText("Country Name");
                        countryField.setPrefColumnCount(20);
                        countryField.setPrefHeight(20);

                        // City Name
                        TextField cityField = new TextField();
                        cityField.setPromptText("City Name");
                        cityField.setPrefHeight(20);
                        cityField.setPrefColumnCount(20);

                        // Button 'Log out'
                        Button logOutBtn = new Button("Log out");
                        GridPane.setHalignment(logOutBtn, HPos.LEFT);

                        // Button 'Search'
                        Button searchBtn = new Button("Search");
                        GridPane.setHalignment(searchBtn, HPos.RIGHT);

                        rootPane.getChildren().addAll(borderPane, gridPane);

                        gridPane.getChildren().addAll(countryField, cityField, logOutBtn, searchBtn);
                        GridPane.setConstraints(countryField, 0, 3);
                        GridPane.setConstraints(cityField, 0, 4);
                        GridPane.setConstraints(logOutBtn, 0, 6);
                        GridPane.setConstraints(searchBtn, 0, 6);

                        logOutBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                stage.setScene(firstScene);
                                // BOOLEAN DE LOGGED IN A FALSE ?????????????
                            }
                        });



                    }
                });
            }
        });
    }
}