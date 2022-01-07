package transcription;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import globalConnectionModule.globalCnx;


import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import accueil.accueilController;

import java.net.URL;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;

/**
 * classe qui gere la page création d'ouvrage
 */
public class transcriptionController implements Initializable {

	@FXML
	private Button btn_explorer;
	@FXML
	private Button btn_validerImage;
	@FXML
	private Button btn_validerOuvrageAuteur;

	@FXML
	private Button btn_validerEditeurImprimeur;
	@FXML
    private Button btn_retour;

	@FXML
	private Label lbl_nom;
	@FXML
	private Label lbl_prenom;

	@FXML
	private TextArea txt_commentaire;

	@FXML
	private TextField txt_titreOuvrage;

	@FXML
	private DatePicker date_Sortie;

	@FXML
	private TextField txt_nomAuteur;

	@FXML
	private TextField txt_prenomAuteur;
	@FXML
	private TextField txt_adresseEditeur;
	@FXML
	private TextField txt_adresseImprimeur;
	@FXML
	private TextField txt_nomEditeur;
	@FXML
	private TextField txt_nomImprimeur;
	@FXML
	private TextField txt_telEditeur;
	@FXML
	private TextField txt_telImprimeur;
	@FXML
	private TextField txt_numeroPageToAdd;

	@FXML
	private ImageView img_Apercu;
	
	@FXML
	private Tab onglet_EditeurImprimeur;
	@FXML
	private Tab onglet_Pages;
	
