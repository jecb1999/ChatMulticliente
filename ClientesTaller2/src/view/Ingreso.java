package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import control.IngresoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Ingreso extends Stage {
	
	private Scene scene;
	private TextField nombreUsuario;
	private Button ingresar;
	private Label label;
	private Label instrucciones;
	private String iid;
	
	private IngresoController control;
	
	
	public Ingreso() {
			AnchorPane ap = new AnchorPane();
			ap.setPrefHeight(400);
			ap.setPrefWidth(251);
			label = new Label("CHAT JECB");
			label.setLayoutX(31);
			label.setLayoutY(32);
			label.setFont(new Font(39));
			instrucciones = new Label("Ingresar usuario");
			instrucciones.setLayoutX(37);
			instrucciones.setLayoutY(140);
			instrucciones.setFont(new Font(25));
			nombreUsuario = new TextField();
			nombreUsuario.setLayoutX(51);
			nombreUsuario.setLayoutY(188);
			ingresar = new Button("Ingresar");
			ingresar.setLayoutX(96);
			ingresar.setLayoutY(341);
			ap.getChildren().add(instrucciones);
			ap.getChildren().add(ingresar);
			ap.getChildren().add(label);
			ap.getChildren().add(nombreUsuario);
			scene = new Scene(ap);
			this.setScene(scene);
			
			control = new IngresoController(this);
		
	}


	public TextField getNombreUsuario() {
		return nombreUsuario;
	}


	public Button getIngresar() {
		return ingresar;
	}
	
	public String getIid() {
		return iid;
	}
	
	public void setIID(String iid) {
		this.iid = iid;
	}
	
	
	


}
