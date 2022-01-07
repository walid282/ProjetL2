module Zdoug {
	
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
	requires mysql.connector.java;
	requires java.desktop;
	requires API;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
	opens inscription to javafx.graphis, javafx.fxml;
	opens accueil to javafx.graphics, javafx.fxml, javafx.base;
	opens globalConnectionModule to javafx.graphics, javafx.fxml;	
	opens ouvrage to javafx.graphics, javafx.fxml;
	opens transcription to javafx.graphics, javafx.fxml;
	opens rechercheOuvrage to javafx.graphics, javafx.fxml, javafx.base;
	exports rechercheOuvrage to javafx.graphics, javafx.fxml;
	opens modifierOuvrage to javafx.graphics, javafx.fxml, javafx.base;
	opens startTranscription to javafx.graphics, javafx.fxml, javafx.base;

}