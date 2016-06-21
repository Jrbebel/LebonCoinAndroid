package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

import entities.users;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Mise a jour du design des spinner --material design spinner component-- source :http://www.viralandroid.com/2016/02/android-material-design-spinner-dropdown-example.html
     **/
    private MaterialBetterSpinner spinnerCategoriePro;

    private EditText editTextRegion;
    private EditText editTextEmail;
    private EditText editTextNom;
    private EditText editTextPrenom;
    private EditText editTextAdresse;
    private EditText editTextCodePostal;
    private AutoCompleteTextView editTextVille;
    private EditText editTextTelephone;
    private EditText editTextDateNaissance;
    private EditText editTextPseudo;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
    private CalendarView calendarView;

    private Button buttonEnvoyer;
    private ArrayList listItems;
    private ArrayList listItemsInfo;
    private String idVille;
    private users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        initComponents();
        initListeners();
        modeAffichage();
        this.createSpinnerContent();
    }


    /**
     * Génère et créé le contenu des spinner phase de test
     */
    private void createSpinnerContent() {

        // Mise a jour des données de spinnerCategoriePro
        String[] tCatPro = {"Particulier", "Professionnel"};
        ArrayAdapter<String> aaSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tCatPro);
        aaSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCategoriePro.setAdapter(aaSpinner);


    }


    /**
     * Assignation des widgets
     */
    private void initComponents() {


        users = new users(getApplicationContext());

        spinnerCategoriePro = (MaterialBetterSpinner) findViewById(R.id.spinnerCategoriePro);
        editTextRegion = (EditText) findViewById(R.id.editTextRegion);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNom = (EditText) findViewById(R.id.editTextNom);
        editTextPrenom = (EditText) findViewById(R.id.editTextPrenom);
        editTextAdresse = (EditText) findViewById(R.id.editTextAdresse);
        editTextCodePostal = (EditText) findViewById(R.id.editTextCodePostal);
        editTextVille = (AutoCompleteTextView) findViewById(R.id.editTextVille);
        listItemsInfo = new ArrayList<String>();
        editTextVille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  Toast.makeText(InscriptionActivity.this, "Toast" + listItemsInfo.get(position) + " size : " + listItemsInfo.size(), Toast.LENGTH_LONG).show();
                String[] info = listItemsInfo.get(position).toString().split(";");
                idVille = info[0];
                editTextCodePostal.setText(info[2]);
                editTextRegion.setText(info[3]);
            }
        });
        /******
         afterTextChanged : Appeler aprés que le texte de la zone ciblé soit changé
         beforeTextChanged : Appeler avant que le texte de la zone ciblé soit changé
         onTextChanged : Appeler quand le texte de la zone ciblé est en cours de changement
         *****/

        editTextVille.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tailleTextVille = editTextVille.getText().length();
                //      Toast.makeText(InscriptionActivity.this, "Taille texte" +tailleTextVille, Toast.LENGTH_SHORT).show();
                if (tailleTextVille > 4) { // on commmece a echerche la ville a partir de 4 caractere
                    //   Toast.makeText(InscriptionActivity.this, "Texte change" + before, Toast.LENGTH_SHORT).show();

                    new TacheAsynchroneGetDataRegion().execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextTelephone = (EditText) findViewById(R.id.editTextTelephone);
        editTextDateNaissance = (EditText) findViewById(R.id.editTextDateNaissance);
        editTextPseudo = (EditText) findViewById(R.id.editTextPseudo);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);
        buttonEnvoyer = (Button) findViewById(R.id.buttonEnvoyer);


    }


    /**
     * Permet d'afficher la vue correspondante à l'inscription ou la modification du profil ,
     * par défault c'est que c une inscription
     * si c est une modification on hydrate les champs avec les valeurs des preferences
     **/
    public void modeAffichage() {

        Intent intent = getIntent();
        String modeAffichage = intent.getStringExtra("typeAction");

        users user = new users(getApplicationContext());

        if (modeAffichage.equals("modificationProfil")) {

            editTextEmail.setText(user.getEmail());
            editTextNom.setText(user.getNom());
            editTextPrenom.setText(user.getPrenom());
            editTextAdresse.setText(user.getAdresse());
            editTextCodePostal.setText(user.getCodepostale());
            editTextVille.setText(user.getVille());
            editTextTelephone.setText(user.getTelephone());
            editTextDateNaissance.setText(user.getDatenaissance());
            editTextPseudo.setText(user.getPseudo());
            editTextPassword.setText(user.getMotdepasse());
            spinnerCategoriePro.setText(user.getCategorie());
            editTextRegion.setText(user.getRegion());

            idVille = users.getIdVille();

            buttonEnvoyer.setText("Mise à jour profils");
        }

    }


    @Override
    public void onClick(View v) {
        if (v == buttonEnvoyer) {
            new TacheAsynchroneEnregistrement().execute();
        }
    }

    /**
     * Déclaration des listeners
     */
    private void initListeners() {

        buttonEnvoyer.setOnClickListener(InscriptionActivity.this);

    }


    /*
    * AsyncTask<Params, Progress, Result>
   */
    private class TacheAsynchroneEnregistrement extends AsyncTask<Integer, Integer, Integer> {

        private String getNom = editTextNom.getText().toString();
        private String getPrenom = editTextPrenom.getText().toString();
        private String getTel = editTextTelephone.getText().toString();
        private String getEmail = editTextEmail.getText().toString();
        private String getDatenaiss = editTextDateNaissance.getText().toString();
        private String getAdresse = editTextAdresse.getText().toString();
        private String getPseudo = editTextPseudo.getText().toString();
        private String getPassword = editTextPassword.getText().toString();
        private String getPassConf = editTextPasswordConfirm.getText().toString();
        private String getVille = editTextVille.getText().toString();
        private String getCodePostal = editTextCodePostal.getText().toString();
        private String getRegion = editTextRegion.getText().toString();
        private String getCategorie = spinnerCategoriePro.getText().toString();

        @Override
        // ----------------------------
        protected Integer doInBackground(Integer... aiParametres) {

            // String... parametre : nombre variable d'arguments
            // Se deplace dans un thread d'arriere-plan
            List<String> listTemp = new ArrayList<String>();

            int resultat = 0;
            //url avec les elements a envoyer vers mon serveur
            String lsURL = "http://ns320828.ip-5-39-86.eu:8080/apiLeBonCoin/InscriptionForAndroid" +
                    "?nom=" + getNom + "&prenom=" + getPrenom + "&telephone=" + getTel +
                    "&email=" + getEmail + "&date=" + getDatenaiss + "&adresse=" + getAdresse + "&pseudo=" + getPseudo +
                    "&mdp=" + getPassword + "&mdpconf=" + getPassConf + "&idVille=" +
                    idVille + "&idCategorie=" + "1" + "&idclient=" + users.getIdUser();

            URL urlConnection = null;
            HttpURLConnection httpConnection = null;


            try {
                // Instanciation de HttpURLConnection avec l'objet url
                urlConnection = new URL(lsURL);
                httpConnection = (HttpURLConnection) urlConnection.openConnection();
                StringBuilder lsbResultat = new StringBuilder();

                // Envoi avec la methode get ou post
                httpConnection.setRequestMethod("POST");

                // Autorise l'envoi de donnees

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

                    //logique :on recupere si la requete a ete executer ou pas
                    //tout la logique de verification des champs se feront dans l'application afin d'eviter les requetes et les traitements par le serveur
                    JSONObject jObj = new JSONObject(lsbResultat.toString());

                    if (jObj.get("retour").equals("success")) { //si l'objet renvoie success


                        //si success on crée son fichier de preference, on peut dire que si l'enregistrement est ok ( à exxternalier )
                        //on peut dc recuperer ces information directement dans le formulaire de l'inscirption

                        users.setNom(getNom);
                        users.setPrenom(getPrenom);
                        users.setTelephone(getTel);
                        users.setDatenaissance(getDatenaiss);
                        users.setEmail(getEmail);
                        users.setPseudo(getPseudo);
                        users.setAdresse(getAdresse);
                        users.setMotdepasse(getPassword);
                        users.setVille(getVille);
                        users.setIdVille(idVille);
                        users.setCodepostale(getCodePostal);
                        users.setRegion(getRegion);
                        users.setCategorie(getCategorie);

                        users.commitPref();

                        resultat = 1; //code de retour

                    } else { //code error

                        return -1;
                    }
                } catch (JSONException e) {
                    System.out.println("Message json" + e.getMessage());
                }

                br.close();
                inputStream.close();

            } catch (IOException e) {
                //message
                System.out.println("meessage IOE : " + e.getMessage());
            } finally {
                // Deconnexion
                httpConnection.disconnect();
            }


            // Renvoie la valeur a onPostExecute
            return resultat;
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
        protected void onPostExecute(Integer asResultat) {
            // Synchronisation avec le thread de l'UI
            //si success on redirige l'activity vers la page pricipal
            if (asResultat == 1) { // OK
                Intent intentProvenance = getIntent();
                String modeAffichage = intentProvenance.getStringExtra("typeAction");

                if (modeAffichage.equals("modificationProfil")) {
                    Intent intent = new Intent(InscriptionActivity.this, MonCompteActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(InscriptionActivity.this, PrincipaleActivity.class);
                    startActivity(intent);
                }

            } else { //Toast pour afficher Erreur dans un toast sur l'activity Inscription
                Toast.makeText(InscriptionActivity.this, "Probleme connexion , contactez le support ", Toast.LENGTH_LONG).show();
            }
        } /// onPostExecute

    }

    /*
       * AsyncTask<Params, Progress, Result>
      */
    private class TacheAsynchroneGetDataRegion extends AsyncTask<String, ArrayList<String>, ArrayList<String>> {


        private String VilleCher = editTextVille.getText().toString();

        @Override
        // ----------------------------
        protected ArrayList<String> doInBackground(String... aiParametres) {

            // String... parametre : nombre variable d'arguments
            // Se deplace dans un thread d'arriere-plan
            listItems = new ArrayList<String>();

            int resultat = 0;
            //url avec les elements a envoyer vers mon serveur
            String lsURL = "http://ns320828.ip-5-39-86.eu:8080/apiLeBonCoin/RechercheVilleForAndroid?villeRech=" + VilleCher;

            URL urlConnection = null;
            HttpURLConnection httpConnection = null;


            try {
                // Instanciation de HttpURLConnection avec l'objet url
                urlConnection = new URL(lsURL);
                httpConnection = (HttpURLConnection) urlConnection.openConnection();
                StringBuilder lsbResultat = new StringBuilder();

                // Envoi avec la methode get ou post
                httpConnection.setRequestMethod("GET");
                // Autorise l'envoi de donnees

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

                    if (listItemsInfo.size() > 0) { //permet d effacer la liste a chaque recherche sinon garde en memoire les autre recherche faite  auparavant

                        listItemsInfo.clear();
                    }

                    //logique :on recupere si la requete a ete executer ou pas
                    //tout la logique de verification des champs se feront dans l'application afin d'eviter les requetes et les traitements par le serveur
                    JSONObject jObj = new JSONObject(lsbResultat.toString());

                    if (!jObj.get("Ville").equals("0")) { //si l'objet renvoie success

                        JSONArray array = jObj.getJSONArray("Ville");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {


                            listItems.add(array.getJSONArray(i).get(1).toString());
                            listItemsInfo.add(array.getJSONArray(i).get(0).toString() + ";" + array.getJSONArray(i).get(1).toString() + ";" + array.getJSONArray(i).get(2).toString() + ";" + array.getJSONArray(i).get(3).toString());

                        }

                        resultat = 1; //code de retour

                    } else { //code error

                        listItems.add("0");
                    }
                } catch (JSONException e) {
                    System.out.println("Message json" + e.getMessage());
                }

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
            return listItems;
        } /// doInBackground

        @Override
        // -------------------------
        protected void onPostExecute(ArrayList<String> asResultat) {
            // Synchronisation avec le thread de l'UI

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(InscriptionActivity.this, android.R.layout.simple_list_item_1, asResultat);
            editTextVille.setAdapter(adapter);
        } /// onPostExecute

    }


}
