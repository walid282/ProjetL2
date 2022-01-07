package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import globalConnectionModule.globalCnx;


import java.io.File;
import java.util.ResourceBundle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cette classe est la classe qui permet de controller
 * la fenetre de connexion, c'est elle qui régit tout
 * dans cette fenêtre.
 *
 */
public class SampleController {
	
	@FXML
	private Button connexionButton;
	@FXML
	private TextField mailUser;
	@FXML
	private PasswordField passwordUser;
	@FXML
	private Label messageErreur;
	@FXML
	private Button inscriptionButton;
	
// Ouvre la JFrame Inscription
	/**
	 * Cette méthode permet d'ouvrir la fenêtre Inscription
	 * @param event
	 */
	public void inscriptionButtonOnAction(ActionEvent event) {
		String link = "/inscription/inscription.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Inscription");
	}
	
	
    // teste si le login est correcte (dans la BDD) et récupère l'identifiant de l'utilisateur
	/**
	 * Cette méthode teste si l'utilisateur a inscrit le bon mot de passe
	 * et la bonne adresse email.
	 * @param event
	 */
	public void connexionButtonOnAction(ActionEvent event) {
		messageErreur.setText("");
		//Connection sqlCnx = glconnect.getConnect();
		boolean connected = false;
		try {
		connected = globalCnx.glconnect.loginCheck(mailUser.getText(), passwordUser.getText());
		if (connected){
			globalCnx.glconnect.openWindow(event, "/accueil/accueil.fxml","Transcrilog : Accueil");
		}else {
			messageErreur.setText("Identifiant ou mot de passe incorrects");
		}
		} catch (Exception e) {
			messageErreur.setText("Erreur execution");
			e.printStackTrace();
		}
		globalCnx.glconnect.closeConnection();
	}
	
}