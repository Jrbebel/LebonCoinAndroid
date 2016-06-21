package jrbebel.fr.monboncoin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

public class CategorieActivity extends AppCompatActivity {

    private ListView listeCategorie;
    private ArrayAdapter<String> arrayAdapterListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        initComposant();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
      /*  *//**Ajout de la barre de retour**//*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

*/
        listeCategorie = (ListView) findViewById(R.id.listViewCategorie);
        new TacheAsynchroneGetData().execute();
    }


    /*
  * AsyncTask<Params, Progress, Result>
 */
    private class TacheAsynchroneGetData extends AsyncTask<Integer, Integer, List<String>> {

        @Override
        // ----------------------------
        protected List<String> doInBackground(Integer... aiParametres) {

            // String... parametre : nombre variable d'arguments
            // Se deplace dans un thread d'arriere-plan
            List<String> listTemp = new ArrayList<String>();
            String lsURL = "http://ns320828.ip-5-39-86.eu:8080/apiLeBonCoin/CategorieSSCatForAndroid";

            URL urlConnection = null;
            HttpURLConnection httpConnection = null;

            //   double liTotal = aiParametres[0];
            // double liProgression = 0;
            // double liRapport;

            try {
                // Instanciation de HttpURLConnection avec l'objet url
                urlConnection = new URL(lsURL);
                httpConnection = (HttpURLConnection) urlConnection.openConnection();
                StringBuilder lsbResultat = new StringBuilder();

                // Envoi avec la methode get ou post
                httpConnection.setRequestMethod("GET");
                System.out.println("Activity categorie");
                // Autorise l'envoi de donnees
                // Sets the flag indicating whether this URLConnection allows input.
//                httpConnection.setDoInput(true);

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


                try {
                    // System.out.println("String"+lsbResultat);
                    JSONObject jObj = new JSONObject(lsbResultat.toString());

                    System.out.println("LES TITRES TOTAUX:" + jObj.names());
                    for (int i = 0; i < jObj.length(); i++) {

                        System.out.println("LES TITRES :" + jObj.names().get(i));

                        listTemp.add(jObj.names().get(i).toString().toUpperCase());

                        JSONArray array = jObj.getJSONArray(jObj.names().get(i).toString());
                        System.out.println("size array" + array.length());

                        for (int j = 0; j < array.length(); j++) {

                            listTemp.add(array.getJSONArray(j).get(2).toString().toLowerCase());

                        }

                    }


                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Message erreur" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Message json" + e.getMessage());
                }
             /*   liProgression++;
                    liRapport = (liProgression / liTotal) * 100;
                    // Sans l'appel a cette methode l'UI n'est pas maj
                    publishProgress((int) liRapport);*/

                br.close();
                inputStream.close();

            } catch (IOException e) {
                //message
                System.out.println("meessage IOE" + e.getMessage());
            } finally {
                // Deconnexion
                httpConnection.disconnect();
            }


            // Renvoie la valeur a onPostExecute
            return listTemp;
        } /// doInBackground

        @Override
        // ----------------------------
        protected void onProgressUpdate(Integer... aiProgressions) {
            // Synchronisation avec le thread de l'UI
            // MAJ de la barre de progression
            //  progressBarSQL.setProgress(aiProgressions[0]);
            // textViewProgressionPourcentage.setText(Integer.toString(aiProgressions[0]) + " %");
        } // / onProgressUpdate

        @Override
        // -------------------------
        protected void onPostExecute(List<String> asResultat) {
            // Synchronisation avec le thread de l'UI
            // Affiche le resultat fina
            arrayAdapterListe = new ArrayAdapter<String>(CategorieActivity.this, android.R.layout.simple_list_item_1, asResultat);
            listeCategorie.setAdapter(arrayAdapterListe);


        } /// onPostExecute

    } /// TacheAsynchrone
}
