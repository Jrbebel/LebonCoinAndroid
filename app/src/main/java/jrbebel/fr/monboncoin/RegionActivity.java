package jrbebel.fr.monboncoin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RegionActivity extends AppCompatActivity {

    private ArrayList listItems;
    private ListView listeRegion;
    private ArrayAdapter<String> arrayAdapterListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComposant();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initComposant() {
        listeRegion = (ListView) findViewById(R.id.listViewRegion);
        new TacheAsynchrone().execute();
    }


    /// onCreate

/*

* AsyncTask<Params, Progress, Result>

*/

    private class TacheAsynchrone extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override

// ­­­­­­­­­­­­­­­­­­­­­­­­­­­­

        protected ArrayList<String> doInBackground(String... asParametres) {

            // "http://10.57.222.153:80/RessourcesPourAndroid/paysGetCSV.php";

// String... parametre : nombre variable d'arguments

// Se deplace dans un thread d'arriere­plan
            listItems = new ArrayList<String>();
            StringBuilder lsbResultat = new StringBuilder();

// 10.0.2.2 est un alias­host Android pour l'IP locale

//String lsURL = "http://10.0.2.2/RessourcesPourAndroid/paysGetCSV.php";

//String lsURL =

            String lsURL = "http://ns320828.ip-5-39-86.eu:8080/apiLeBonCoin/RegionForAndroid";

            URL urlConnection = null;

            HttpURLConnection httpConnection = null;

            try {

// Instanciation de HttpURLConnection avec l'objet url

                urlConnection = new URL(lsURL);

                httpConnection = (HttpURLConnection) urlConnection.openConnection();

// Envoi avec la methode get ou post

                httpConnection.setRequestMethod("GET");

// Autorise l'envoi de donnees

// Sets the flag indicating whether this URLConnection allows input.

                httpConnection.setDoInput(true);

// Connexion

                httpConnection.connect();

// Le flux de la reponse

                InputStream inputStream = httpConnection.getInputStream();

// Comme l'on recoit un flux Text ASCII

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));


                String lsLigne;


                while ((lsLigne = br.readLine()) != null) {
                    lsbResultat.append(lsLigne);
                }

                // try parse the string to a JSON object
                try {
                    JSONObject jObj = new JSONObject(lsbResultat.toString());

                    JSONArray array = jObj.getJSONArray("REGION");
                    for (int i = 0; i < array.length(); i++) {
                        ///System.out.println(array.getJSONArray(i).get(2));
                        listItems.add(array.getJSONArray(i).get(2));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                br.close();

                inputStream.close();

            } catch (IOException e) {

                listItems.add("messge" + e.getMessage());

            } finally {

// Deconnexion

                httpConnection.disconnect();

            }


// Renvoie la valeur a onPostExecute

            return listItems;

        } /// doInBackground

        @Override

// ­­­­­­­­­­­­­­­­­­­­­­­­­

        protected void onPostExecute(ArrayList<String> asResultat) {

            System.out.println("asresulat" + asResultat);
            arrayAdapterListe = new ArrayAdapter<String>(RegionActivity.this, android.R.layout.simple_list_item_1, asResultat);
            listeRegion.setAdapter(arrayAdapterListe);


        } /// onPostExecute

    } /// TacheAsynchrone


}
