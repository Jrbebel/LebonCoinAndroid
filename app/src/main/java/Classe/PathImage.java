package Classe;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by JRBEBEL on 22/06/2016.
 */
public class PathImage {


    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;

    /**
     * type n'est pas utilise pour l'instant
     * permettrait de distinguer photo et video
     *
     * @param type
     * @return
     */
    public static Uri getOutputMediaFileUri(int type) {
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

}
