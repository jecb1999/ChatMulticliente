package control;

import comm.Receptor.OnMessageListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import comm.TCPConnection;
import javafx.application.Platform;
import javafx.scene.control.Button;
import model.DirectMessage;
import model.ExitMessage;
import model.Generic;
import model.IdentifyYou;
import model.Message;
import model.NewConnectionMessage;
import model.NotifyClientMessage;
import view.ChatWindow;

public class ChatController implements OnMessageListener {

	private ChatWindow view;
	private TCPConnection connection;
	private ArrayList<Button> buttonOfClients;
	private String id2;

	public ChatController(ChatWindow view) {
		this.view = view;
		init();
	}

	public void init() {
		connection = TCPConnection.getInstance();
		connection.setListenerOfMessages(this);
		buttonOfClients = new ArrayList<>();

		view.getSendBtn().setOnAction((e) -> {
			if (view.getTodos().isDisable() == true) {
				String id = UUID.randomUUID().toString();
				String date = Calendar.getInstance().getTime().toString();
				String msg = view.getMessageTF().getText();
				String sendBy = id2;
				Message msgObj = new Message(id, msg, date, sendBy);
				Gson gson = new Gson();
				String json = gson.toJson(msgObj);

				connection.getEmisor().sendMessage(json);
				view.getMessageTF().setText("");
			} else {
				String id = UUID.randomUUID().toString();
				String date = Calendar.getInstance().getTime().toString();
				String msg = view.getMessageTF().getText();
				String sendBy = id2;
				String clientID = "";
				for (int i = 0; i < buttonOfClients.size(); i++) {
					if (buttonOfClients.get(i).isDisable()) {
						clientID = buttonOfClients.get(i).getText();

					}
				}
				DirectMessage dmsg = new DirectMessage(id, msg, date, clientID, sendBy);
				Gson gson = new Gson();
				String json = gson.toJson(dmsg);

				connection.getEmisor().sendMessage(json);
				view.getMessageTF().setText("");
			}
		});

		view.getTodos().setOnAction(

				(e) -> {
					for (int j = 0; j < buttonOfClients.size(); j++) {
						if (buttonOfClients.get(j).isDisable() == true) {
							buttonOfClients.get(j).setDisable(false);

						}
					}
					view.getTodos().setDisable(true);

				}

		);

		view.setOnCloseRequest(

				(e) -> {
					ExitMessage em = new ExitMessage(String.valueOf(connection.getSocket().getLocalPort()));
					System.out.println(em.getReID());
					Gson gson = new Gson();
					String json = gson.toJson(em);

					connection.getEmisor().sendMessage(json);
					view.close();

				}

		);

	}

	@Override
	public void OnMessage(String msg) {

		Gson gson = new Gson();
		Generic type = gson.fromJson(msg, Generic.class);
		switch (type.getType()) {
		case "Message":
			Platform.runLater(() -> {
				Message msjObj = gson.fromJson(msg, Message.class);
				view.getMessagesArea().appendText(msjObj.getSendBy() + ":" + "" + "\n" + msjObj.getBody() + "\n");
			});
			break;
		case "NC":
			Platform.runLater(() -> {
				NewConnectionMessage msjObj = gson.fromJson(msg, NewConnectionMessage.class);
				Button nc = new Button(msjObj.getIdN());
				nc.setPrefHeight(36);
				nc.setPrefWidth(134);
				nc.setId(msjObj.getIdN());
				nc.setOnAction(

						(e) -> {
							if (view.getTodos().isDisable() == true) {
								view.getTodos().setDisable(false);
							}
							for (int j = 0; j < buttonOfClients.size(); j++) {
								if (buttonOfClients.get(j).isDisable() == true) {
									if (buttonOfClients.get(j) != nc) {
										buttonOfClients.get(j).setDisable(false);
									} else {
										buttonOfClients.get(j).setDisable(true);
									}
								}
							}
							nc.setDisable(true);
						}

				);
				buttonOfClients.add(nc);
				view.getList().getChildren().add(nc);
			});
			break;
		case "Notify client":
			Platform.runLater(() -> {
				NotifyClientMessage msjObj = gson.fromJson(msg, NotifyClientMessage.class);
				String[] list = msjObj.getIDNewClient().split(",");
				for (int i = 0; i < list.length; i++) {
					if (list[i].isEmpty() != true) {
						Button nc = new Button(list[i]);
						nc.setPrefHeight(36);
						nc.setPrefWidth(134);
						nc.setId(list[i]);
						nc.setOnAction(

								(e) -> {
									if (view.getTodos().isDisable() == true) {
										view.getTodos().setDisable(false);
									}
									for (int j = 0; j < buttonOfClients.size(); j++) {
										if (buttonOfClients.get(j).isDisable() == true) {
											if (buttonOfClients.get(j) != nc) {
												buttonOfClients.get(j).setDisable(false);
											} else {
												buttonOfClients.get(j).setDisable(true);
											}
										}
									}
									nc.setDisable(true);

								}

						);

						view.getList().getChildren().add(nc);
						buttonOfClients.add(nc);
					}
				}
			});

			break;
		case "identify":
			Platform.runLater(() -> {
				IdentifyYou msjObj = gson.fromJson(msg, IdentifyYou.class);
				Button nc = new Button(msjObj.getForScreen());
				nc.setPrefHeight(36);
				nc.setPrefWidth(134);
				nc.setId(msjObj.getForScreen());
				nc.setOnAction(

						(e) -> {
							if (view.getTodos().isDisable() == true) {
								view.getTodos().setDisable(false);
							}
							for (int j = 0; j < buttonOfClients.size(); j++) {
								if (buttonOfClients.get(j).isDisable() == true) {
									if (buttonOfClients.get(j) != nc) {
										buttonOfClients.get(j).setDisable(false);
									} else {
										buttonOfClients.get(j).setDisable(true);
									}
								}
							}
							nc.setDisable(true);
						}

				);
				buttonOfClients.add(nc);
				id2 = msjObj.getIdYou();
				view.getList().getChildren().add(nc);

			}

			);
			break;
		case "Exit":
			ExitMessage msjObj = gson.fromJson(msg, ExitMessage.class);
			Platform.runLater(() -> {
				for (int i = 0; i < view.getList().getChildren().size(); i++) {
					if (view.getList().getChildren().get(i).getId().equals(msjObj.getReID())) {
						view.getList().getChildren().remove(i);
					}
				}
			});
			break;

		}
	}
}
