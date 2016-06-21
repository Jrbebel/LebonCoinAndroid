package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import Classe.ShowProgress;
import configuration.Configuration;
import configuration.HttpConnexion;

public class AnnonceDetailActivity extends AppCompatActivity {

    private String idAnnonce;
    private ImageView image;
    private TextView titreAnnonce;
    private TextView descriptif;
    private TextView prix;
    private TextView vendeur;
    private TextView misenligne;
    private LinearLayout linear;
    private ProgressBar progress;
    private ArrayList listeMap; //permettra de faire la corespondance avec l'item de la liste et l'id de l'annonce

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_detail);
        initComposant();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentProvenance = getIntent();
        idAnnonce = intentProvenance.getStringExtra("idAnnonce");

        /**On appele le processbar**/

        ShowProgress.showProgress(true, progress, linear, getResources());

        new TacheAsynchroneAnnonceDetails().execute();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), PrincipaleActivity.class);
        myIntent.putExtra("type", "Offres");
        startActivity(myIntent);
        return true;

    }

    public void initComposant() {

        image = (ImageView) findViewById(R.id.imageAnnonceDetails);
        titreAnnonce = (TextView) findViewById(R.id.textViewTitreAnnonce);
        descriptif = (TextView) findViewById(R.id.textViewDescriptif);
        prix = (TextView) findViewById(R.id.textViewprix);
        vendeur = (TextView) findViewById(R.id.textViewVendeur);
        misenligne = (TextView) findViewById(R.id.textViewMEligne);
        linear = (LinearLayout) findViewById(R.id.linearlaout);
        progress = (ProgressBar) findViewById(R.id.progress);

    }


    /***************************
     * Tache ascynchrone pour recupere les info de l'annonce en ligne ( inspiration entre le support de cours et la tache asynchrone
     *************************************/


    private class TacheAsynchroneAnnonceDetails extends AsyncTask<String, Integer, ArrayList> {

        @Override
        protected ArrayList<String> doInBackground(String... asParametres) {


            ArrayList listAnnonce = new ArrayList();
            listeMap = new ArrayList<String>();


            String lsURL = Configuration.getAnnonceDetails(idAnnonce);
            String cheminImage = Configuration.getEspaceImage();  // Chemin vers l'espace des images
            JSONObject jObj;
            URL urlImage;
            InputStream is;
            try {
                System.out.println("lsurl : " + lsURL);
                jObj = HttpConnexion.getdataJson(lsURL, "GET");
                System.out.println("jOBJ  " + jObj);
                JSONArray array = jObj.getJSONArray("AnnonceDescriptif");
                String imageURL = array.getJSONArray(0).get(0).toString();
                String titreAnnonce = array.getJSONArray(0).get(1).toString();
                String descriptif = array.getJSONArray(0).get(2).toString();
                String prix = array.getJSONArray(0).get(3).toString();
                String imagePath = cheminImage + imageURL;
                String vendeur = array.getJSONArray(0).get(4).toString();
                String misenligne = array.getJSONArray(0).get(5).toString();

                try {
                    urlImage = new URL(imagePath);
                    is = urlImage.openStream();
                    System.out.println(" image : " + imageURL);
                } catch (IOException e1) {
                    is = getResources().openRawResource(R.drawable.notfound);
                    System.out.println("image  non trouvé ERREIR");
                }

                Bitmap bmp = BitmapFactory.decodeStream(is);

                listAnnonce.add(bmp);
                listAnnonce.add(titreAnnonce);
                listAnnonce.add(descriptif);
                listAnnonce.add(prix);
                listAnnonce.add(vendeur);
                listAnnonce.add(misenligne);

                is.close();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("message ioexception " + e.getMessage());
                e.printStackTrace();
            }


            return listAnnonce;

        }

        @Override
        protected void onPostExecute(ArrayList asResultat) {

            image.setImageBitmap((Bitmap) asResultat.get(0));

            titreAnnonce.setText((CharSequence) asResultat.get(1));
            descriptif.setText("Description : \n" + asResultat.get(2));
            prix.setText("Prix : " + asResultat.get(3) + " €");
            vendeur.setText("Vendu par : " + asResultat.get(4));
            misenligne.setText("Mise en ligne le " + asResultat.get(5));
            ShowProgress.showProgress(false, progress, linear, getResources());

        } /// onPostExecute


    }

}