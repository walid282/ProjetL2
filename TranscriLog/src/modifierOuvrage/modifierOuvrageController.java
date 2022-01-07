package modifierOuvrage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.ResourceBundle;
import globalConnectionModule.globalCnx;


import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * classe qui gere la page Modifier Ouvrage du logiciel
 *
 */
public class modifierOuvrageController implements Initializable{

    @FXML
    private Label lbl_titreOuvrage;
	@FXML
    private Button btn_ConfirmerModificationsOuvrage;
    @FXML
    private Button btn_Retrour;
    @FXML
    private Button btn_disconnect;
    @FXML
    private Button btn_ConfirmerModificationsImprimeur;
    @FXML
    private Button btn_ConfirmerModificationsEditeur;
    @FXML
    private Button btn_DeletePages;
    @FXML
    private Button btn_loadPage;

    @FXML
    private Label lbl_nom;
    @FXML
    private Label lbl_prenom;
    
    @FXML
    private TextField txt_numNewPage;
    @FXML
    private TextField txt_numPageToDelete;
    @FXML
    private TextField txt_newAdresseEditeur;
    @FXML
    private TextField txt_newAdresseImprimeur;
    @FXML
    private TextArea txt_newCommentaire;
    @FXML
    private DatePicker newDate;
    @FXML
    private TextField txt_newNomAuteur;
    @FXML
    private TextField txt_newNomEditeur;
    @FXML
    private TextField txt_newNomImprimeur;
    @FXML
    private TextField txt_newPrenomAuteur;
    @FXML
    private TextField txt_newTelEditeur;
    @FXML
    private TextField txt_newTelImprimeur;
    @FXML
    private TextField txt_newTitreOuvrage;
    
    public String titreOuvrage;
 	public String commentaire;
 	public LocalDate txt_newDate;
 	public String dateOuvrageString;
    public String nomAuteur;
    public String prenomAuteur;
    public String nomEditeur;
    public String adresseEditeur;
    public String telEditeur;
    public String nomImprimeur;
    public String adresseImprimeur;
    public String telImprimeur;
        		
    //LocalDate date = new LocalDate(); 
    /**
     * Test dabord si les champs ne sont pas vides, si ce n'est
     * pas le cas, on lance la modification de l'ouvrage
     * @param event
     */
    public void btn_ConfirmerOuvrageOnAction(ActionEvent event) {
    	titreOuvrage = txt_newTitreOuvrage.getText();
    	commentaire = txt_newCommentaire.getText();
    	dateOuvrageString = "";
    	if(newDate.getValue() !=null) 
    	{
    		dateOuvrageString = newDate.getValue().toString();
    	}
    	
    	globalCnx.glconnect.modifyOuvrage(titreOuvrage, dateOuvrageString, commentaire);
    	globalCnx.glconnect.messageBoxAlert("Modifications ouvrage", "L'ouvrage a bien été modifié", AlertType.INFORMATION);
    }
    
