package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import configuration.HttpConnexion;
import entities.users;

public class DeposeAnnonceActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Spinner categorie;
    private ArrayAdapter<String> arrayAdapterListe;
    private Button enregistrer;
    private Button buttonPhoto;
    private RadioGroup categorieRadio;
    private RadioGroup radioTypeAnnonce;
    private EditText titre;
    private EditText descriptif;
    private EditText prix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depose_annonce);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComposant();

    }

    public void initComposant() {

        categorie = (Spinner) findViewById(R.id.spinnerCategorie);
        categorieRadio = (RadioGroup) findViewById(R.id.radioGroupStatut);
        radioTypeAnnonce = (RadioGroup) findViewById(R.id.radioGroupTypeAnnonce);
        titre = (EditText) findViewById(R.id.editTextTitre);
        descriptif = (EditText) findViewById(R.id.editTextDescriptif);
        prix = (EditText) findViewById(R.id.editTextPrix);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        titre.setText("Une chaise en or a vendre de dingue");
        descriptif.setText("je vend un truc assez sympa qui fait que je le vend a vous ");
        prix.setText("10100");

        enregistrer = (Button) findViewById(R.id.buttonValider);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TacheAsynchroneEnregistrement().execute();
            }
        });
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeposeAnnonceActivity.this, CameraActivity.class);
                intent.putExtra("type", "deposeAnnonce");
                startActivity(intent);
            }
        });
        new TacheAsynchroneGetData().execute();
    }

    /*****
     * Result de l'activity
     ****/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            System.out.println("resultCode1");

            if (resultCode == RESULT_OK) {
                System.out.println("resultCode");
                // Image captured and saved to fileUri specified in the Intent
                //   initComposant(); //j'appele la methode initComposant pour rafraichir la page apres la photo (a optimiser ou a changer)
                //à prévoir pour envoyer vers le serveur
                Toast.makeText(this, "Photo enregistrer", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Vous avez annnuler la capture de la photo", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "L'image n'a pas pu etre enregistrer", Toast.LENGTH_LONG).show();
            }
        }

    }

    /*
  * AsyncTask<Params, Progress, Result>
 */
    private class TacheAsynchroneGetData extends AsyncTask<Integer, Integer, List<String>> {

        private List itemGrise;

        @Override
        // ----------------------------
        protected List<String> doInBackground(Integer... aiParametres) {

            // String... parametre : nombre variable d'arguments
            // Se deplace dans un thread d'arriere-plan
            List<String> listTemp = new ArrayList<String>();
            itemGrise = new ArrayList();
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
            arrayAdapterListe = new ArrayAdapter<String>(DeposeAnnonceActivity.this, android.R.layout.simple_list_item_1, asResultat) {

            };
            categorie.setAdapter(arrayAdapterListe);


        } /// onPostExecute

    } /// TacheAsynchrone

    /*
   * AsyncTask<Params, Progress, Result>
  */
    private class TacheAsynchroneEnregistrement extends AsyncTask<Integer, Integer, Integer> {

        users user = new users(getApplicationContext());
        private String categorieurl = categorie.toString();
        private String categorieRadio;
        private String radioTypeAnnonceurl = radioTypeAnnonce.toString();
        private String titreurl = titre.getText().toString();
        private String descriptifurl = descriptif.getText().toString();
        private String prixurl = prix.getText().toString().toString();

        @Override
        // ----------------------------
        protected Integer doInBackground(Integer... aiParametres) {

            // String... parametre : nombre variable d'arguments
            // Se deplace dans un thread d'arriere-plan
            List<String> listTemp = new ArrayList<String>();

            int resultat = 0;

            //url avec les elements a envoyer vers mon serveur
            String lsURL = null;
            try {
                lsURL = configuration.Configuration.setAnnonce(titreurl, prixurl, "1", categorieurl, descriptifurl, user.getIdUser());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jObj = HttpConnexion.getdataJson(lsURL, "GET");
                //logique :on recupere si la requete a ete executer ou pas
                //tout la logique de verification des champs se feront dans l'application afin d'eviter les requetes et les traitements par le serveur


                if (jObj.get("retour").equals("success")) { //si l'objet renvoie success


                    resultat = 1; //code de retour

                } else { //code error

                    return -1;
                }
            } catch (JSONException e) {
                System.out.println("Message json" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Renvoie la valeur a onPostExecute
            return resultat;
        } /// doInBackground


        @Override
        // -------------------------
        protected void onPostExecute(Integer asResultat) {
            // Synchronisation avec le thread de l'UI
            //si success on redirige l'activity vers la page pricipal
            if (asResultat == 1) { // OK

                /***Type de la demande ou offre à afficher sur la page principale***/
                Intent intent = new Intent(DeposeAnnonceActivity.this, PrincipaleActivity.class);
                intent.putExtra("type", "Offres");
                startActivity(intent);
                finish();
                Toast.makeText(DeposeAnnonceActivity.this, "Votre Annonce est enregistré", Toast.LENGTH_SHORT).show();

            } else { //Toast pour afficher Erreur dans un toast sur l'activity Inscription
                Toast.makeText(DeposeAnnonceActivity.this, "Probleme connexion , contactez le support ", Toast.LENGTH_LONG).show();
            }
        } /// onPostExecute

    }
}
