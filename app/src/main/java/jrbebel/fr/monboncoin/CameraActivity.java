package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import Classe.PathImage;

public class CameraActivity extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComposantPitcure();
    }

    public void initComposantPitcure() {


        /***Type de la demande ou offre à afficher sur la page principale***/


        // Cree une intention pour la prise de photo
        // et renvoie le controle a l'appelant
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Cree un fichier vide pour sauvegarder la photo
        fileUri = PathImage.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        // Ajoute un extra a l'intention :
        // le fichier ou sera sauvegarder la photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Demarre l'Intention de capture d'image
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    } // / onClick

    /*****
     * Result de l'activity
     ****/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = null;

                Intent intentProvenance = getIntent();

                if (intentProvenance.getStringExtra("camerApplication").equals("myCompte")) {
                    intent = new Intent(this, MonCompteActivity.class);

                }
                startActivity(intent);
                // Image captured and saved to fileUri specified in the Intent
                //initComposant(); //j'appele la methode initComposant pour rafraichir la page apres la photo (a optimiser ou a changer)
                //à prévoir pour envoyer vers le serveur
                Toast.makeText(this, "Photo enregistrer", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Vous avez annnuler la capture de la photo", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "L'image n'a pas pu etre enregistrer", Toast.LENGTH_LONG).show();
            }
        }

    }
}