    /**
     * méthode qui modifie l'auteur
     * @param event
     */
    public void btn_ConfirmerAuteurOnAction(ActionEvent event) {
    	// Si l'auteur n'existe pas
    	nomAuteur = txt_newNomAuteur.getText();
    	prenomAuteur = txt_newPrenomAuteur.getText();
		try {
			if (globalCnx.glconnect.checkAuteurExist(nomAuteur, prenomAuteur) == 0) {
				if (globalCnx.glconnect.messageBox("Attention", "Cet auteur n'existe pas notre base de donnée !", "Voulez vous l'y ajouter ?")){
					globalCnx.glconnect.createAuteur(nomAuteur, prenomAuteur);
					globalCnx.glconnect.messageBoxAlert("Auteur créé", "Votre auteur a été ajouté avec succès", AlertType.INFORMATION);
					globalCnx.glconnect.modifyAuteur(prenomAuteur, nomAuteur);
				}
				else {
					globalCnx.glconnect.messageBoxAlert("Aucune modification", "Aucune modification n'a été effectué, veuillez recommencer si vous voulez ajouter un auteur", AlertType.INFORMATION);
				}
			}
			else {
				globalCnx.glconnect.modifyAuteur(prenomAuteur, nomAuteur);
				globalCnx.glconnect.messageBoxAlert("Modification effectuée", "Nous avons bien modifié l'auteur de votre ouvrage", AlertType.INFORMATION);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	globalCnx.glconnect.modifyOuvrage(titreOuvrage, dateOuvrageString, commentaire);
    	globalCnx.glconnect.messageBoxAlert("Modifications ouvrage", "L'ouvrage a bien été modifié", AlertType.INFORMATION);
    }
    
    /**
     * méthode qui modifie l'editeur
     * @param event
     */
    public void btn_ConfirmerEditeurOnAction(ActionEvent event) {
       	nomEditeur = txt_newNomEditeur.getText();
       	adresseEditeur = txt_newAdresseEditeur.getText();
       	telEditeur = txt_newTelEditeur.getText();
    	try {
    		if (globalCnx.glconnect.checkEditeurExist(nomEditeur) == 0) {
    			if (globalCnx.glconnect.messageBox("Attention", "Cet éditeur n'existe pas notre base de donnée !", "Voulez vous l'y ajouter ?")){
    				globalCnx.glconnect.createEditeur(nomEditeur, adresseEditeur, telEditeur);
    				globalCnx.glconnect.messageBoxAlert("Editeur créé", "Votre éditeur a été ajouté avec succès", AlertType.INFORMATION);
    				globalCnx.glconnect.modifyEditeur(nomEditeur, adresseEditeur, telEditeur);
    			}
    			else {
    				globalCnx.glconnect.messageBoxAlert("Aucune modification", "Aucune modification n'a été effectué, veuillez recommencer si vous voulez ajouter un auteur", AlertType.INFORMATION);
    			}
    		}
    		else {
    			globalCnx.glconnect.modifyEditeur(nomEditeur, adresseEditeur, telEditeur);
    			globalCnx.glconnect.messageBoxAlert("Modification effectuée", "Nous avons bien modifié l'auteur de votre ouvrage", AlertType.INFORMATION);
    		}
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
       	globalCnx.glconnect.modifyEditeur(nomEditeur, adresseEditeur, telEditeur);
       	globalCnx.glconnect.messageBoxAlert("Modifications ouvrage", "L'ouvrage a bien été modifié", AlertType.INFORMATION);
       }
    
    /**
     * méthode qui mofidie l'imprimeur
     * @param event
     */
    public void btn_ConfirmerImprimeurOnAction(ActionEvent event) {
       	nomImprimeur = txt_newNomImprimeur.getText();
       	adresseImprimeur = txt_newAdresseImprimeur.getText();
       	telImprimeur = txt_newTelImprimeur.getText();
    	try {
    		if (globalCnx.glconnect.checkImprimeurExist(nomImprimeur) == 0) {
    			if (globalCnx.glconnect.messageBox("Attention", "Cet imprimeur n'existe pas notre base de donnée !", "Voulez vous l'y ajouter ?")){
    				globalCnx.glconnect.createImprimeur(nomImprimeur, adresseImprimeur, telImprimeur);
    				globalCnx.glconnect.messageBoxAlert("Imprimeur créé", "Votre imprimeur a été ajouté avec succès", AlertType.INFORMATION);
    				globalCnx.glconnect.modifyEditeur(nomImprimeur, adresseImprimeur, telImprimeur);
    			}
    			else {
    				globalCnx.glconnect.messageBoxAlert("Aucune modification", "Aucune modification n'a été effectué, veuillez recommencer si vous voulez ajouter un auteur", AlertType.INFORMATION);
    			}
    		}
    		else {
    			globalCnx.glconnect.modifyImprimeur(nomImprimeur, adresseImprimeur, telImprimeur);
    			globalCnx.glconnect.messageBoxAlert("Modification effectuée", "Nous avons bien modifié l'imprimeur de votre ouvrage", AlertType.INFORMATION);
    		}
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
       	globalCnx.glconnect.modifyImprimeur(nomImprimeur, adresseImprimeur, telImprimeur);
       	globalCnx.glconnect.messageBoxAlert("Modifications ouvrage", "L'ouvrage a bien été modifié", AlertType.INFORMATION);}

    /**
     * méthode qui ouvre la fenetre accueil, il s'agit d'un retour
     * @param event
     */
    public void btn_RetourOnAction(ActionEvent event) {
    	String link = "/accueil/accueil.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Accueil");
    }

    
    /**
     * méthode qui deconnecte l'utilisateur et l'emmene sur 
     * la page connexion
     * @param event
     */
    public void btn_disconnectOnAction(ActionEvent event) {
    	String link = "/application/Sample.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Connexion");
    }

    /**
     * Méthode qui supprime la page selectionnée
     * @param event
     */
    public void btn_DeletePagesOnAction(ActionEvent event) {
    	
    	try {
			if (txt_numPageToDelete.getText().trim() != "") {
				String numPagesToDelete = txt_numPageToDelete.getText();
				if (globalCnx.glconnect.messageBox("ATTENTION", "La suppressions des pages est irreversible", "Confirmez vous la suppression ?")) {
					globalCnx.glconnect.deletePages(numPagesToDelete);
					globalCnx.glconnect.messageBoxAlert("Suppression de pages", "Page(s) supprimé(es)", AlertType.CONFIRMATION);
				}	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    /**
     * méthode qui permet de charger une page en ouvrant
     * un explorer de fichier et de sélectionner l'image que l'on souhaite
     * @param event
     * @throws IOException
     */
    public void btn_loadPageOnAction(ActionEvent event) throws IOException {
    	
    	   // L'utilisateur doit preciser le numéro de la page à ajouter
    	   String numPageToAdd = txt_numNewPage.getText();
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

    	   boolean checkIfPageExist = globalCnx.glconnect.checkIfPageExists(numPageToAdd);
    	   if (!checkIfPageExist) {
    		   if (numPageToAdd.trim()=="") {
    			   globalCnx.glconnect.messageBoxAlert("Attention", "Veuillez préciser le numéro de page à ajouter", AlertType.WARNING);
    		   }
    		   else
    		   {
       				// Ouverture de l'explorer pour chercher la page
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
    		   else {
    			   if(globalCnx.glconnect.messageBox("Attention", "Cette page existe déjà, vous risquez de l'écraser.", "Voulez vous vraiment écraser cette page ?")) {
    				 
    				   globalCnx.glconnect.updatePage(numPageToAdd, sourcePath);
    				   File sourceFile = new File(sourcePath);        //  On créee un objet File correspondant au fichier d'origine 
      					//                               src\images_projet\idUser_idOuvrage_numPageToAdd_NomdudFichierSource
      					File destinationFile = new File(destinationPath + globalCnx.glconnect.userID + "_" + globalCnx.curOuvrageId + "_" + numPageToAdd + "_" + Name);   //Creating A Destination File. Name stays the same this way, referring to getName(
      					System.out.println("DestinationFile  = " + destinationFile.getPath());
      					//Files.copy(sourceFile.toPath(), destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
       			
      					globalCnx.glconnect.addNewPage(numPageToAdd,sourcePath);
      					globalCnx.glconnect.messageBoxAlert("Page ajoutée", "Nous avons bien ajouté votre page", AlertType.INFORMATION);
      				
    			   }
    			   else {
    			   globalCnx.glconnect.messageBoxAlert("Annulation", "Mise a jour page d'ouvrage annulée", AlertType.INFORMATION);
    			   }
    			   }
    	   }

    	

    }
    /**
     * la méthode qui donne le titre de l'ouvrage que l'on modifie
     */
	public void init_lblTitre() {
		String titreOuvrage = globalCnx.glconnect.init_titreOuvrage(globalCnx.glconnect.curOuvrageId);
		lbl_titreOuvrage.setText(titreOuvrage);
	}
	
	
	
	@Override
	/**
	 * méthode qui charge la page directement avec le nom et le prenom de 
	 * l'utilisateur + le titre de l'ouvrage que l'on modifie
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
		init_lblTitre();
		txt_newCommentaire.setWrapText(true);
	}

}
