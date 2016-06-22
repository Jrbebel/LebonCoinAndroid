package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import entities.users;

public class MonCompteActivity extends AppCompatActivity {


    private static final int CODE_REQUEST = 200;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private ImageView imageProfils;
    private TextView nom;
    private TextView email;
    private TextView adresse;
    private TextView datenaissance;
    private TextView pseudo;
    private String pathProfils;
    private Bitmap bmimage;
    private Button buttonUpdateProfil;
    private users user;
    private Uri fileUri;

    /***
     * A externaliser (à améliorer )
     ***/
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * type n'est pas utilise pour l'instant
     * permettrait de distinguer photo et video
     *
     * @param type
     * @return
     */
    private static File getOutputMediaFile(int type) {

        // Recuperation ou
        // Creation d'un dossier dans /mnt/sdcard/Pictures/
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MonBonCoin/photo_profil");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MesPhotos", "Impossible de creer le dossier");
                return null;
            }
        }

        // Creation du nom de la photo
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "picture_user";
        // Creation du fichier ... vide
        // mediaStorageDir.getPath() + File.separator renvoie
        // /mnt/sdcard/Pictures/
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + name + ".jpg");

        return mediaFile;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);
        initComposant();
    }

    private void initComposant() {


        pathProfils = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MonBonCoin/photo_profil/IMG_picture_user.jpg"; // le chemin vers mon fichier jpg
        bmimage = BitmapFactory.decodeFile(pathProfils); //Création d'un Bitmap à partir d'un url
        user = new users(getApplicationContext());

        nom = (TextView) findViewById(R.id.textViewNameCompte); // find by id pour textView nom et email
        email = (TextView) findViewById(R.id.textViewEmail);
        adresse = (TextView) findViewById(R.id.textViewAdresse);
        datenaissance = (TextView) findViewById(R.id.textViewBirthday);
        pseudo = (TextView) findViewById(R.id.textViewPseudo);

        imageProfils = (ImageView) findViewById(R.id.imageButtonImageProfils); // Bouton image button ( image cliquable )
        buttonUpdateProfil = (Button) findViewById(R.id.buttonUpdateProfils);
        affichageInformation();

        /**On regarde si l'image existe sinon on peut le donner une image temporaire**/

        if (bmimage != null) {
            imageProfils.setImageBitmap(bmimage);
        } else {
            //une image temporaire generique
            imageProfils.setImageResource(R.drawable.flag);
        }

        imageProfils.setOnClickListener(new View.OnClickListener() { //si on click sur l'image on en prend une autre pour son profil
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MonCompteActivity.this, CameraActivity.class);
                intent.putExtra("camerApplication", "myCompte");
                startActivity(intent);


            }
        });
        buttonUpdateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MonCompteActivity.this, InscriptionActivity.class);
                intent.putExtra("typeAction", "modificationProfil"); //permet de savoir a l'activity inscription que je viens de l'activity inscription
                startActivity(intent);
            }
        });

    }

    /****
     * Affichage des informations de l'utilisateur connecté ,recuperer dans les preferences
     ***/
    private void affichageInformation() {

        nom.setText(user.getNom().toUpperCase() + " " + user.getPrenom().toLowerCase()); //récupère les noms et email du fichier session
        email.setText(user.getEmail().toLowerCase());
        adresse.setText("Vous habitez : " + "\n" + user.getAdresse() + "\n" + user.getCodepostale() + " " + user.getVille() + "\n" + user.getRegion());
        datenaissance.setText("Née le : " + user.getDatenaissance());
        pseudo.setText("Pseudo : " + user.getPseudo());


    }

}
