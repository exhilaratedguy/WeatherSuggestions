package output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import server.ApiCalls;

import javax.swing.*;

public class Interface extends Application {
    private final int N_DAYS_FORECAST = 5;

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

        stage.centerOnScreen();
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

                        //new Scene
                        Scene tempScene = new Scene(new Group(), 310, 350);

                        //root pane
                        StackPane rootPane = new StackPane();
                        rootPane.setPadding(new Insets(10));
                        tempScene.setRoot(rootPane);

                        //BorderPane to be able to set a Label centered at the top of the window
                        BorderPane bPane = new BorderPane();

                        GridPane tempGrid = new GridPane();
                        tempGrid.setPadding(new Insets(10));
                        tempGrid.setVgap(20);
                        tempGrid.setHgap(20);

                        // Title
                        Label title = new Label(cityValue+", "+countryValue);
                        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
                        bPane.setTop(title);
                        BorderPane.setAlignment(title, Pos.CENTER);

                        // Creating and adding labels for the first column in gridpane
                        for(int i=0; i<5; i++)
                        {
                            Label text = new Label();
                            switch(i)
                            {
                                case(0): text.setText("Temperature:"); break;
                                case(1): text.setText("Sensation:"); break;
                                case(2): text.setText("Precipitation:"); break;
                                case(3): text.setText("Humidity:"); break;
                                case(4): text.setText("Wind speed:"); break;
                                default: break;
                            }
                            GridPane.setConstraints(text, 0, i+2);
                            GridPane.setHalignment(text, HPos.RIGHT);
                            tempGrid.getChildren().add(text);
                        }

                        ApiCalls api = new ApiCalls();
                        String json = api.getCurrentConditions(api.getKey(cityValue, countryValue));

                        if(json.isEmpty()) {
                            return;
                        }

                        stage.setScene(tempScene);

                        json = json.substring(1, json.length()-1); //API returns a json node surrounded by []
                        ObjectMapper objMapper = new ObjectMapper();
                        try {
                            JsonNode rootNode = objMapper.readTree(json);

                            for(int i=0; i<5; i++)
                            {
                                JsonNode tempNode = rootNode;
                                Label text = new Label();
                                switch(i)
                                {
                                    case(0): text.setText(tempNode.get("Temperature").get("Metric").get("Value").toString() + " ºC"); break;
                                    case(1): text.setText(tempNode.get("RealFeelTemperature").get("Metric").get("Value").toString() + " ºC"); break;
                                    case(2): text.setText(tempNode.get("PrecipitationSummary").get("Precipitation").get("Metric").get("Value").toString() + " mm"); break;
                                    case(3): text.setText(tempNode.get("RelativeHumidity").toString() + "%"); break;
                                    case(4): text.setText(tempNode.get("Wind").get("Speed").get("Metric").get("Value").toString() + " km/h"); break;
                                    default: break;
                                }
                                GridPane.setConstraints(text, 1, i+2);
                                GridPane.setHalignment(text, HPos.LEFT);
                                tempGrid.getChildren().add(text);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Button 'Back' -> return to main menu
                        Button backBtn = new Button("Back");
                        tempGrid.getChildren().add(backBtn);
                        GridPane.setConstraints(backBtn, 0, 8);
                        GridPane.setHalignment(backBtn, HPos.LEFT);

                        rootPane.getChildren().addAll(bPane, tempGrid);

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
                        } else {
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

                                // new Scene
                                Scene forecastScene = new Scene(new Group(), 1100, 450);
                                stage.setScene(forecastScene);
                                stage.centerOnScreen();

                                // root pane
                                StackPane rootPane = new StackPane();
                                rootPane.setPadding(new Insets(10));
                                forecastScene.setRoot(rootPane);

                                // BorderPane to be able to set a Text centered at the top of the window
                                BorderPane borderPane2 = new BorderPane();

                                // City name
                                Text cityName = new Text(cityValue+", "+countryValue);
                                cityName.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 26));
                                borderPane2.setTop(cityName);
                                BorderPane.setAlignment(cityName, Pos.CENTER);

                                // GridPane for the VBoxes
                                GridPane gridPane = new GridPane();
                                gridPane.setPadding(new Insets(10));
                                gridPane.setVgap(20);
                                //gridPane.setHgap(20);

                                ApiCalls api = new ApiCalls();
                                String json = api.getDaily5DaysForecast(api.getKey(cityValue, countryValue));

