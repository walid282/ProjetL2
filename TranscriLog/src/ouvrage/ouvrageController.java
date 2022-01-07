package ouvrage;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import rechercheOuvrage.*;
import accueil.*;

import java.io.File;
import java.io.InputStream;
import java.util.Observable;
import java.util.ResourceBundle;
import globalConnectionModule.globalCnx;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * classe qui gere la page ouvrage
 *
 */
public class ouvrageController implements Initializable{
	  	@FXML
	    private ImageView img_Apercu;

	  	@FXML
	  	private Button btn_retour;
	  	@FXML
	    private Button btn_transcription;
	  	@FXML
	    private Button btn_modifier;
	  	
	  	@FXML
	    private Label lbl_Titre;

	  	@FXML
	    private Label lbl_Editeur;

	    @FXML
	    private Label lbl_Imprimeur;

	    @FXML
	    private Label lbl_dateSortie;

	    @FXML
	    private Label lbl_nbDePage;

	    @FXML
	    private Label lbl_nom;

	    @FXML
	    private Label lbl_nomAuteur;

	    @FXML
	    private Label lbl_nomTranscripteur;

	    @FXML
	    private Label lbl_prenom;

	    @FXML
	    private TextArea txtArea_commentaire;

	    
		@Override
		// Surcharge la procédure pour initialiser les controles de la JFrame 
		/**
		 * méthode qui permet de charger la page avec le nom de l'utilisateur
		 */
		public void initialize(URL arg0, ResourceBundle arg1) {
			init_Labels();
			globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
		}
	    
		// Ouvre la JFrame accueil (fentre liste ouvrages de l'utilisateur connecté)
		/**
		 * méthode qui permet de retourner à la page d'accueil
		 * @param event
		 */
		public void btn_retourOnAction(ActionEvent event) {
			//String link = "/accueil/accueil.fxml";
			globalCnx.glconnect.openWindow(event, globalCnx.callingWindowLink,globalCnx.callingWindowTitle);
			}	   
	    
	    
		// Initialisations des labels sur la jFrame Ouvrage 
	    private void init_Labels() {
			try {
				ResultSet rsLabels = globalCnx.glconnect.getUserOuvragesDetails(globalCnx.glconnect.curOuvrageId);
				while(rsLabels.next())
				{
					String nom_transcripteur = rsLabels.getString("Nom_Transcripteur");
					String prenom_transcripteur = rsLabels.getString("Prenom_Transcripteur");
					String titre_ouvrage = rsLabels.getString("Titre_Ouvrage");
					String nbPage_ouvrage = rsLabels.getString("Nb_Pages_Ouvrage");
					String dateSortie_ouvrage = rsLabels.getString("Date_Sortie_Ouvrage");
					String commentaire_Ouvrage = rsLabels.getString("Commentaire");
					String nom_auteur = rsLabels.getString("Nom_Auteur");
					String prenom_auteur = rsLabels.getString("Prenom_Auteur");
					String nom_editeur = rsLabels.getString("Nom_Editeur");
					String adresse_editeur = rsLabels.getString("Adresse_Editeur");
					String tel_editeur = rsLabels.getString("Tel_Editeur");
					String nom_imprimeur = rsLabels.getString("Nom_Imprimeur");
					String adresse_imprimeur = rsLabels.getString("Adresse_Imprimeur");
					String tel_imprimeur = rsLabels.getString("Tel_Imprimeur");
					String rang_page = rsLabels.getString("rang_page");
					String lienOriginal_page = rsLabels.getString("lien_Original");
					
					lbl_Titre.setText(titre_ouvrage);
					lbl_nomTranscripteur.setText(nom_transcripteur + " " + prenom_transcripteur);
					lbl_dateSortie.setText(dateSortie_ouvrage);
					lbl_Editeur.setText(nom_editeur);
					lbl_Imprimeur.setText(nom_imprimeur);
					lbl_nbDePage.setText(nbPage_ouvrage);
					lbl_nomAuteur.setText(nom_auteur + " " + prenom_auteur);
					
					txtArea_commentaire.setText(commentaire_Ouvrage);
					txtArea_commentaire.setWrapText(true);
					txtArea_commentaire.setEditable(false);
					//Class<?> clazz = this.getClass();
					//InputStream url = clazz.getResourceAsStream("2.png");
					//Image image = new Image("2.png");
					//img_Apercu = new ImageView(image);
					//img_Apercu.setImageR(););
					
					//File file = new File(lienOriginal_page);
			        //Image image = new Image(file.toURI().toString());
			        //img_Apercu.setImage(image);
				}			
			} catch
			 (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	    
	    /**
	     * méthode qui permet d'ouvrir la fenetre modifierOuvrage
	     * @param event
	     */
	    public void btn_modifierOnAction(ActionEvent event) {
	    	String link = "/modifierOuvrage/modifierOuvrage.fxml";
			globalCnx.glconnect.openWindow(event, link,"Transcrilog : Modification d'un ouvrage");
	    }

	   /**
	    * méthode qui permet méthode qui permet d'ouvrir la fenêtre de transcription
	    * @param event
	    */
	    public void btn_transcriptionOnAction(ActionEvent event) {
	    	String link = "/startTranscription/startTranscription.fxml";
			globalCnx.glconnect.openWindow(event, link,"Transcrilog : Transcription");
	    }
}
