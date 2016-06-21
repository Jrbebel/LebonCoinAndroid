package configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jean raynal BEBEL on 19/06/16.
 */
public class HttpConnexion {

    private static StringBuilder lsbResultat;
    private static String lsLigne;
    private static URL urlConnection = null;
    private static HttpURLConnection httpConnection = null;
    private static JSONObject jObj;

    public static JSONObject getdataJson(String url, String methode) throws IOException, JSONException {

        lsbResultat = new StringBuilder();


        urlConnection = new URL(url);
        httpConnection = (HttpURLConnection) urlConnection.openConnection();
        httpConnection.setRequestMethod(methode);
        httpConnection.setDoInput(true);
        httpConnection.connect();
        InputStream inputStream = httpConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        while ((lsLigne = br.readLine()) != null) {
            lsbResultat.append(lsLigne);
        }
        jObj = new JSONObject(lsbResultat.toString());
        br.close();
        inputStream.close();

        return jObj;

    }
}