                                ObjectMapper objMapper = new ObjectMapper();
                                try{
                                    JsonNode rootNode = objMapper.readTree(json);
                                    rootNode = rootNode.get("DailyForecasts");

                                    json = rootNode.toString();
                                    json = json.substring(1, json.length()-1); //DailyForecasts node is surrounded by []

                                    // split the json node into multiple nodes (easier to read info of the various days)
                                    String[] date = json.split("[{]\"Date\""); // split when ' {"Date" '
                                    // the previous .split() returns an empty string in date[0] so to get rid of that
                                    date = Arrays.copyOfRange(date, 1, date.length);

                                    int n_nodes = date.length;

                                    for(int i=0; i<n_nodes; i++)
                                    {
                                        date[i] = "{\"Date\"" + date[i]; // re-add the ' {"dt" ' that .split() removes so as to use ObjectMapper
                                        if(i != n_nodes-1)                                      // remove the ',' at the end so as to use ObjectMapper
                                            date[i] = date[i].substring(0, date[i].length()-1); // last element does not have a ',' at the end
                                    }

                                    //Adding the dates over each column of the matrix
                                    for(int i=0; i<n_nodes; i++)
                                    {
                                        Label day = new Label();

                                        JsonNode tempNode = objMapper.readTree(date[i]);
                                        tempNode = tempNode.get("Date");

                                        //positions 1 to 11 of the Date value holds the date, the rest of it is other info
                                        day.setText(tempNode.toString().substring(1,11));

                                        day.setFont(Font.font("Verdana", FontWeight.BLACK, 14));
                                        GridPane.setConstraints(day, i+1, 3);
                                        GridPane.setHalignment(day, HPos.CENTER);
                                        gridPane.getChildren().add(day);
                                    }

                                    //Adding the descriptions for each row of the matrix
                                    for(int i=0; i<4; i++)
                                    {
                                        Label text = new Label();
                                        switch(i)
                                        {
                                            case(0): text.setText("Max temp:"); break;
                                            case(1): text.setText("Min temp:"); break;
                                            case(2): text.setText("Daytime:"); break;
                                            case(3): text.setText("Nighttime:"); break;
                                            default: break;
                                        }
                                        text.setFont(Font.font("Verdana", FontWeight.BLACK, 14));
                                        GridPane.setConstraints(text, 0, i+4);
                                        GridPane.setHalignment(text, HPos.RIGHT);
                                        gridPane.getChildren().add(text);
                                    }

                                    //Adding the forecast info to the matrix
                                    for(int col=0; col<n_nodes; col++)
                                    {
                                        //Creating a JsonNode for the current day so as to use the 'switch' below
                                        JsonNode tempNode = objMapper.readTree(date[col]);
                                        for(int row=0; row<4; row++)
                                        {
                                            Label text = new Label();
                                            switch (row)
                                            {
                                                case(0): text.setText(tempNode.get("Temperature").get("Maximum").get("Value")+" ºC"); break;
                                                case(1): text.setText(tempNode.get("Temperature").get("Minimum").get("Value")+" ºC"); break;
                                                case(2): String str = tempNode.get("Day").get("ShortPhrase").toString();
                                                         str = str.substring(1, str.length()-1); // remove the ' " ' at the start and end of string
                                                         text.setText(str);
                                                         break;
                                                case(3): str = tempNode.get("Night").get("ShortPhrase").toString();
                                                         str = str.substring(1, str.length()-1); // remove the ' " ' at the start and end of string
                                                         text.setText(str); break;
                                                default: break;
                                            }
                                            GridPane.setConstraints(text, col+1, row+4);
                                            GridPane.setHalignment(text, HPos.CENTER);
                                            gridPane.getChildren().add(text);
                                        }
                                    }
                                    ColumnConstraints firstCol = new ColumnConstraints(90);
                                    ColumnConstraints otherCols = new ColumnConstraints();

                                    // divide the space equally by the columns with the forecast info (from the 2nd onwards)
                                    otherCols.setPrefWidth( (stage.getWidth()-firstCol.getPrefWidth())/5.0 );

                                    gridPane.getColumnConstraints().addAll(firstCol, otherCols, otherCols, otherCols, otherCols, otherCols);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // Button 'back'
                                Button backBtn = new Button("Back");
                                GridPane.setConstraints(backBtn, 0, 12);
                                gridPane.getChildren().add(backBtn);

                                rootPane.getChildren().addAll( borderPane2, new VBox(gridPane) );

                                backBtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        stage.setScene(loggedInScene);
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }
}