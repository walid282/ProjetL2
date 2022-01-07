package inscription;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.ResourceBundle;
import globalConnectionModule.globalCnx;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * classe qui gere l'inscription d'un utilisateur
 *
 */
public class inscriptionController {
	@FXML
	private TextField txt_newEmail;
	@FXML
	private PasswordField txt_newPassword;
	@FXML
	private TextField txt_newPrenom;
	@FXML
	private TextField txt_newNom;
	@FXML
	private PasswordField txt_confirmPassword;
	//@FXML
	//private Label lbl_messageErreur;
	
	@FXML
	private Button btn_inscription;
	@FXML
	private Button btn_letsConnect;
	
    // Inscrit le nouvel utilisateur dans la BDD
	
	/**
	 * méthode qui :
	 * test si l'inscription est valide en vérifiant si un compte 
	 * avec cette adresse email existe déjà
	 * puis si ce n,'est pas le cas, l'inscris en l'ajoutant à
	 * la base de données.
	 * @param event
	 */
	public void inscriptionOnAction(ActionEvent event) {
		//lbl_messageErreur.setText("");
		
		String newEmail = txt_newEmail.getText();
		String newPassword = txt_newPassword.getText();
		String newPrenom = txt_newPrenom.getText();
		String newNom = txt_newNom.getText();
		String confirmPassword = txt_confirmPassword.getText();
		globalCnx glInscription = new globalCnx();
		int res = globalCnx.glconnect.ERR_USER_CREATION_FAILED;

		if (txt_newEmail.getText().isEmpty() || txt_newPassword.getText().isEmpty()|| txt_newPrenom.getText().isEmpty()|| txt_newNom.getText().isEmpty()) {
			globalCnx.glconnect.messageBoxAlert("ATTENTION", "Tous les champs sont obligatoires !", AlertType.ERROR);
		}
		else {
		try {
			if (newPassword.equals(confirmPassword) == false) {
				globalCnx.glconnect.messageBoxAlert("Erreur mot de passe", "Le mot de passe doit être le même dans mot de passe et confirmer mot de passe", AlertType.ERROR);
			}
			else {
			res = globalCnx.glconnect.inscriptionUtilisateur(newEmail, newPassword, newPrenom, newNom);
			if (res ==globalCnx.glconnect.USER_CREATION_SUCCED){
				globalCnx.glconnect.messageBoxAlert("Compte créé", "Votre compte a bien été créé", AlertType.INFORMATION);
				btn_letsConnect.setVisible(true);
			}if (res == globalCnx.glconnect.ERR_USER_ALREADY_EXISTS) {
				globalCnx.glconnect.messageBoxAlert("ATTENTION", "Cet utilisateur existe déjà", AlertType.ERROR);
			}
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
	
    // Test si le login est correct récupère l'identifiant et informe si on est connécté ou pas	
	/**
	 * méthode qui permet de connecter l'utilisateur en ouvrant la
	 * fenêtre accueil
	 * @param event
	 */
	public void letsconnectButtonOnAction(ActionEvent event) {
		
		String mailUser = txt_newEmail.getText();
		String passwordUser = txt_newPassword.getText();
		boolean connected = false;
		try {
		connected = globalCnx.glconnect.loginCheck(mailUser, passwordUser);
		globalCnx.glconnect.closeConnection();
        globalCnx.glconnect.openWindow(event, "/accueil/accueil.fxml","Transcrilog : Accueil");
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	/**
	 * méthode qui ouvre la fenêtre connexion
	 * @param event
	 */
	public void retourConnexionOnAction(ActionEvent event) {
		String link = "/application/sample.fxml";
		globalCnx.glconnect.openWindow(event, link,"Transcrilog : Connexion");
    }
}
	