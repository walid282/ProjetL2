package globalConnectionModule;

import javafx.scene.control.Label;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import com.mysql.jdbc.StringUtils;

import accueil.mesOuvrages;
//import application.bddConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import rechercheOuvrage.mesResultats;

// Classe globale gerant toutles les transactions avec la base de données
/**
 * Cette classe est la plus importante de tout le code, c'est 
 * elle qui est la classe centrale, c'est elle qui regroupe
 * toute les méthodes qui font appelle à la base de données, celle qui
 * regroupe toute les méthodes qui seront par la suite appelée par les 
 * autres classes dans les autres packages
 *
 */
public class globalCnx {
	public static int userID;
	public static globalCnx glconnect = new globalCnx();
	public static int curOuvrageId;
    public static int curSearchBy;
    public static ObservableList <mesResultats> curlistMesResultats;
	
	public Connection con;
	public final int ERR_USER_ALREADY_EXISTS = 1000;
	public final int ERR_USER_CREATION_FAILED = 1100;
	public final int USER_CREATION_SUCCED = 1;
	
	public final int ERR_AUTEUR_ALREADY_EXISTS = 2000;
	public final int ERR_AUTEUR_CREATION_FAILED = 2100;
	public final int ERR_OUVRAGE_ALREADY_EXISTS = 2200;
	public final int ERR_OUVRAGE_CREATION_FAILED = 2300;
	public final int OK_AUTEUR_CREATION_SUCCED = 2;
	public final int OK_OUVRAGE_CREATION_SUCCED = 3;
	
	public final int SEARCH_BY_TITRE_OUVRAGE = 1;
	public final int SEARCH_BY_NOM_AUTEUR = 2;
	public final int SEARCH_BY_NOM_EDITEUR = 3;
	public final int SEARCH_BY_NOM_IMPRIMEUR = 4;
	

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private final String BDD_SERVER = "localhost";
	private final String BDD_USER = "root";
	private final String BDD_PWD = "";
	public static String callingWindowLink;
	public static String callingWindowTitle;
	
	

