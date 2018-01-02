package output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Stack;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import server.ApiCalls;

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

                        Scene tempScene = new Scene(new Group(), 310, 350);
                        stage.setScene(tempScene);

                        GridPane tempGrid = new GridPane();
                        tempGrid.setPadding(new Insets(10));
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
                        Label lblRealFeel = new Label();
                        Label lblPrecipitation= new Label();
                        Label lblHumidity = new Label();
                        Label lblWindSpeed = new Label();

                        // Temperature
                        GridPane.setConstraints(lblTemperature, 0, 1);
                        GridPane.setConstraints(lblRealFeel, 0, 2);
                        tempGrid.getChildren().add(lblTemperature);
                        tempGrid.getChildren().add(lblRealFeel);

                        // Precipitation
                        GridPane.setConstraints(lblPrecipitation, 0, 3);
                        tempGrid.getChildren().add(lblPrecipitation);

                        // Humidity
                        GridPane.setConstraints(lblHumidity, 0, 4);
                        tempGrid.getChildren().add(lblHumidity);

                        // Pressure
                        GridPane.setConstraints(lblWindSpeed, 0, 5);
                        tempGrid.getChildren().add(lblWindSpeed);

                        ApiCalls api = new ApiCalls();
                        String json = api.getCurrentConditions(api.getKey(cityValue, countryValue));
                        json = json.substring(1, json.length()-1);

                        ObjectMapper objMapper = new ObjectMapper();
                        try {
                            JsonNode rootNode = objMapper.readTree(json);

                            JsonNode tempNode = rootNode.get("Temperature").get("Metric").get("Value");
                            lblTemperature.setText("Temperature: " + tempNode.toString() + " ºC");

                            tempNode = rootNode.get("RealFeelTemperature").get("Metric").get("Value");
                            lblRealFeel.setText("Sensation: " + tempNode.toString() + " ºC");

                            tempNode = rootNode.get("RelativeHumidity");
                            lblHumidity.setText("Humidity: " + tempNode.toString());

                            tempNode = rootNode.get("Wind").get("Speed").get("Metric");
                            lblWindSpeed.setText("Wind speed: " + tempNode.toString() + " km/h");

                            tempNode = rootNode.get("PrecipitationSummary").get("Precipitation").get("Metric").get("Value");
                            lblPrecipitation.setText("Precipitation: " + tempNode.toString() + " mm");




                            //Weather weather = objMapper.treeToValue(mainNode, Weather.class);

                            /*
                            lblTemperature.setText("Temperature: " + weather.getTemp());
                            lblTemp_max.setText("Max temperature: " + weather.getTemp_max());
                            lblTemp_min.setText("Min temperature: " + weather.getTemp_min());
                            lblHumidity.setText("Humidity: " + weather.getHumidity());
                            lblPressure.setText("Pressure: " + weather.getPressure());
                            */
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
                GridPane.setConstraints(logInBtn, 0, 8);
                GridPane.setHalignment(logInBtn, HPos.RIGHT);

                // Button 'Back'
                Button backBtn = new Button ("Back");
                GridPane.setConstraints(backBtn, 0, 8);
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
                        String query = "SELECT * FROM Users WHERE email IS '" + email.getText() +"';";

                        // if the user does not exist
                        if(!db.isUser(query))
                        {
                            JOptionPane.showMessageDialog(null,"This user does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        } // ELSE BOOLEAN DE LOGGED IN A TRUE ???????????????
                        else {
                            query = "SELECT * FROM Users WHERE email IS '" + email.getText() + "' AND password IS '" + pw.getText() + "';";
                            // if the email/pw combo is wrong
                            if(!db.isUser(query))
                            {
                                JOptionPane.showMessageDialog(null,"Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        // root
                        StackPane rootPane = new StackPane();
                        rootPane.setPadding(new Insets(10));

                        // new Scene
                        Scene loggedInScene = new Scene(rootPane, 310, 350);
                        loggedInScene.setRoot(rootPane);
                        stage.setScene(loggedInScene);

                        // BorderPane to be able to set a Text centered at the top of the window
                        BorderPane borderPane = new BorderPane();

                        // 'Hello <name>'
                        Text name = new Text("Hello " + db.getName(email.getText()) + "!");
                        name.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 25));
                        borderPane.setTop(name);
                        BorderPane.setAlignment(name, Pos.CENTER);

                        //GridPane
                        GridPane gridPane = new GridPane();
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
                        GridPane.setConstraints(logOutBtn, 0, 8);
                        GridPane.setConstraints(searchBtn, 0, 8);

                        logOutBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                stage.setScene(firstScene);
                                // BOOLEAN DE LOGGED IN A FALSE ?????????????
                            }
                        });

                        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                String countryValue = countryField.getText();
                                String cityValue = cityField.getText();

                                try{
                                    String urlString = "http://api.openweathermap.org/data/2.5/forecast?q="
                                            + cityValue + "," + countryValue + "&APPID=7a7620706be88fd95da0167b0f625f24";

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

                                    // new Scene
                                    Scene forecastScene = new Scene(new Group(), 575, 450);
                                    stage.setScene(forecastScene);

                                    // root pane
                                    StackPane rootPane = new StackPane();
                                    rootPane.setPadding(new Insets(10));
                                    forecastScene.setRoot(rootPane);

                                    // BorderPane to be able to set a Text centered at the top of the window
                                    BorderPane borderPane2 = new BorderPane();

                                    // City name
                                    Text cityName = new Text(cityValue+", "+countryValue);
                                    cityName.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
                                    borderPane2.setTop(cityName);
                                    BorderPane.setAlignment(cityName, Pos.CENTER);

                                    // GridPane for the VBoxes
                                    GridPane gridPane = new GridPane();
                                    gridPane.setPadding(new Insets(10));
                                    gridPane.setVgap(20);
                                    gridPane.setHgap(20);

                                    String output;
                                    String json = "";
                                    while ( (output = br.readLine()) != null){
                                        json += output;
                                        //System.out.println(json);
                                    }
                                    conn.disconnect();

                                    // split the json node into multiple nodes (for the multiple times)
                                    String[] info = json.split("[{]\"dt\""); // split when ' {"dt" '
                                    final int N_NODES = info.length;
                                    System.out.println(N_NODES+"\n\n\n");

                                    Label[] times = new Label[N_NODES-1];
                                    ObjectMapper objMapper = new ObjectMapper();
                                    JsonNode[] dateNodes = new JsonNode[N_NODES-1];
                                    JsonNode[] mainNodes = new JsonNode[N_NODES-1];
                                    Weather[] weathers = new Weather[N_NODES-1];
                                    String[] hours = new String[N_NODES-1];

                                    for(int i=0; i<N_NODES; i++)
                                    {
                                        if( i>0 ) // first node is the info about the response and city code, etc
                                        {
                                            int j = i-1;
                                            info[i] = "{\"dt\"" + info[i]; // re-add the ' {"dt" ' that .split() removes so as to then use ObjectMapper
                                            info[i] = info[i].substring(0, info[i].length()-1); // remove the ',' at the end so as to use ObjectMapper
                                            dateNodes[j] = objMapper.readTree(info[i]);
                                            mainNodes[j] = dateNodes[j].get("main");
                                            weathers[j] = objMapper.treeToValue(mainNodes[j], Weather.class);
                                            hours[j] = dateNodes[j].get("dt_txt").toString();
                                            // posiçoes 12 e 13 - hora
                                            //System.out.println(hours[j]);
                                        }
                                        //System.out.println(i + "-\t" + info[i]);
                                    }

                                    // create matrix with possible hours
                                    for(int i=0; i<=7; i++)
                                    {
                                        Text hour = new Text();
                                        switch(i)
                                        {
                                            case 0: hour.setText("00h"); break;
                                            case 1: hour.setText("03h"); break;
                                            case 2: hour.setText("06h"); break;
                                            case 3: hour.setText("09h"); break;
                                            case 4: hour.setText("12h"); break;
                                            case 5: hour.setText("15h"); break;
                                            case 6: hour.setText("18h"); break;
                                            case 7: hour.setText("21h"); break;
                                            default: break;
                                        }
                                        hour.setFont(Font.font("Verdana",FontWeight.BLACK, 14));
                                        GridPane.setConstraints(hour, i+1, 3);
                                        GridPane.setHalignment(hour, HPos.CENTER);
                                        gridPane.getChildren().add(hour);
                                    }

                                    for(int i=0; i<=5; i++)
                                    {
                                        Text day = new Text();
                                        switch(i)
                                        {
                                            case(0): day.setText(hours[0].substring(1,11)); break;
                                            case(1): day.setText(hours[8].substring(1,11)); break;
                                            case(2): day.setText(hours[16].substring(1,11)); break;
                                            case(3): day.setText(hours[24].substring(1,11)); break;
                                            case(4): day.setText(hours[32].substring(1,11)); break;
                                            case(5): day.setText(hours[39].substring(1,11)); break;
                                            default: break;
                                        }
                                        day.setFont(Font.font("Verdana", FontWeight.BLACK, 14));
                                        GridPane.setConstraints(day, 0, i+4);
                                        GridPane.setHalignment(day, HPos.CENTER);
                                        gridPane.getChildren().add(day);
                                    }

                                    DecimalFormat fmt = new DecimalFormat("0.##");
                                    int row = 0;
                                    for(int i=0; i<N_NODES-1;i++)
                                    {
                                        // positions 12 and 13 have the hours of the current node
                                        System.out.println(Integer.parseInt(hours[i].substring(12,14)));
                                        int col = Integer.parseInt(hours[i].substring(12, 14)) / 3;
                                        Label label = new Label(
                                                fmt.format( weathers[i].getTemp() ).replace(',','.')
                                        );
                                        gridPane.getChildren().add(label);
                                        GridPane.setConstraints(label, col+1, row+4);

                                        // max 'col' is 7 because max Hour from node is 21
                                        if( col==7 )
                                            row++;
                                    }

                                    // Button 'back'
                                    Button backBtn = new Button("Back");
                                    GridPane.setConstraints(backBtn, 0, 12);
                                    gridPane.getChildren().add(backBtn);

                                    rootPane.getChildren().addAll(borderPane2, gridPane);

                                    backBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            stage.setScene(loggedInScene);
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
                });
            }
        });
    }
}