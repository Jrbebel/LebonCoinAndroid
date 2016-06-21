package entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

/**
 * Created by jrbebel on 09/06/2016.
 * Classe permettant de recupere les getters et modifier les utilisateurs
 * par le biais des SharedPreferences
 */
public class users extends PreferenceActivity {


    public static final String MyPREFERENCES = "User";
    private String id;
    private String categorie;
    private String nom;
    private String prenom;
    private String region;
    private String adresse;
    private String codepostale;
    private String ville;
    private String telephone;
    private String datenaissance;
    private String pseudo;
    private String motdepasse;
    private String email;
    private String idVille;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedpreferences;

    public users(Context context) {

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    public String getIdUser() {
        id = sharedpreferences.getString("id", null).toString();
        return id;
    }

    public void setIdUser(String id) {
        editor.putString("id", id);
    }

    public String getCategorie() {
        categorie = sharedpreferences.getString("categorie", null).toString();
        return categorie;
    }

    public void setCategorie(String categorie) {
        editor.putString("categorie", categorie);
    }

    public String getNom() {
        nom = sharedpreferences.getString("nom", null).toString();
        return nom;
    }

    public void setNom(String nom) {
        editor.putString("nom", nom);
    }

    public String getPrenom() {
        prenom = sharedpreferences.getString("prenom", null).toString();
        return prenom;
    }

    public void setPrenom(String prenom) {
        editor.putString("prenom", prenom);
    }

    public String getRegion() {
        region = sharedpreferences.getString("region", null).toString();
        return region;
    }

    public void setRegion(String region) {
        editor.putString("region", region);
    }

    public String getAdresse() {
        adresse = sharedpreferences.getString("adresse", null).toString();
        return adresse;
    }

    public void setAdresse(String adresse) {
        editor.putString("adresse", adresse);
    }

    public String getCodepostale() {
        codepostale = sharedpreferences.getString("codepostale", null).toString();
        return codepostale;
    }

    public void setCodepostale(String codepostale) {
        editor.putString("codepostale", codepostale);
    }

    public String getIdVille() {

        idVille = sharedpreferences.getString("idVille", null).toString();
        return idVille;
    }

    public void setIdVille(String idVille) {
        editor.putString("idVille", idVille);
    }


    public String getVille() {
        ville = sharedpreferences.getString("ville", null).toString();
        return ville;
    }

    public void setVille(String ville) {
        editor.putString("ville", ville);
    }

    public String getTelephone() {
        telephone = sharedpreferences.getString("telephone", null).toString();
        return telephone;
    }

    public void setTelephone(String telephone) {
        editor.putString("telephone", telephone);
    }

    public String getDatenaissance() {
        datenaissance = sharedpreferences.getString("datenaissance", null).toString();
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        editor.putString("datenaissance", datenaissance);
    }

    public String getPseudo() {
        pseudo = sharedpreferences.getString("pseudo", null).toString();
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        editor.putString("pseudo", pseudo);
    }

    public String getMotdepasse() {

        motdepasse = sharedpreferences.getString("motdepasse", null).toString();
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        editor.putString("motdepasse", motdepasse);
    }

    public String getEmail() {
        email = sharedpreferences.getString("email", null).toString();
        return email;
    }

    public void setEmail(String email) {
        editor.putString("email", email);
    }

    public void commitPref() {
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