	//Fonction connexion base de donnée
	// retourne l'objet connection avec la chaine connecionn au serveur le mot de passe et l'utilisateur root
	/**
	 * méthode qui lance la connexion à la base de donnée
	 * @return con : connexion à la base de donnéees
	 */
	public Connection getConnect() {
		
		try {
			//1) Chargement du Driver MySQL
			Class.forName("com.mysql.jdbc.Driver");
			//2) Creer la connexion			
			this.con = DriverManager.getConnection("jdbc:mysql://"+ BDD_SERVER +"/logiciel", BDD_USER, BDD_PWD);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			e.getCause();
		}
		
		return con;
}
	// Fermeture  la connexion
	/**
	 * méthode qui permet juste la fermeture de la connexion
	 */
	public void closeConnection() {
		try {
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Vérification que l'utilisateur a bien un compte crée dans la base de données
	// et si c'est le cas récupère l'identifiant utilisateur userID pour l'utiliser dans les connexions aux tables de la base de données
	/**
	 * Méthode qui vérifie si l'utilisateur a fournit une adresse email 
	 * et un mot de passe existant dans la BDD
	 * @param mail
	 * @param password
	 * @return true si login existant, false si login n'existe pas.
	 */
	public boolean loginCheck(String mail, String password) {
		boolean res = false;	
		Connection con = this.getConnect();
		String sql_checkLogin = "SELECT COUNT(*), idUtilisateur FROM utilisateur WHERE email LIKE '" + mail +"' AND password LIKE '" + password + "'";
		try {
			Statement stmt = con.createStatement();
			ResultSet reqCheckLogin = stmt.executeQuery(sql_checkLogin);
			while(reqCheckLogin.next()) {
				if(reqCheckLogin.getInt(1) == 1) {
					userID = reqCheckLogin.getInt(2);
					res = true;					
				}else {
					res = false;
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return res;
	}
	
	
	//Fonction pour enregistrer un utilisateur dans la bdd
	/**
	 * Méthode qui ajoute à la base de données les informations fournies 
	 * par l'utilisateur lorsqu'il s'inscrit. 
	 * @param email
	 * @param password
	 * @param prenom
	 * @param nom
	 * @return l'etat de reussite de l'inscription 
	 * @throws Exception
	 */
	public int inscriptionUtilisateur(String email, String password, String prenom, String nom) throws Exception {	
		int res = ERR_USER_CREATION_FAILED;
		Connection con = this.getConnect();
		String sql_checkUserExist = "SELECT idUtilisateur FROM utilisateur WHERE email LIKE '" + email +"'";
		String sql_userCreation = " INSERT INTO utilisateur (nom, prenom, email, password) VALUES ('" + nom + "', '" + prenom +"', '"+ email +"', '"+password+"')";

			Statement stmt = con.createStatement();
			ResultSet rsCheckLogin = stmt.executeQuery(sql_checkUserExist);
			if (rsCheckLogin.next())
			{
				String msg = "rien";
				res = ERR_USER_ALREADY_EXISTS;
			}
			else
			{
				int res_insert = stmt.executeUpdate(sql_userCreation);
				res = USER_CREATION_SUCCED;	
			}
		return res;
	}
	
	// Ouverture d'une JFrame Java 
	/**
	 * méthode qui ouvre une fenetre 
	 * @param event
	 * @param link : chemin d'arrivée
	 * @param windowTitle : titre de la fenêtre
	 */
	public void openWindow(ActionEvent event, String link, String windowTitle) {
		// TODO Auto-generated method stub
		try {
			FXMLLoader loader  = new FXMLLoader();
			loader.setLocation(getClass().getResource(link));
			Parent prt = loader.load();
			Scene scene = new Scene(prt);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.setTitle(windowTitle);
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Liste des ouvrages de l'utilisateur connecté
	// La procédure retourne un objet list de type ObservableList<mesOuvrages>
	/**
	 * Méthode qui récupere le titre et l'idOuvrage de l'utilisateur
	 * grace a la connexion a la BDD.
	 * @return la liste des ouvrages
	 */
	public ObservableList<mesOuvrages> getMesOuvrages(){
		Connection con = this.getConnect();
		ObservableList<mesOuvrages> list = FXCollections.observableArrayList();
		String sql_mesOuvrages = "SELECT idOuvrage, titre FROM ouvrage WHERE idUtilisateur = " + userID ;
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rsMesOuvrages = stmt.executeQuery(sql_mesOuvrages);
				if (rsMesOuvrages.next())
				{
					do {
						list.add(new mesOuvrages(rsMesOuvrages.getInt(1),rsMesOuvrages.getString(2)));
						
					} while(rsMesOuvrages.next());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return list;
	}

	
	
	/**
	 * méthode qui récupère toutes les informations d'un ouvrage
	 * grace a la connexion a la bdd
	 * @param idOuvrageSelected
	 * @return le détail d'un ouvrage
	 */
	public ResultSet getUserOuvragesDetails(int idOuvrageSelected) {
		Connection con = glconnect.getConnect();
		ResultSet rsLabels = null;
		// initialisation des prenoms et noms du user connecté
		String sql_getLabels = "SELECT Trs.nom 'Nom_Transcripteur', Trs.prenom 'Prenom_Transcripteur',\r\n"
				+ "       O.idOuvrage, O.titre  'Titre_Ouvrage',  O.nbDePage 'Nb_Pages_Ouvrage', O.dateSortie 'Date_Sortie_Ouvrage',O.commentaire 'Commentaire',\r\n"
				+ "       A.nom 'Nom_Auteur', A.prenom 'Prenom_Auteur',\r\n"
				+ "       E.nom 'Nom_Editeur', E.adresse 'Adresse_Editeur', E.tel 'Tel_Editeur', \r\n"
				+ "       Imp.nom 'Nom_Imprimeur', Imp.adresse 'Adresse_Imprimeur', Imp.tel 'Tel_Imprimeur',\r\n"
				+ "       Pges.Rang 'rang_page' , Pges.lien_fichierOrigine 'lien_Original'\r\n"
				+ "       \r\n"
				+ "FROM ouvrage O Inner Join Auteur A On O.idAuteur = A.idAuteur\r\n"
				+ "               Left Join editeur E on O.idEditeur = E.idEditeur\r\n"
				+ "               Left Join imprimeur Imp on O.idImprimeur = Imp.idImprimeur\r\n"
				+ "               Inner Join utilisateur Trs On O.idUtilisateur = Trs.idUtilisateur\r\n"
				+ "               Left Join page Pges On O.idOuvrage = Pges.idOuvrage\r\n"
				+ "WHERE O.idUtilisateur = " + userID +"\r\n"
				+ "AND O.idOuvrage = " + idOuvrageSelected;
		try {
			Statement stmt = con.createStatement();
			rsLabels = stmt.executeQuery(sql_getLabels);
						
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsLabels;

	}


    // retourne un recordset avec le nom et le prénom de l'utilisateur connécté
	/**
	 * méthode qui permet de récuperer le nom et le prénom de 
	 * l'utilisateur via l'idUtilisateur.
	 * @return
	 */
	public ResultSet  getUserFirstAndLastName() {
		Connection con = glconnect.getConnect();
		ResultSet rs_FirstLastName = null;
		String sql_getNames = "SELECT prenom, nom FROM utilisateur WHERE idUtilisateur = " + userID;
		try {
			Statement stmt = con.createStatement();
			rs_FirstLastName = stmt.executeQuery(sql_getNames);		
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
        }
		return rs_FirstLastName;
    }
    
    
    // Recueil informations nouvel ouvrage pour l'ajouter a la bdd
    /**
     * méthode qui vérifie si l'auteur précisé par 
     * l'utilisateur existe dans notre base de données 
     * @param nomAuteur
     * @param prenomAuteur
     * @return 0 si non, si oui l'idAueur
     * @throws Exception
     */
	public int checkAuteurExist( String nomAuteur, String prenomAuteur) throws Exception {	
		int res = 0;
		Connection con = this.getConnect();
		String sql_checkAuteurExist = "SELECT idAuteur FROM auteur WHERE nom = '" + nomAuteur + "' AND prenom = '" + prenomAuteur +"'" ;
		Statement stmt = con.createStatement();
		ResultSet rsCheckAuteur = stmt.executeQuery(sql_checkAuteurExist);
		if (rsCheckAuteur.next())
		{
				res =  	rsCheckAuteur.getInt(1);			
		}
		return res;
    }
    
	    /*

    // Recueil informations nouvel ouvrage pour l'ajouter a la bdd
    public int addInfosOuvrageBDD(String titre, Date dateSortie, String nomAuteur, String prenomAuteur, String commentaire) throws Exception {	
		int res = ERR_CREATION_FAILED;
		Connection con = this.getConnect();
		String sql_checkOuvrageAuteurExist = "SELECT O.titre, A.nom FROM ouvrage O INNER JOIN auteur A ON O.idAuteur = A.idAuteur WHERE O.titre = '" + titre +"' AND A.nom = '" + nomAuteur;
		String sql_checkAuteurExist = "SELECT nom, prenom FROM auteur WHERE nom = '" + nomAuteur + "' AND prenom = '" + prenomAuteur +"'" ;
		String sql_auteurCreation = " INSERT INTO ouvrage (nom, prenom) VALUES ('" + nomAuteur + "', '" + prenomAuteur +"')";
		String sql_ouvrageCreation = " INSERT INTO ouvrage (titre, dateSortie, commentaire) VALUES ('" + titre + "', '" + dateSortie +"', '"+ commentaire +"')";
		
			Statement stmt = con.createStatement();
			ResultSet rsCheckAuteur = stmt.executeQuery(sql_checkAuteurExist);
			if (rsCheckAuteur.next())
			{
				String msg = "auteur existe deja";
				res = "AUTEUR_EXISTE";
				
			}
			else
			{
				int res_insert = stmt.executeUpdate(sql_auteurCreation);
				
				ResultSet rsCheckOuvrageAuteur = stmt.executeQuery(sql_checkOuvrageAuteurExist);
				if (rsCheckAuteur.next())
				{
					String msg = "cet ouvrage existe deja";
				}
				else
				{
					int res_insertOuvrage = stmt.executeUpdate(sql_ouvrageCreation);
					//int res_insert = stmt.executeUpdate(sql_userCreation);
					
				}
			}
		return res;
	}
    */
	/**
	 * Méthode qui créé l'auteur selon les informations fournies
	 * par l'utilisateur
	 * @param nomAuteur
	 * @param prenomAuteur
	 * @return
	 */
	public int createAuteur(String nomAuteur, String prenomAuteur) {
		// TODO Auto-generated method stub
		int res_CreateAuteur = 0;
		Connection con = this.getConnect();
		String sql_createAuteur = " INSERT INTO auteur (nom, prenom) VALUES ('" + nomAuteur + "', '" + prenomAuteur +"')";
		Statement stmt;
		try {
			stmt = con.createStatement();
			res_CreateAuteur = stmt.executeUpdate(sql_createAuteur);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res_CreateAuteur;
	}

	/**
	 * Créer des pop up, des messages box
	 * @param titre
	 * @param warning
	 * @param question
	 * @return true si l'utilisateur clique sur Ok, false sinon
	 */
    public boolean messageBox(String titre, String warning, String question) {
    	boolean resultat = false;
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle(titre);
    	alert.setHeaderText(warning);
    	alert.setResizable(false);
    	alert.setContentText(question);

    	Optional<ButtonType> result = alert.showAndWait();
    	ButtonType button = result.orElse(ButtonType.CANCEL);

    	if (button == ButtonType.OK) {
    	    //System.out.println("Ok pressed");
    	    resultat = true;
    	} else {
    	    //System.out.println("canceled");
    	    resultat = false;
    	}
    	return resultat;
    }
    
    /**
     * Généralisation de la méthode messageBox
     * @param titre
     * @param warning
     * @param type
     */
    public void messageBoxAlert(String titre, String warning, AlertType type) {
    	
    	Alert alert = new Alert(type);
    	alert.setTitle(titre);
    	alert.setHeaderText(warning);
    	alert.setResizable(false);
    	//alert.setContentText(question);

    	alert.showAndWait();
    }
   
    /**
     * Vérifie si un ouvrage existe selon le titre de l'ouvrage et le nom de 
     * l'auteur fournit
     * @param titreOuvrage
     * @param nomAuteur
     * @return l'idOuvrage
     */
	public int checkOuvrageExiste(String titreOuvrage, String nomAuteur) {
		// TODO Auto-generated method stub
		int res = 0;
		Connection con = this.getConnect();
		String sql_checkOuvrageExist = "SELECT idOuvrage FROM ouvrage O INNER JOIN auteur A ON O.idAuteur = A.idAuteur WHERE O.titre = '" + titreOuvrage +"' AND A.nom = '" + nomAuteur+"'" ;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rsCheckOuvrage = stmt.executeQuery(sql_checkOuvrageExist);
			if (rsCheckOuvrage.next())
			{
					res =  	rsCheckOuvrage.getInt(1);			
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

    /**
     * Méthode qui créer l'ouvrage avec les paramètres fournis par l'utilisateur
     * @param titre
     * @param dateSortie
     * @param commentaire
     * @param idAuteur
     * @param idUtilisateur
     */
	public void createOuvrage(String titre, LocalDate dateSortie, String commentaire, int idAuteur, int idUtilisateur) {
		
    	//SimpleDateFormat formater = null;
	    //formater = new SimpleDateFormat("yy-MM-dd");
	
	    int res_createOuvrage = 0;
		Connection con = this.getConnect();
		String sql_createOuvrage = " INSERT INTO ouvrage (titre, dateSortie, commentaire, idAuteur, idUtilisateur) "
				+ "VALUES ('" + titre + "', '" + dateSortie +"', '"+ commentaire +"', '"+ idAuteur +"' ,'" + idUtilisateur + "')";	
		Statement stmt;
		try {
			stmt = con.createStatement();
			res_createOuvrage = stmt.executeUpdate(sql_createOuvrage);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * méthode qui permet de récuperer l'idAuteur dont le nom et le prénom
	 * a été fourni par l'utilisateur.
	 * @param nomAuteur
	 * @param prenomAuteur
	 * @return idAuteur
	 */
	public int getIDauteur(String nomAuteur, String prenomAuteur) {
		// TODO Auto-generated method stub
		int idAuteur = 0;
		Connection con = this.getConnect();
		String sql_GetIdAuteur = "SELECT idAuteur FROM auteur WHERE nom = '" + nomAuteur + "' AND prenom = '" + prenomAuteur +"'" ;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdAuteur = stmt.executeQuery(sql_GetIdAuteur);
			if (res_getIdAuteur.next()) {
				idAuteur = res_getIdAuteur.getInt(1);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idAuteur;
	}

	/**
	 * Méthode qui permet de fournir la liste des ouvrage qui
	 * correspondent à la recherche de l'utilisateur
	 * @param rechercheUtilisateur
	 * @param searchBy
	 * @return la liste de résultat
	 */
	public ObservableList<mesResultats> rechercheOuvrage(String rechercheUtilisateur, int searchBy) {
		int idOuvrage = 0;
		curSearchBy = searchBy;
		ObservableList<mesResultats> list = FXCollections.observableArrayList();
		String titre;
		String auteur;
		String editeur;
		String imprimeur;
		Connection con = this.getConnect();
		String sql_GetOuvrage = "SELECT  DISTINCT O.titre 'titre_Ouvrage', A.nom 'nom_Auteur', E.nom 'nom_Editeur' , I.nom 'nom_Imprimeur' , O.idOuvrage "
				+ " FROM ouvrage O "
				+ " INNER JOIN auteur A ON O.idAuteur = A.idAuteur "
				+ " INNER JOIN editeur E ON O.idEditeur = E.idEditeur "
				+ "  INNER JOIN imprimeur I ON O.idImprimeur = I.idImprimeur ";
		String sql_GetOuvrageByTitreOuvrage = sql_GetOuvrage + " WHERE O.titre LIKE '%" + rechercheUtilisateur + "%'" ;
		String sql_GetOuvrageByNomAuteur = sql_GetOuvrage + " WHERE A.nom LIKE '%" + rechercheUtilisateur + "%'" ;
		String sql_GetOuvrageByNomEditeur = sql_GetOuvrage + " WHERE E.nom LIKE '%" + rechercheUtilisateur + "%'" ;
		String sql_GetOuvrageByNomImprimeur = sql_GetOuvrage + " WHERE I.nom LIKE '%" + rechercheUtilisateur + "%'" ;
		Statement stmt;
		ResultSet res_getOuvrage=null;
		try {
			stmt = con.createStatement();
			
			if (searchBy ==  SEARCH_BY_TITRE_OUVRAGE) {
				res_getOuvrage = stmt.executeQuery(sql_GetOuvrageByTitreOuvrage);
			}
			if (searchBy == SEARCH_BY_NOM_AUTEUR) {
				res_getOuvrage = stmt.executeQuery(sql_GetOuvrageByNomAuteur);
			}
			if (searchBy == SEARCH_BY_NOM_EDITEUR) {
				res_getOuvrage = stmt.executeQuery(sql_GetOuvrageByNomEditeur);
			}
			if (searchBy == SEARCH_BY_NOM_IMPRIMEUR) {
				res_getOuvrage = stmt.executeQuery(sql_GetOuvrageByNomImprimeur);
			}
			if (res_getOuvrage.next()) {
				
				
				do {
					titre = res_getOuvrage.getString("titre_Ouvrage");
					auteur = res_getOuvrage.getString("nom_Auteur");
					editeur = res_getOuvrage.getString("nom_Editeur");
					imprimeur = res_getOuvrage.getString("nom_Imprimeur");
					idOuvrage = res_getOuvrage.getInt("idOuvrage");
					list.add(new mesResultats ( titre, auteur, editeur, imprimeur));
				}
				while(res_getOuvrage.next());
			}
			curlistMesResultats = list;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return list;
	}


	
	/**
	 * Méthode qui récupere l'idOuvrage dont le titre et 
	 * l'auteur a été précisé par l'utilisateur
	 * @param titre_Ouvrage
	 * @param nomAuteur
	 * @return idOuvrage
	 */
	public int getIdOuvrage(String titre_Ouvrage, String nomAuteur) {
		// TODO Auto-generated method stub
		int idOuvrage = 0;
		Connection con = this.getConnect();
		String sql_GetIdOuvrage = "SELECT O.idOuvrage FROM ouvrage O, auteur A WHERE O.titre = '" + titre_Ouvrage + "' AND A.nom = '" + nomAuteur + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdOuvrage = stmt.executeQuery(sql_GetIdOuvrage);
			if (res_getIdOuvrage.next()) {
				idOuvrage = res_getIdOuvrage.getInt(1);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		curOuvrageId = idOuvrage;
		return idOuvrage;
	}


	/**
	 * méthode qui met à jour la bdd en modifiant
	 * la table ouvrage en lui ajoutant un editeur et un imprimeur
	 * @param idEditeur
	 * @param idImprimeur
	 * @param idOuvrage
	 */
	public void updateOuvrageEditeurImprimeurIds(int idEditeur, int idImprimeur, int idOuvrage) {

	    int res_updateOuvrageEditeurImprimeurIds = 0;
		Connection con = this.getConnect();
		String sql_updateOuvrageEditeurImprimeurIds = "UPDATE ouvrage SET idImprimeur = " + idImprimeur + ", idEditeur = " + idEditeur + " WHERE ouvrage.idOuvrage = " +idOuvrage;	
		Statement stmt;
		try {
			stmt = con.createStatement();
			res_updateOuvrageEditeurImprimeurIds = stmt.executeUpdate(sql_updateOuvrageEditeurImprimeurIds);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	/**
	 * méthode qui vérifie que l'éditeur existe
	 * @param nomEditeur
	 * @return 0 si non, l'idEditeur si oui
	 */
	public int checkEditeurExist(String nomEditeur) {
		// TODO Auto-generated method stub
		int res = 0;
		Connection con = this.getConnect();
		String sql_checkEditeurExist = "SELECT idEditeur FROM editeur WHERE nom = '" + nomEditeur+ "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rsCheckEditeur = stmt.executeQuery(sql_checkEditeurExist);
			if (rsCheckEditeur.next())
			{
					res =  	rsCheckEditeur.getInt(1);			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Méthode qui verifie si un imprimeur existe
	 * @param nomImprimeur
	 * @return 0 si non, idImprimeur si oui
	 */
	public int checkImprimeurExist(String nomImprimeur) {
		// TODO Auto-generated method stub
		int res = 0;
		Connection con = this.getConnect();
		String sql_checkImprimeurExist = "SELECT idImprimeur FROM imprimeur WHERE nom = '" + nomImprimeur + "'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rsCheckImprimeur = stmt.executeQuery(sql_checkImprimeurExist);
			if (rsCheckImprimeur.next())
			{
					res =  	rsCheckImprimeur.getInt(1);			
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * méthode qui créer un nouvel éditeur
	 * @param nomEditeur
	 * @param adresseEditeur
	 * @param telEditeur
	 * @return
	 */
	public int createEditeur(String nomEditeur, String adresseEditeur, String telEditeur) {
				int res_CreateEditeur = 0;
				Connection con = this.getConnect();
				String sql_createEditeur = " INSERT INTO editeur (nom, adresse, tel) VALUES ('" + nomEditeur + "', '" + adresseEditeur +"', '" + telEditeur +"')";
				Statement stmt;
				try {
					stmt = con.createStatement();
					res_CreateEditeur = stmt.executeUpdate(sql_createEditeur);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res_CreateEditeur;
	}

	/**
	 * méthode qui créer un nouvel imprimeur
	 * @param nomImprimeur
	 * @param adresseImprimeur
	 * @param telImprimeur
	 * @return
	 */
	public int createImprimeur(String nomImprimeur, String adresseImprimeur, String telImprimeur) {
		int res_CreateImprimeur = 0;
		Connection con = this.getConnect();
		String sql_createImprimeur = " INSERT INTO imprimeur (nom, adresse, tel) VALUES ('" + nomImprimeur + "', '" + adresseImprimeur +"', '" + telImprimeur +"')";
		Statement stmt;
		try {
			stmt = con.createStatement();
			res_CreateImprimeur = stmt.executeUpdate(sql_createImprimeur);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res_CreateImprimeur;
	}
	
	
	/**
	 * méthode qui récupere l'idEditeur
	 * @param nomEditeur
	 * @return idEditeur
	 */
	public int getIdEditeur(String nomEditeur) {
		Connection con = this.getConnect();
		int idEditeur = 0;
		String sql_getIdEditeur = " SELECT idEditeur FROM editeur WHERE nom = '" + nomEditeur +"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdEditeur = stmt.executeQuery(sql_getIdEditeur);
			if (res_getIdEditeur.next()) {
				idEditeur = res_getIdEditeur.getInt(1);
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idEditeur;
	
	}
	
	
	/**
	 * méthode qui récupere l'idImprimeur
	 * @param nomImprimeur
	 * @return idImprimeur
	 */
	public int getIdImprimeur(String nomImprimeur) {
		// TODO Auto-generated method stub
		Connection con = this.getConnect();
		int idImprimeur = 0;
		String sql_getIdImprimeur = " SELECT idImprimeur FROM imprimeur WHERE nom = '" + nomImprimeur +"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdImprimeur = stmt.executeQuery(sql_getIdImprimeur);
			if (res_getIdImprimeur.next()) {
				idImprimeur = res_getIdImprimeur.getInt(1);
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idImprimeur;
	}
	
	/**
	 * méthode qui permet de supprimer un ouvrage dont l'idOuvrage est l'idOuvrage 
	 * de l'ouvrage selectionné
	 * @param curOuvrageId2
	 */
	public void deleteOuvrage(int curOuvrageId2) {
		// TODO Auto-generated method stub
		Connection con = this.getConnect();
		String sql_delOuvrage = " DELETE FROM ouvrage WHERE idOuvrage = " + curOuvrageId;
		Statement stmt;
		try {
			stmt = con.createStatement();
			int res_delOuvrage = stmt.executeUpdate(sql_delOuvrage);
			}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Méthode qui met à jour la table ouvrage en changeant le titre
	 * sa date et son commentaire
	 * @param titreOuvrage
	 * @param dateOuvrage
	 * @param commentaire
	 */
	public void modifyOuvrage(String titreOuvrage, String dateOuvrage, String commentaire) {
	  String sqlModifyOuvrage = " Update Ouvrage Set " + IIF(titreOuvrage != "", " titre = '" + titreOuvrage.replace("'", "''") + "'","") 
	  + IIF(dateOuvrage != "" && titreOuvrage !="", "," ,"")  + IIF(dateOuvrage != "", " dateSortie = '" + dateOuvrage +"'","")
	  + IIF((dateOuvrage != "" || titreOuvrage !="")&& commentaire != "", "," ,"") + IIF(commentaire != "", " commentaire = '" + commentaire.replace("'", "''") + "'","") + " WHERE idOuvrage = " + curOuvrageId;
	  Connection con = this.getConnect();
	  Statement stmt;
	  try {
		  	stmt = con.createStatement();
			int res_ModifyOuvrage = stmt.executeUpdate(sqlModifyOuvrage);
			}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode qui modifie l'idAuteur de la table Ouvrage dont 
	 * l'idOuvrage est celui de l'ouvrage sélectionné
	 * @param prenomAuteur
	 * @param nomAuteur
	 */
	public void modifyAuteur(String prenomAuteur, String nomAuteur) {
		String sqlGetIdAuteur = "SELECT idAuteur FROM auteur WHERE nom = '" + nomAuteur + "' AND prenom ='" + prenomAuteur + "'";
		int idAuteur = 0;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdAuteur = stmt.executeQuery(sqlGetIdAuteur);
			if (res_getIdAuteur.next()) {
				idAuteur = res_getIdAuteur.getInt(1);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlModifyAuteur = " Update Ouvrage Set idAuteur = " + idAuteur + " WHERE idOuvrage = " + curOuvrageId;
		Connection con = this.getConnect();
		try {
		  	stmt = con.createStatement();
			int res_ModifyAuteur = stmt.executeUpdate(sqlModifyAuteur);
			}
		catch (SQLException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	
	/**
	 * Méthode qui modifie l'idEditeur de la table Ouvrage dont 
	 * l'idOuvrage est celui de l'ouvrage sélectionné
	 * @param nomEditeur
	 * @param adresseEditeur
	 * @param telEditeur
	 */
	public void modifyEditeur(String nomEditeur, String adresseEditeur, String telEditeur) {
		// TODO Auto-generated method stub
		String sqlGetIdEditeur = "SELECT idEditeur FROM editeur WHERE nom = '" + nomEditeur + "'";
		int idEditeur = 0;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdEditeur = stmt.executeQuery(sqlGetIdEditeur);
			if (res_getIdEditeur.next()) {
				idEditeur = res_getIdEditeur.getInt(1);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlModifyEditeur = " Update Ouvrage Set idEditeur = " + idEditeur + " WHERE idOuvrage = " + curOuvrageId;
		Connection con = this.getConnect();
		try {
		  	stmt = con.createStatement();
			int res_ModifyEditeur = stmt.executeUpdate(sqlModifyEditeur);
			}
		catch (SQLException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
	}

	/**
 	 * Méthode qui modifie l'idImprimeur de la table Ouvrage dont 
	 * l'idOuvrage est celui de l'ouvrage sélectionné
	 * @param nomImprimeur
	 * @param adresseImprimeur
	 * @param telImprimeur
	 */
	public void modifyImprimeur(String nomImprimeur, String adresseImprimeur, String telImprimeur) {
		// TODO Auto-generated method stub
		String sqlGetIdImprimeur = "SELECT idImprimeur FROM imprimeur WHERE nom = '" + nomImprimeur + "'";
		int idImprimeur = 0;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet res_getIdImprimeur = stmt.executeQuery(sqlGetIdImprimeur);
			if (res_getIdImprimeur.next()) {
				idImprimeur = res_getIdImprimeur.getInt(1);
				}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlModifyImprimeur = " Update Ouvrage Set idImprimeur = " + idImprimeur + " WHERE idOuvrage = " + curOuvrageId;
		Connection con = this.getConnect();
		try {
		  	stmt = con.createStatement();
			int res_ModifyImprimeur = stmt.executeUpdate(sqlModifyImprimeur);
			}
		catch (SQLException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
	}



	private  String IIF(boolean test, String valueIfTrue, String valueIfFalse) {
		String res = valueIfTrue;
		if (! test) res = valueIfFalse;
		return res;
	}
	
	// Initialisation des contrôles labels dans la JFrame acceuil des ouvrahes de l'utilisateur connécté
	/**
	 * Méthode qui récupère le nom et le prenom de l'utilisateur 
	 * connecté
	 * @param lbl_nom
	 * @param lbl_prenom
	 */
	public static void init_Labels_nom_prenom( Label lbl_nom, Label lbl_prenom ) {
	 		try {
	 			ResultSet rsFirstAndLastName = globalCnx.glconnect.getUserFirstAndLastName();
	 			while(rsFirstAndLastName.next())
	 			{
	 				String nom = rsFirstAndLastName.getString(2);
	 				String prenom = rsFirstAndLastName.getString(1);
	 				lbl_nom.setText(nom);
	 				lbl_prenom.setText(prenom);
	 			}			
	 		} catch (SQLException e) {
	 			e.printStackTrace();
	 		}

	 	}
	 	
	/**
	 * Méthode qui récupere le titre de l'ouvrage sélectionne 
	 * @param idOuvrage
	 * @return titre ouvrage
	 */
	public String init_titreOuvrage( int idOuvrage) {
 			String sqlGetTitreOuvrage = "SELECT titre FROM ouvrage WHERE idOuvrage = " + idOuvrage;
 			String titre = "";
 			Statement stmt;
 			Connection con = this.getConnect();
 			try {
 				stmt = con.createStatement();
 				ResultSet res_getTitre = stmt.executeQuery(sqlGetTitreOuvrage);
 				if (res_getTitre.next()) {
 					titre = res_getTitre.getString(1);
 					}
 				} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			return titre;
	 	}
	
	/**
	 * Méthode qui supprime les pages sélectionnee
	 * @param numPagesToDelete
	 */
	public void deletePages(String numPagesToDelete) {
			String sqlDeletePages = " DELETE From page Where NumeroDePage in (" + numPagesToDelete  + ")  AND idOuvrage = " + curOuvrageId;
			String sqlNewNumberOfPages = " Select COUNT(*) From Page Where idOuvrage = " + curOuvrageId;
			

			Connection con = this.getConnect();
			try {
			  	   Statement stmt = con.createStatement();
				   stmt.executeUpdate(sqlDeletePages);
				   ResultSet res_getNbrePages = stmt.executeQuery(sqlNewNumberOfPages);
	 				if (res_getNbrePages.next()) {
	 					int NumberOfPages = res_getNbrePages.getInt(1);
	 					String sqlUpdateOuvrage = " Update Ouvrage set nbDePage = " + NumberOfPages+ " where idOuvrage =  " + curOuvrageId;
	 					stmt.executeUpdate(sqlUpdateOuvrage);
	 					
	 					}
				    
				}
			catch (SQLException e) 
				{
				   e.printStackTrace();
				}
		}
	
	
	/**
	 * Méthode qui ajoute la nouvelle page chargée par l'utilisateur via son chemin
	 * @param numPageToAdd
	 * @param sourcePath
	 */
	public void addNewPage(String numPageToAdd, String sourcePath) {
			sourcePath = sourcePath.replace("\\", "\\\\");
			String sqlAddPages = " INSERT INTO page (NumeroDePage, idOuvrage, lien_fichierOrigine) VALUES (" + numPageToAdd + ", " + curOuvrageId + ", '" + sourcePath +"')";
			String sqlUpdateNbPage = " UPDATE ouvrage SET nbDePage = nbDePage + 1 WHERE idOuvrage = " + curOuvrageId;
			Connection con = this.getConnect();
			try {
			  	   Statement stmt = con.createStatement();
				   stmt.executeUpdate(sqlAddPages);
				   stmt.executeUpdate(sqlUpdateNbPage);
				}
			catch (SQLException e) 
				{
				   e.printStackTrace();
				}
			
		}
		
	
	
	/**
	 * Vérifie si une page existe déjà
	 * @param numPage
	 * @return true si page existante, false sinon
	 */
	public boolean checkIfPageExists(String numPage) {
			boolean check = false;
			Connection con = this.getConnect();
			String sql_checkPageExist = "SELECT Rang FROM page WHERE numeroDePage = '" + numPage + "' AND idOuvrage = '" + curOuvrageId +"'" ;
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rsCheckPage = stmt.executeQuery(sql_checkPageExist);
				if (rsCheckPage.next())
				{
						check =  true;			
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return check;
	    }
		
	/**
	 * Met a jour la table page en changeant le lien_fichierOrigine
	 * @param numPage
	 * @param name
	 */
	 public void updatePage(String numPage, String name) {
			String idPage = "";
			String getIdPage = "SELECT Rang FROM page WHERE numeroDePage = " + numPage + " AND idOuvrage = " + curOuvrageId;
			//String updatePage = "UPDATE page SET lien_fichierOrigine = " + name + " WHERE Page = " + idPage;
			Connection con = this.getConnect();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rsgetIdPage = stmt.executeQuery(getIdPage);
				if (rsgetIdPage.next()) {
				   idPage = rsgetIdPage.getString(1);
				    String updatePage = "UPDATE page SET lien_fichierOrigine = " + name + " WHERE Page = " + idPage;
				    stmt.executeUpdate(updatePage);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	 
	 /**
	  * Met a jour la table ouvrage en lui changeant son idEditeur
	  * @param nomEditeur
	  */
	 public void updateEditeurIdInOuvrage(String nomEditeur) {
			// TODO Auto-generated method stub
			int idEditeur = 0;
			idEditeur = getIdEditeur(nomEditeur);
			String sql_updateEditeurIdInOuvrage = "UPDATE ouvrage SET idEditeur = " + idEditeur + " "
					+ " WHERE idOuvrage = " + curOuvrageId;
			Connection con = this.getConnect();
			Statement stmt;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(sql_updateEditeurIdInOuvrage);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	 /**
 	  * Met a jour la table ouvrage en lui changeant son idImprimeur
	  * @param nomImprimeur
	  */
	 public void updateImprimeurIdInOuvrage(String nomImprimeur) {
			int idImprimeur = 0;
			idImprimeur = getIdImprimeur(nomImprimeur);
			String sql_updateImprimeurIdInOuvrage = "UPDATE ouvrage SET idImprimeur = " + idImprimeur + " "
												+ " WHERE idOuvrage = " + curOuvrageId;
			Connection con = this.getConnect();
			Statement stmt;
			try {
					stmt = con.createStatement();
					stmt.executeUpdate(sql_updateImprimeurIdInOuvrage);
				}
			catch (SQLException e) {
									e.printStackTrace();
									}	
		}
	
	 
	 /**
	  * méthode qui récupere le chemin de la page passée en parametre
	  * @param numPage
	  * @return le chemin de la page
	  */
	 public String getPage(String numPage) {
			String pathPage = "";
			
			String sqlGetPage = " SELECT lien_fichierOrigine FROM page WHERE idOuvrage = " + curOuvrageId + " AND numeroDePage = " + numPage; 
			Connection con = this.getConnect();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rsgetPath = stmt.executeQuery(sqlGetPage);
				if (rsgetPath.next()) {
					pathPage = rsgetPath.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pathPage;
		}
			















}
