package rechercheOuvrage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import accueil.mesOuvrages;
/*
import java.io.File;
import java.util.Observable;
import java.util.ResourceBundle;
*/
import globalConnectionModule.globalCnx;

/*
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
*/

/**
 * classe qui gère la fenetre Recherche Ouvrage 
 *
 */
public class rechercheOuvrageController implements Initializable{

    @FXML
    private Button btn_details;
    @FXML
    private Button btn_lancerRecherche;
    @FXML
    private Button btn_retour;

    @FXML
    private Label lbl_nom;
    @FXML
    private Label lbl_prenom;

    @FXML
    private ToggleGroup optionRecherche;

    @FXML
    private RadioButton radio_btn_auteur;
    @FXML
    private RadioButton radio_btn_editeur;
    @FXML
    private RadioButton radio_btn_imprimeur;
    @FXML
    private RadioButton radio_btn_titre;

    @FXML
    private TableView<mesResultats> listeResultat;
    @FXML
    private TableColumn<mesResultats, String> tab_auteur;
    @FXML
    private TableColumn<mesResultats, String> tab_editeur;
    @FXML
    private TableColumn<mesResultats, String> tab_imprimeur;
    @FXML
    private TableColumn<mesResultats, String> tab_titre;
    

    @FXML
    private TextField txt_rechercheUser;
    accueil.accueilController accueilIdOuvrage = new accueil.accueilController();
	//public static Integer idOuvrageSelected;
	public String titre_choisis;
	public String auteur_choisis;
	
	private static String curTextfieldRecherche;
   
    private ObservableList<mesResultats> listMesResultats = FXCollections.observableArrayList();
    private ObservableList<mesResultats> listMesResultatsSelected = FXCollections.observableArrayList();
   
    /**
     * méthode qui ouvre la fenetre détails ouvrage de l'ouvrage selectionné
     * @param event
     */
    public void detailsOuvrageOnAction(ActionEvent event) {
    	listMesResultatsSelected = listeResultat.getSelectionModel().getSelectedItems();
    	titre_choisis = listMesResultatsSelected.get(0).getTitre();
    	auteur_choisis = listMesResultatsSelected.get(0).getAuteur();
    	globalCnx.curOuvrageId = globalCnx.glconnect.getIdOuvrage(titre_choisis, auteur_choisis);
    	globalCnx.callingWindowLink = "/rechercheOuvrage/rechercheOuvrage.fxml";
    	globalCnx.callingWindowTitle = "Transcrilog : Recherche d'ouvrage";
    	String linkOuvrage = "/ouvrage/ouvrage.fxml";
		globalCnx.glconnect.openWindow(event, linkOuvrage,"Transcrilog : Détails Ouvrage");
    }

    /**
     * méthode qui permet de lancer la recherche d'un ouvrage
     * selon le critere de recherche choisis
     * @param event
     */
    public void lancerRechercheOnAction(ActionEvent event) {
    	int searchBy = globalCnx.glconnect.SEARCH_BY_TITRE_OUVRAGE;
    	if (radio_btn_auteur.isSelected()) {
    		searchBy = globalCnx.glconnect.SEARCH_BY_NOM_AUTEUR ;
    	}
    	if (radio_btn_editeur.isSelected()) {
    		searchBy =  globalCnx.glconnect.SEARCH_BY_NOM_EDITEUR;
    	}
    	if (radio_btn_imprimeur.isSelected()) {
    		searchBy =  globalCnx.glconnect.SEARCH_BY_NOM_IMPRIMEUR;
    	}
        globalCnx.glconnect.curSearchBy = searchBy;
    	listMesResultats = globalCnx.glconnect.rechercheOuvrage(txt_rechercheUser.getText(), searchBy);
    	curTextfieldRecherche = txt_rechercheUser.getText();
    	updateListeOuvrages(listMesResultats);
    }


	private void updateListeOuvrages(ObservableList<mesResultats> listmesResultats) {
		if (listmesResultats != null) {
		txt_rechercheUser.setText(curTextfieldRecherche);
		if (globalCnx.glconnect.curSearchBy == globalCnx.glconnect.SEARCH_BY_NOM_AUTEUR) radio_btn_auteur.setSelected(true);
		if (globalCnx.glconnect.curSearchBy == globalCnx.glconnect.SEARCH_BY_NOM_EDITEUR) radio_btn_editeur.setSelected(true);
		if (globalCnx.glconnect.curSearchBy == globalCnx.glconnect.SEARCH_BY_TITRE_OUVRAGE) radio_btn_titre.setSelected(true);
		if (globalCnx.glconnect.curSearchBy == globalCnx.glconnect.SEARCH_BY_NOM_IMPRIMEUR) radio_btn_imprimeur.setSelected(true);
		tab_titre.setCellValueFactory(new PropertyValueFactory<mesResultats, String>("titre"));
    	tab_auteur.setCellValueFactory(new PropertyValueFactory<mesResultats, String>("auteur"));
    	tab_editeur.setCellValueFactory(new PropertyValueFactory<mesResultats, String>("editeur"));
    	tab_imprimeur.setCellValueFactory(new PropertyValueFactory<mesResultats, String>("imprimeur"));
    	
    	listeResultat.setItems(listmesResultats);
		}
	}
   

    /**
     * ouvre la fenêtre Accueil
     * @param event
     */
    public void retourOnAction(ActionEvent event) {
    	String link = "/accueil/accueil.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Accueil");
    }

    
	

	@Override
	/**
	 * méthode qui initialise la page avec le nom et prenom de l'utilisateur
	 * et donne la liste ouvrage
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
		updateListeOuvrages(globalCnx.curlistMesResultats);
	}
	
	
}

