package accueil;

import javafx.collections.ObservableList;

// Classe mes ouvrages
// Utilisée dans la grille des ouvrages de l'utilisateur pour la liste  ObservableList<mesOuvrages>
/**
 * C'est grâce à cette classe que l'on peut créer la liste
 * des ouvrages persos dans la page accueil
 * @author wella
 *
 */
public class mesOuvrages {
	
	private String Titre;
    private Integer idOuvrage;
	
	public mesOuvrages(Integer idOuvrage,String titre) {
		super();
		this.Titre = titre;
		this.idOuvrage = idOuvrage;
	}


	public String getTitre() {
		return this.Titre;
	}
	
	public Integer getIdOuvrage() {
		return this.idOuvrage;
	}
	
}
