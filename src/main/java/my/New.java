package my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class New {
    private static HttpURLConnection 	httpCon = null;
    private static URL url;

    private static final String hostURL = "http://donationweb-4-0.herokuapp.com";
    private static final String LocalhostURL = "http://192.168.30.107:30000";


    public static void setup(String request) {
        try {
            url = new URL(LocalhostURL + request);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setUseCaches(false);
            httpCon.setReadTimeout(15 * 1000); // 15 seconds to timeout
            httpCon.setRequestProperty( "Content-Type", "application/json" );
            httpCon.setRequestProperty("Accept", "application/json");
            System.out.println("Connect Succsess!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String url = "/donations";

        BufferedReader reader = null;
        StringBuilder stringBuilder = null;
        try {
            setup(url);
            httpCon.setRequestMethod("GET");
            httpCon.setDoInput(true);
            httpCon.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);


            reader.close();
            System.out.println(stringBuilder.toString());
//            System.out.println(httpCon.getResponseMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
