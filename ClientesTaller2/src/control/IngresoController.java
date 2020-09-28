package control;

import java.util.Calendar;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import javafx.application.Platform;
import comm.TCPConnection;
import model.AccessMessage;
import model.DirectMessage;
import model.Generic;
import model.Message;
import view.ChatWindow;
import view.Ingreso;

public class IngresoController implements OnMessageListener {

	private Ingreso view;
	private TCPConnection connection;
	private String iid;

	public IngresoController(Ingreso view) {
		this.view = view;
		init();
		connection = TCPConnection.getInstance();
		connection.setListenerOfMessages(this);
	}

	public void init() {
		view.getIngresar().setOnAction(

				e -> {
					String id = iid;
					String date = Calendar.getInstance().getTime().toString();
					String msg = view.getNombreUsuario().getText();
					AccessMessage msgObj = new AccessMessage(id, date, msg);
					Gson gson = new Gson();
					String json = gson.toJson(msgObj);
					connection.getEmisor().sendMessage(json);
				}

		);
	}

	@Override
	public void OnMessage(String msg) {

		Gson gson = new Gson();

		Generic type = gson.fromJson(msg, Generic.class);

		switch (type.getType()) {
		case "Message":
			Message m = gson.fromJson(msg, Message.class);
			Message normal = new Message(m.getId(), m.getBody(), m.getDate(), "");
			this.iid = normal.getBody();
			break;
		case "accept":
			Platform.runLater(

					() -> {
						ChatWindow window = new ChatWindow();
						window.show();
						view.close();
					}

			);
			break;
		case "error":
			JOptionPane.showMessageDialog(null, "Usuario repetido", "ERROR", JOptionPane.ERROR_MESSAGE);
			break;
		}

	}
}
