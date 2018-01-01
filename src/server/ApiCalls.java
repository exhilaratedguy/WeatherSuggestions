package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiCalls {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        ApiCalls http = new ApiCalls();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGetAccu();

        //System.out.println("\nTesting 2 - Send Http POST request");
        //http.sendPost();

    }
    private void sendGetAccu() throws Exception {

        String url = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=GhGJ8BnsA9PlCsWak4ksS51GZNLrV2H2&q=Bucharest&language=en-us&details=false&offset=0&alias=Never";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

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

    // HTTP GET request
    private void sendGet() throws Exception {

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
    private void sendPost() throws Exception {

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