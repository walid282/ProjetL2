package accueil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
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

import java.io.File;
import java.util.Observable;
import java.util.ResourceBundle;
import globalConnectionModule.globalCnx;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Cette classe permet de controller la page Accueil, chaque bouton,
 * chaque zone de texte etc est g�r� ici. C'est ici qu'on appelle toutes les m�thodes
 * pour agir sur cette page
 *
 */
public class accueilController implements Initializable {
	@FXML
	private Label lbl_prenom;
	@FXML
	private Label lbl_nom;
	@FXML
	private Button btn_demarrerTranslation;
	@FXML
	private Button btn_rechercherOuvrage;
	@FXML
	private Button btn_detailsOuvrage;
	@FXML 
	private Button donneMoiMonNom;
	@FXML
	private Button btn_seDeconnecter;
	@FXML
	private Button btn_deleteOuvrageSelectionned;
	@FXML
	private TableView<mesOuvrages> tableView_listeOuvragesPerso;
	@FXML
	private TableColumn<mesOuvrages, String> titre;
	@FXML
	private TableColumn<mesOuvrages, Integer> idOuvrage;

	    
	private int userID = globalCnx.userID;
	private ObservableList<mesOuvrages> listMesOuvrages = FXCollections.observableArrayList();
	private ObservableList<mesOuvrages> listMesOuvragesSelected = FXCollections.observableArrayList();
	
	// Ouvre la jFrame Transcriptions
	/**
	 * Cette m�thode permet d'ouvrir la fen�tre Cr�ation d'un ouvrage 
	 * (m�me si le nom de la m�thode laisse pr�sager qu'on ouvre une autre 
	 * fen�tre, il s'agit juste d'un probl�me de notation)
	 * @param event
	 */
	public void demarrerTranslationOnAction(ActionEvent event) {
		String link = "/transcription/transcription.fxml";
		globalCnx.glconnect.openWindow(event, link, "Transcrilog : Cr�ation ouvrage");
}
	// Ouvre la jFrame Ouvrage
	/**
	 * Cette m�thode permet d'ouvrir la fen�tre Recherche d'un ouvrage
	 * @param event
	 */
	public void rechercherOuvrageOnAction(ActionEvent event) {
		String link = "/rechercheOuvrage/rechercheOuvrage.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Recherche d'ouvrage");
	}
	
	// Ouvre la jFrame D�tails de l'ouvrage
	/**
	 * Cette m�thode permet d'ouvrir la fen�tre D�tails d'un ouvrage en veillant
	 * � bien r�cup�rer les informations de l'ouvrage s�lectionn�
	 * @param event
	 */
	public void detailsOuvrageOnAction(ActionEvent event) {
		listMesOuvragesSelected = tableView_listeOuvragesPerso.getSelectionModel().getSelectedItems();
		globalCnx.glconnect.curOuvrageId = listMesOuvragesSelected.get(0).getIdOuvrage();
		String linkOuvrage = "/ouvrage/ouvrage.fxml";
		globalCnx.glconnect.callingWindowLink = "/accueil/accueil.fxml";
		globalCnx.glconnect.callingWindowTitle = "Transcrilog : Accueil";
		globalCnx.glconnect.openWindow(event, linkOuvrage,"Transcrilog : D�tails de l'ouvrage");
	}
	
	// Initialise les labels et la grille des ouvrages 
	/**
	 * Cette m�thode permet que d�s lors que la fen�tre accueil est ouverte
	 * que certains affichage ou options apparaissent instantanement
	 */
	public void initialisations() {
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
		init_tableMesOuvrages();
}

	/*
	ObservableList<mesOuvrages> list = FXCollections.observableArrayList(
			new mesOuvrages("Ouvrage1"),
			new mesOuvrages("Ouvrage2"),
			new mesOuvrages("Ouvrage3"),
			new mesOuvrages("Ouvrage4"),
			new mesOuvrages("Ouvrage5")
			);
	*/
	
	@Override
	// Surcharge de la proc�dure d'initialisation de JFrame acceuil des ouvrages utilisateur
	/**
	 * Cette m�thode permet que d�s lors que la fen�tre accueil est ouverte
	 * que certains affichage ou options apparaissent instantanement
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
		init_tableMesOuvrages();
	}
	
	// Initialise la liste des ouvrages de l'utilisateur connect�
	private void init_tableMesOuvrages() {
		listMesOuvrages = globalCnx.glconnect.getMesOuvrages();
		idOuvrage.setCellValueFactory(new PropertyValueFactory<mesOuvrages, Integer>("idOuvrage"));
		titre.setCellValueFactory(new PropertyValueFactory<mesOuvrages, String>("titre"));
		tableView_listeOuvragesPerso.setItems(listMesOuvrages);
	}
	
	/**
	 * Cette m�thode permet de pouvoir se d�connecter et de retourner
	 * � la page de connexion
	 * @param event
	 */
	public void btn_seDeconnecterOnAction(ActionEvent event) {
		String link = "/application/Sample.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Connexion");
		}
	
	/**
	 * Cette m�thode permet de supprimer des ouvrages que 
	 * l'utilisateur a ajout� dans la page. 
	 * @param event
	 */
	public void btn_deleteOuvrageSelectionned(ActionEvent event) {
		if(globalCnx.glconnect.messageBox("ATTENTION", "Si vous supprimez cet ouvrage vous perdrez toutes ses donn�es", "Voulez vous vraiment le supprimer?")) {
			listMesOuvragesSelected = tableView_listeOuvragesPerso.getSelectionModel().getSelectedItems();
			System.out.println(listMesOuvragesSelected);
			globalCnx.curOuvrageId = listMesOuvragesSelected.get(0).getIdOuvrage();
			globalCnx.glconnect.deleteOuvrage(globalCnx.curOuvrageId);
			listMesOuvrages = globalCnx.glconnect.getMesOuvrages();
			idOuvrage.setCellValueFactory(new PropertyValueFactory<mesOuvrages, Integer>("idOuvrage"));
			titre.setCellValueFactory(new PropertyValueFactory<mesOuvrages, String>("titre"));
			tableView_listeOuvragesPerso.setItems(listMesOuvrages);
			globalCnx.glconnect.messageBoxAlert("Ouvrage supprim�", "Nous avons bien supprim� l'ouvrage s�l�ctionn�", AlertType.ERROR);
		}
	}

}
	