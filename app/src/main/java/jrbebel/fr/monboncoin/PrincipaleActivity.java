package jrbebel.fr.monboncoin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classe.MyViewBinder;
import Classe.ShowProgress;
import configuration.Configuration;
import configuration.HttpConnexion;
import entities.users;

public class PrincipaleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textEmail;
    private TextView textName;
    private ListView liste;
    private users user;
    private ImageView image;
    private Context baseContext;
    private String type;
    private ArrayList listeMap; //permettra de faire la corespondance avec l'item de la liste et l'id de l'annonce
    private String idAnnonce;

    private ProgressBar progress;
    private LinearLayout linearPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        initComposant();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /***Pour ajouter les elements dans la navigation**/

        View header = navigationView.getHeaderView(0);
        // textView se trouvant dans header
        textEmail = (TextView) header.findViewById(R.id.textViewEmailHeader);
        textName = (TextView) header.findViewById(R.id.textViewNameHeader);
        user = new users(getApplicationContext());
        textEmail.setText(user.getEmail());
        textName.setText(user.getNom() + " " + user.getPrenom());


        /***On execute notre thread de liste annonce***/

        ShowProgress.showProgress(true, progress, linearPrincipal, getResources());
        new TacheAsynchrone().execute();
    }

    private void initComposant() {

        liste = (ListView) findViewById(R.id.listViewAnnonce);
        image = (ImageView) findViewById(R.id.imageViewAnnonce);
        baseContext = getBaseContext();

        progress = (ProgressBar) findViewById(R.id.progressprincipale);
        linearPrincipal = (LinearLayout) findViewById(R.id.linearprincipale);

        /**On recupere les informations de la liste appuyer**/

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idAnnonce = listeMap.get(position).toString();
                Intent intent = new Intent(PrincipaleActivity.this, AnnonceDetailActivity.class);
                intent.putExtra("idAnnonce", idAnnonce);
                startActivity(intent);
                finish();
            }
        });

        /***Type de la demande ou offre à afficher sur la page principale***/
        Intent intentProvenance = getIntent();
        type = intentProvenance.getStringExtra("type");
        // Toast.makeText(PrincipaleActivity.this, "Page des " + type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {


            drawer.closeDrawer(GravityCompat.START);
        } else {


            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principale, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_offre) {

            Intent intent = new Intent(this, PrincipaleActivity.class);
            intent.putExtra("type", "Offres");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_demande) {

            Intent intent = new Intent(this, PrincipaleActivity.class);
            intent.putExtra("type", "Demande");
            startActivity(intent);
            finish();

        } else if (id == R.id.listregion) {

            Intent intent = new Intent(this, RegionActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.mycompte) {

            Intent intent = new Intent(this, MonCompteActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.myannonce) {

        } else if (id == R.id.listcategorie) {

            Intent intent = new Intent(this, CategorieActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.deconnexion) {

            //si il appuie on supprime la session MyPREFERENCES
            user.logout();

            //et on retourne sur la page de login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.dspannonce) {

            Intent intent = new Intent(this, DeposeAnnonceActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /***************************
     * Tache ascynchrone pour recupere les info en ligne ( inspiration entre le support de cours et la tache asynchrone
     *************************************/


    private class TacheAsynchrone extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... asParametres) {


            listeMap = new ArrayList<String>();
            ArrayList<Map<String, Object>> listAnnonce = new ArrayList<Map<String, Object>>();
            String lsURL = Configuration.getAnnonce(type);
            URL urlImage = null;
            String cheminImage = Configuration.getEspaceImage();  // Chemin vers l'espace des images

            try {
                JSONObject jObj = HttpConnexion.getdataJson(lsURL, "GET");

                InputStream is;
                Bitmap bmp = null;
                JSONArray array = jObj.getJSONArray("listeAnnonce");
                int size = array.length();
                Map<String, Object> hm;
                for (int i = 0; i < size; i++) {
                    try {
                        urlImage = new URL(cheminImage + array.getJSONArray(i).get(1).toString());
                        is = urlImage.openStream();


                    } catch (IOException e1) {
                        is = getResources().openRawResource(R.drawable.notfound);

                    }
                    bmp = BitmapFactory.decodeStream(is);
                    // ca plante si l'image n'existe pas malgre le try/catch
                    // c'est pour cela que la requete HEAD est testee avant

                    // --- Chargement et decodage

                    hm = new HashMap<String, Object>();
                    hm.put("image", bmp);
                    hm.put("nom", array.getJSONArray(i).get(2).toString());
                    hm.put("prix", array.getJSONArray(i).get(3).toString() + " €");

                    listAnnonce.add(hm);
                    listeMap.add(i, array.getJSONArray(i).get(0).toString()); // position dans la liste et id de l'annonce
                    is.close();
                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return listAnnonce;

        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, Object>> asResultat) {
            this.remplissageAnnonce(asResultat);
            ShowProgress.showProgress(false, progress, linearPrincipal, getResources());
        } /// onPostExecute

        /******************************
         * Mon code pour afficher la liste des annonces
         ******************************************/

        private void remplissageAnnonce(List listeAnnonce) {
            try

            {


                SimpleAdapter sa = new SimpleAdapter(
                        baseContext,
                        listeAnnonce,
                        R.layout.affichageitem_annonce,
                        new String[]{"image", "nom", "prix"},
                        new int[]{R.id.imageViewAnnonce, R.id.textViewAnnonce, R.id.textViewAnnonce1}
                );
                sa.setViewBinder(new MyViewBinder());
                liste.setAdapter(sa);

            } catch (Exception e)

            {
                Toast.makeText(getBaseContext(), "Erreur ? " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }


}




