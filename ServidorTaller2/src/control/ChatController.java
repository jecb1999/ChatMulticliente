package control;

import comm.Receptor.OnMessageListener;

import java.util.Calendar;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

import comm.TCPConnection;
import comm.TCPConnection.OnConnectionListener;
import javafx.application.Platform;
import model.AccessMessage;
import model.DirectMessage;
import model.ErrorMessage;
import model.ExitMessage;
import model.Generic;
import model.Message;
import view.ChatWindow;

public class ChatController implements OnMessageListener, OnConnectionListener {

	private ChatWindow view;
	private TCPConnection connection;

	public ChatController(ChatWindow view) {
		this.view = view;
		init();
	}

	public void init() {
		connection = TCPConnection.getInstance();
		connection.setPuerto(5000);
		connection.start();
		connection.setConnectionListener(this);
		connection.setMessageListener(this);
	}

	@Override
	public void onConnection(String id) {
		Platform.runLater(

				() -> {
					view.getMessagesArea().appendText("<<< Nuevo cliente conectado " + id + "! >>>\n");
				}

		);

	}

	@Override
	public void OnMessage(String msg) {
		Gson gson = new Gson();
		Generic type = gson.fromJson(msg, Generic.class);

		switch (type.getType()) {
		case "Message":
			connection.sendBroadcast(msg);
			break;
		case "DirectMessage":
			DirectMessage m = gson.fromJson(msg, DirectMessage.class);
			Message normal = new Message(m.getId(), m.getBody(), m.getDate(), m.getSendBy());
			String json = gson.toJson(normal);
			connection.sendDirectMessage(m.getClientId(), json);
			break;
		case "Access":
			AccessMessage am = gson.fromJson(msg, AccessMessage.class);
			connection.addToSessions(am.getId(), am.getBody());
			break;
		case "Exit":
			System.out.println("aqui");
			ExitMessage em = gson.fromJson(msg, ExitMessage.class);
			System.out.println(em.getReID());
			connection.eraseClient(em.getReID());
		}
		Platform.runLater(

				() -> {

					Message msjObj = gson.fromJson(msg, Message.class);
					view.getMessagesArea().appendText("<<< " + msjObj.getDate() + ": " + msjObj.getBody() + "\n");
				}

		);

	}

}
