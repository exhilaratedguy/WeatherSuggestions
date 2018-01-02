package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;

public class ApiCalls {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String API_KEY = "GhGJ8BnsA9PlCsWak4ksS51GZNLrV2H2";

    public static void main(String[] args) throws Exception {

        ApiCalls http = new ApiCalls();

        http.getCurrentConditions(http.getKey("Bucharest", "Romania"));

        //System.out.println("Testing 1 - Send Http GET request");
        //http.getKey("Bucharest", "Romania");

        //System.out.println("\nTesting 2 - Send Http POST request");
        //http.sendPost();

    }

    public void sendGetAccu() throws Exception {

        String url = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=GhGJ8BnsA9PlCsWak4ksS51GZNLrV2H2&q=Bucharest&language=en-us&details=false&offset=0&alias=Never";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        //con.setRequestMethod("GET");

        // add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while( (inputLine = in.readLine()) != null ){
            response.append(inputLine);
        }
        in.close();

        System.out.println("\n"+response.toString());

    }

    public String getKey(String city, String country) throws Exception {
        String str = "";
        try {
            String urlString = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY;
            urlString += "&q=" + city + "%2C%20" + country;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == 404) { // wrong country/city name -- doesn't find the web page
                JOptionPane.showMessageDialog(null, "Wrong country/city name", "Error 404", JOptionPane.ERROR_MESSAGE);
                return str;
            } else if (conn.getResponseCode() != 200) { //if response is not successful
                throw new RuntimeException("Failed! HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ( (output = br.readLine()) != null && !output.equals("[]") ){
                str += output;
                System.out.println(str);
            }
            conn.disconnect();

            if( !str.isEmpty() ) {
                str = str.substring(21, 27);
                System.out.println(str);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    public String getCurrentConditions(String key) throws Exception{
        String json = "";

        try {
            String urlString = "http://dataservice.accuweather.com/currentconditions/v1/" + key + "?apikey=";
            urlString += API_KEY + "&details=true";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == 404) { // wrong country/city name -- doesn't find the web page
                JOptionPane.showMessageDialog(null, "Wrong country/city name", "Error 404", JOptionPane.ERROR_MESSAGE);
                return json;
            } else if (conn.getResponseCode() != 200) { //if response is not successful
                throw new RuntimeException("Failed! HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ( (output = br.readLine()) != null && !output.equals("[]") ){
                json += output;
                System.out.println(json);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "https://api.openweathermap.org/data/2.5/forecast?q=bucharest,romania&APPID=7a7620706be88fd95da0167b0f625f24";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ) {
            response.append(inputLine);
        }
        in.close();

        /*
        String str = response.toString();
        String[] info = str.split("[{]\"dt\""); // split when ' {"dt" '

        //print result
        for(int i=0; i<info.length; i++){
            if(i!=0) {
                info[i] = "{\"dt\"" + info[i];
                info[i] = info[i].substring(0, info[i].length()-1); //tirar a virgula no final para poder usar o ObjectMapper
            }
            System.out.println(i + "-\t" + info[i]);
        }
        */
    }

    // HTTP POST request
    public void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ){
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}