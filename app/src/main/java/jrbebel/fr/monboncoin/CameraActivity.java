package jrbebel.fr.monboncoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Button buttonPrendreUnePhoto;
    private Uri fileUri;

    /**
     * type n'est pas utilise pour l'instant
     * permettrait de distinguer photo et video
     *
     * @param type
     * @return
     */
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
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MonBonCoin/photo_profil/");

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
    } // / getOutputMediaFile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onClick();
    }

    public void onClick() {

        // Cree une intention pour la prise de photo
        // et renvoie le controle a l'appelant
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Cree un fichier vide pour sauvegarder la photo
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        // Ajoute un extra a l'intention :
        // le fichier ou sera sauvegarder la photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Demarre l'Intention de capture d'image
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    } // / onClick
}
