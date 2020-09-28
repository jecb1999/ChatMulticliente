package view;

import control.ChatController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatWindow extends Stage{
	
	
	//UI Elements
	private Scene scene;
	private TextField messageTF;
	private Button sendBtn;
	private TextArea messagesArea;
	private VBox list;
	private Button todos;
	private ChatController contol;

	
	
	public ChatWindow() {
		
		messageTF = new TextField();
		messageTF.setLayoutX(147);
		messageTF.setLayoutY(353);
		messageTF.setPrefHeight(36);
		messageTF.setPrefWidth(388);
		sendBtn = new Button("---->");
		sendBtn.setLayoutX(540);
		sendBtn.setLayoutY(354);
		sendBtn.setPrefHeight(34);
		sendBtn.setPrefWidth(52);
		messagesArea = new TextArea();
		messagesArea.setLayoutX(146);
		messagesArea.setLayoutY(24);
		messagesArea.setPrefHeight(318);
		messagesArea.setPrefWidth(446);
		list = new VBox();
		list.setLayoutY(23);
		list.setPrefHeight(377);
		list.setPrefWidth(134);
		todos = new Button("Todos");
		todos.setPrefHeight(36);
		todos.setPrefWidth(134);
		todos.setDisable(true);
		todos.setId("Todos");
		list.getChildren().add(todos);
		
		AnchorPane ap = new AnchorPane();
		ap.setPrefHeight(400);
		ap.setPrefWidth(600);
		ap.getChildren().add(list);
		ap.getChildren().add(messagesArea);
		ap.getChildren().add(messageTF);
		ap.getChildren().add(sendBtn);
		
		scene = new Scene(ap);
		this.setScene(scene);
		contol = new ChatController(this);
	}



	public TextField getMessageTF() {
		return messageTF;
	}



	public Button getSendBtn() {
		return sendBtn;
	}



	public TextArea getMessagesArea() {
		return messagesArea;
	}
	
	public VBox getList() {
		return list;
	}
	
	public Button getTodos() {
		return todos;
	}

}
