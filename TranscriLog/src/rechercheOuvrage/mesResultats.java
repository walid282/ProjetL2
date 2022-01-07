package rechercheOuvrage;

import javafx.collections.ObservableList;

public class mesResultats {

	private String titre;
    private String auteur;
    private String editeur;
    private String imprimeur;

    public mesResultats(String titre, String auteur, String editeur, String imprimeur) {
    	super();
    	this.titre = titre;
    	this.auteur = auteur;
    	this.editeur = editeur;
    	this.imprimeur = imprimeur;
    }

/**
* @return the titre
*/
    public String getTitre() {
    	return titre;
    }

/**
* @return the auteur
*/
    public String getAuteur() {
    	return auteur;
    }

/**
* @return the editeur
*/
    public String getEditeur() {
    	return editeur;
    }

/**
* @return the imprimeur
*/
    public String getImprimeur() {
    	return imprimeur;
    }


}