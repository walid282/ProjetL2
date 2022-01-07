/**
 * 
 */
package startTranscription;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import rechercheOuvrage.*;
import accueil.*;
import app.CCLabeler;
import app.ImagesToProcessList;
import app.MeasuresList;

import java.io.File;
import java.io.InputStream;
import java.util.Observable;
import java.util.ResourceBundle;
import globalConnectionModule.globalCnx;


import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *classe qui permet de d�marrer la transcription de pages d'un ouvrage
 */
public class startTranscriptionController implements Initializable{
	 @FXML
	 private Button btn_loadPage;
	 @FXML
	 private Button btn_transcrire;
	 @FXML
	 private ImageView img_origine;
	 @FXML
	 private ImageView img_transcrite;

	 @FXML
	 private Label lbl_appelApi;
	 @FXML
	 private Label lbl_nom;
	 @FXML
	 private Label lbl_prenom;

	 @FXML
	 private TextField txt_numeroPage;

    /**
     * m�thode qui permet de charger une page dans la base de donn�ees
     * selon le num�ro de page choisis par l'utilisateur
     * @param event
     */
    public void loadPageOnaction(ActionEvent event) {
  	   // L'utilisateur doit preciser le num�ro de la page � ajouter
  	   String numPage = txt_numeroPage.getText();
  	   if (numPage.trim()=="") {
  		   globalCnx.glconnect.messageBoxAlert("Attention", "Veuillez pr�ciser le num�ro de page � transcrire", AlertType.WARNING);
  	   }
  	   else
  	   {
           // On cherche la page dans la BDD
  		    numPage = txt_numeroPage.getText();
            String pagePath = globalCnx.glconnect.getPage(numPage);
            Path chemin = Paths.get(pagePath);        
 			Path Name = chemin.getFileName();      // On r�cupere la partie Nom du chemin absolu

            lbl_appelApi.setText("Page " + Name.toString() + " Charg�e");
            
            // On essaie de l'afficher
            File file = new File(pagePath);
            Image image = new Image(file.toURI().toString());
            img_origine.setImage(image); 
            

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
    
    
    /**
     * m�thode qui permet de d�marrer la transcription
     * @param event
     */
    public void transcrireOnAction(ActionEvent event) {
    	       // On  cr�e une liste d'images � traiter
    			ImagesToProcessList ipl = new ImagesToProcessList();
    			//ipl.addImagesFromFolder("./images/document/"); // 10 images
    			//ipl.addImageName("./images/document/00000.jpg");
    			ipl.addImageName("./images_projet/0.jpg");
    			// traite chaque image de la liste
    			CCLabeler counter = new CCLabeler();

    			for (Object o : ipl) {
    				// traite l'image et compte les particules
    				String imagename_to_process = (String) o;
    				System.out.println(imagename_to_process);
    				counter.process(imagename_to_process);

    				// recupère les mesures de l'image traitée
    				MeasuresList measure_list = counter.getMeasures();

    			}
    }

	@Override
	/**
	 * m�thode qui initialise la page en mettant le nom et le pr�nom 
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		globalCnx.init_Labels_nom_prenom(lbl_nom, lbl_prenom);
	}
}