	private int idEditeur = 0;
	private int idImprimeur = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
	}
	



	
	// Utilise l'explorer pour chercher le fichier image à charger
	/**
	 * méthode qui permet de chatrger des pages grace
	 * à un explorer de fichier, on récupère le chemin
	 * @param event
	 * @throws IOException
	 */
    public void btn_loadPageOnAction(ActionEvent event) throws IOException {
    	
 	   // L'utilisateur doit preciser le numéro de la page à ajouter
 	   String numPageToAdd = txt_numeroPageToAdd.getText();
 	   if (numPageToAdd.trim()=="") {
 		   globalCnx.glconnect.messageBoxAlert("Attention", "Veuillez préciser le numéro de page à ajouter", AlertType.WARNING);
 	   }
 	   else
 	   {
    			// Ouverture de l'explorer pour chercher la page
 		    FileChooser fc = new FileChooser();
    			fc.getExtensionFilters().add(new ExtensionFilter("Png Files", "*.png"));
    			File file = fc.showOpenDialog(null);
    			// Si l'utilisateur à préciseé une Page dans l'explorateur
     		if (file != null) {
     			System.out.println(file.getAbsolutePath());

     			String link = file.getAbsolutePath();  // chemin absolu
     			Path chemin = Paths.get(link);        
     			Path Name = chemin.getFileName();      // On récupere la partie Nom du chemin absolu
     			System.out.println("Name = " + Name);
     			FileSystem fs = FileSystems.getDefault();   // Classe FileSystem pour ecrire sur l'ordi

     			String  sourcePath = chemin.toString();         // Fichier chargé par l'utilisateur 
     			String destinationPath="src\\images_projet\\";  // Début du changement du nom pour créer le fichier a enregistrer
     			//String destinationPath="D:\\Test\\";  // Début du changement du nom pour créer le fichier a enregistrer
     			File sourceFile = new File(sourcePath);        //  On créee un objet File correspondant au fichier d'origine 
     			//                               src\images_projet\idUser_idOuvrage_numPageToAdd_NomdudFichierSource
     			File destinationFile = new File(destinationPath + globalCnx.glconnect.userID + "_" + globalCnx.curOuvrageId + "_" + numPageToAdd + "_" + Name);   //Creating A Destination File. Name stays the same this way, referring to getName(
     			System.out.println("DestinationFile  = " + destinationFile.getPath());
     			//Files.copy(sourceFile.toPath(), destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
     			
     			globalCnx.glconnect.addNewPage(numPageToAdd,sourcePath);
     			globalCnx.glconnect.messageBoxAlert("Page ajoutée", "Nous avons bien ajouté votre page", AlertType.INFORMATION);
 	   }
 	
 			try 
 			{
 				//Files.copy(sourceFile.toPath(), destinationFile.toPath(),REPLACE_EXISTING);  
 				// Static Methods To Copy Copy source path to destination path
 			} catch(Exception e)
 			{
 				System.out.println(e);  // printing in case of error.
 			}
 	   }
}



    /**
     * méthode qui permet de créer l'ouvrage et l'auteur (si ni ouvrage ni auteur n'existent pas déjà)
     * @param event
     */
	public void btn_validerOuvrageAuteurOnAction(ActionEvent event) {
		// Si il manque un champ
		int idAuteur = 0;
		if(txt_nomAuteur.getText().isEmpty() || txt_prenomAuteur.getText().isEmpty()||
				txt_titreOuvrage.getText().isEmpty()) 
		{
			globalCnx.glconnect.messageBoxAlert("Erreur", "Attention les champs : Titre ouvrage, nom auteur, prénom auteur sont obligatoires.", AlertType.ERROR);
		}
		else  //Sinon tous les champs ont été saisis 
		{
			try 
			{
				// Si l'auteur n'existe pas
				if (globalCnx.glconnect.checkAuteurExist(txt_nomAuteur.getText(), txt_prenomAuteur.getText()) == 0)
				{
					//si auteur existe pas on crée l'auteur et on crée l'ouvrage
					createAuteurETOuvrage(); 
				}
				else // Sinon Auteur existe deja dans la base 
				{
					//si auteur existe alors on alerte l'user avec une msg box et on crée l'ouvrage s'il n'existe pas
					if(globalCnx.glconnect.messageBox("Attention", "Cet auteur existe déjà dans la base !", "Voulez-vous ajouter cet ouvrage quand même ?")) 
					{
						createOuvrageWhenItdoNotExist();		
					}

				}
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		globalCnx.glconnect.messageBoxAlert("Création Ouvrage", "L'ouvrage a été créé avec succès", AlertType.INFORMATION);
		onglet_EditeurImprimeur.setDisable(false);
		onglet_Pages.setDisable(false);
	}

	
	private void createOuvrageWhenItdoNotExist() {
		int idAuteur;
		if(globalCnx.glconnect.checkOuvrageExiste(txt_titreOuvrage.getText(), txt_nomAuteur.getText())==0) 
		{
			//si ouvrage n'existe pas : on cree
			idAuteur = globalCnx.glconnect.getIDauteur(txt_nomAuteur.getText(), txt_prenomAuteur.getText());
			globalCnx.glconnect.createOuvrage(txt_titreOuvrage.getText(), date_Sortie.getValue(), txt_commentaire.getText(), idAuteur, globalCnx.userID);
		}
	}
	
	private void createAuteurETOuvrage() {
		int idAuteur;
		int created = globalCnx.glconnect.createAuteur(txt_nomAuteur.getText(), txt_prenomAuteur.getText());
		if ( created!=0 ) 
		{

			idAuteur = globalCnx.glconnect.getIDauteur(txt_nomAuteur.getText(), txt_prenomAuteur.getText());
			globalCnx.glconnect.createOuvrage(txt_titreOuvrage.getText(), date_Sortie.getValue(), txt_commentaire.getText(), idAuteur, globalCnx.userID);
		}
		
	}

	/**
	 * Méthode qui permet d'ajouter l'editeur et l'imprimeur selon sils
	 * existent deja ou pas
	 * @param event
	 */
	public void btn_validerEditeurImprimeurOnAction(ActionEvent event) {
			// Si il manque un champ
			int idAuteur = 0;
			String nomEditeur = txt_nomEditeur.getText();
			String adresseEditeur = txt_adresseEditeur.getText();
			String telEditeur = txt_telEditeur.getText();
			String nomImprimeur = txt_nomImprimeur.getText();
			String adresseImprimeur = txt_adresseImprimeur.getText();
			String telImprimeur = txt_telImprimeur.getText();
			globalCnx.glconnect.curOuvrageId = globalCnx.glconnect.getIdOuvrage(txt_titreOuvrage.getText(), txt_nomAuteur.getText());
			if(txt_adresseEditeur.getText().isEmpty() || txt_adresseImprimeur.getText().isEmpty()||
					txt_nomEditeur.getText().isEmpty() || txt_nomImprimeur.getText().isEmpty() || txt_telEditeur.getText().isEmpty() || txt_telImprimeur.getText().isEmpty()) 
			{
				globalCnx.glconnect.messageBoxAlert("Erreur", "Attention les champs : Nom, adresse et télephones de l'éditeur et de l'imprimeur sont obligatoires.", AlertType.ERROR);
			}
			else  //Sinon tous les champs ont été saisis 
			{
				try 
				{
					
					// Si l'editeur n'existe pas je demande l'autorisation et je crée si Cancel on arrête
					if ( globalCnx.glconnect.checkEditeurExist(nomEditeur)==0) {
						if(globalCnx.glconnect.messageBox("Editeur non trouvé", "L'éditeur n'existe pas. ", "Voulez vous le créer ?")) {
							globalCnx.glconnect.createEditeur(nomEditeur, adresseEditeur, telEditeur);
							globalCnx.glconnect.updateEditeurIdInOuvrage(nomEditeur);
						}
						else {
							// on fait rien
						}
					}
					else {
						globalCnx.glconnect.updateEditeurIdInOuvrage(nomEditeur);
					}
					if ( globalCnx.glconnect.checkImprimeurExist(nomImprimeur)==0) {
						if(globalCnx.glconnect.messageBox("Imprimeur non trouvé", "L'imprimeur n'existe pas. ", "Voulez vous le créer ?")){
							globalCnx.glconnect.createImprimeur(nomImprimeur, adresseImprimeur, telImprimeur);
							globalCnx.glconnect.updateImprimeurIdInOuvrage(nomImprimeur);
						}
						else {
							//on f rien
						}
					}
					else {
						globalCnx.glconnect.updateImprimeurIdInOuvrage(nomImprimeur);
					}
					

				}
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	

	
	/**
	 * méthode qui ouvre la fenêtre Accueil
	 * @param event
	 */
	public void btn_retourOnAction(ActionEvent event) {
		 	String link = "/accueil/accueil.fxml";
			globalCnx.glconnect.openWindow(event, link,"Transcrilog : Accueil");
	    }

}
