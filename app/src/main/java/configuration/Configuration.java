package configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jrbebel on 19/06/16.
 */
public class Configuration {

    private static String pathroot = "http://ns320828.ip-5-39-86.eu:8080/";

    public Configuration() {


    }

    public static String getConnexion(String email, String password) {

        String connexionUrl = pathroot + "apiLeBonCoin/ConnexionApi?email=" + email + "&mdp=" + password;
        return connexionUrl;
    }

    public static String getAnnonce(String type) {

        String annonceUrl = pathroot + "apiLeBonCoin/ListeAnnonceOffre?type=" + type;
        return annonceUrl;
    }

    public static String getEspaceImage() {

        String pathImage = "http://ns320828.ip-5-39-86.eu/siteLeboncoin/imageAnnonce/";
        return pathImage;

    }

    public static String getAnnonceDetails(String idAnnonce) {

        String annonceDeetailsURL = pathroot + "apiLeBonCoin/AnnonceDescriptifForAndroid?id=" + idAnnonce;
        return annonceDeetailsURL;
    }

    public static String setAnnonce(String titreUrl, String prixUrl, String produiType, String categorie, String descriptif, String user) throws UnsupportedEncodingException {

        String setAnnonce = "http://ns320828.ip-5-39-86.eu:8080/apiLeBonCoin/DeposerAnnonceForAndroid?" +
                "designation=" + URLEncoder.encode(titreUrl, "UTF-8") + "&prix=" + URLEncoder.encode(prixUrl, "UTF-8") + "&produitype=" + "1" +
                "&idclient=" + user + "&categorie=" + "1" + "&descriptif=" + URLEncoder.encode(descriptif, "UTF-8");


        return setAnnonce;
    }

}
