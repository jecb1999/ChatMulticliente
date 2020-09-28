package comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import model.AcceptMesagge;
import model.ErrorMessage;
import model.ExitMessage;
import model.IdentifyYou;
import model.Message;
import model.NewConnectionMessage;
import model.NotifyClientMessage;

public class TCPConnection extends Thread {

	
	private static TCPConnection instance = null;

	private TCPConnection() {
		sessions = new ArrayList<>();
		suplicants = new ArrayList<>();
	}

	public static synchronized TCPConnection getInstance() {
		if (instance == null) {
			instance = new TCPConnection();
		}
		return instance;
	}

	private int puerto;
	private OnConnectionListener connectionListener;
	private OnMessageListener messageListener;
	private ServerSocket server;
	private ArrayList<Session> sessions;
	private ArrayList<Session> suplicants;

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(puerto);

			while (true) {
				System.out.println("Esperando en el puerto " + puerto);
				Socket socket = server.accept();
				System.out.println("socket: "+socket);
				System.out.println("Nuevo cliente conectado");
				String id = UUID.randomUUID().toString();
				Session session = new Session(id, socket);
				suplicants.add(session);
				setAllMessageListenerSuplicants(messageListener);
				String idm = UUID.randomUUID().toString();
				Message msg = new Message(idm, id, Calendar.getInstance().getTime().toString(), "");
				sendIID(id, msg);
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void setAllMessageListenerSuplicants(OnMessageListener listener) {
		for (int i = 0; i < suplicants.size(); i++) {
			Session s = suplicants.get(i);
			s.getReceptor().setListener(listener);
		}

	}

	public void setAllMessageListener(OnMessageListener listener) {
		for (int i = 0; i < sessions.size(); i++) {
			Session s = sessions.get(i);
			s.getReceptor().setListener(listener);
		}
	}

	public void setConnectionListener(OnConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public interface OnConnectionListener {
		public void onConnection(String id);
	}

	public void setMessageListener(OnMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void sendBroadcast(String msg) {

		for (int i = 0; i < sessions.size(); i++) {
			Session s = sessions.get(i);
			s.getEmisor().sendMessage(msg);
		}

	}

	public void sendDirectMessage(String id, String msg) {
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getId().equals(id)) {
				sessions.get(i).getEmisor().sendMessage(msg);
				break;
			}
		}
	}

	public void sendIID(String id, Message msg) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < suplicants.size(); i++) {
			if (suplicants.get(i).getId().equals(id)) {
				Gson gson = new Gson();
				String json = gson.toJson(msg);
				suplicants.get(i).getEmisor().sendMessage(json);
				break;
			}
		}

	}

	public void addToSessions(String id, String body) {
		if (inUse(body) == false) {
			Socket socket = getSocketOfSupplicant(id);
			if (socket != null) {
				Session ns = new Session(body, socket);
				connectionListener.onConnection(body);
				sessions.add(ns);
				setAllMessageListener(messageListener);
				acceptMessage(body);
				putMeInTheList(body);
				notifyConnections(body, listClients(body));
				notifyNewC(body);
			}
		} else {
			errorMesage(id);
		}
	}

	public void errorMesage(String id) {
		for (int i = 0; i < suplicants.size(); i++) {
			if (suplicants.get(i).getId().equals(id)) {
				ErrorMessage em = new ErrorMessage();
				Gson gson = new Gson();
				String msg = gson.toJson(em);
				suplicants.get(i).getEmisor().sendMessage(msg);
			}
		}

	}

	public boolean inUse(String body) {
		boolean ret = false;
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getId().equals(body)) {
				ret = true;
			}
		}
		return ret;
	}

	public Socket getSocketOfSupplicant(String id) {
		Socket s = null;
		for (int i = 0; i < suplicants.size(); i++) {
			if (suplicants.get(i).getId().equals(id)) {
				Socket socket = suplicants.get(i).getSocket();
				suplicants.remove(i);
				s = socket;
			}

		}
		return s;
	}

	public void acceptMessage(String id) {
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getId().equals(id)) {
				AcceptMesagge am = new AcceptMesagge();
				Gson gson = new Gson();
				String msg = gson.toJson(am);
				sessions.get(i).getEmisor().sendMessage(msg);
			}
		}
	}

	public void notifyNewC(String idN) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		NewConnectionMessage nc = new NewConnectionMessage(idN);
		String msg = gson.toJson(nc);
		alertNewClient(idN, msg);
	}

	public void putMeInTheList(String mID) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		IdentifyYou iy = new IdentifyYou(mID, mID + "(Yo)");
		String msg = gson.toJson(iy);
		sendDirectMessage(mID, msg);
	}

	public void notifyConnections(String id, String list) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NotifyClientMessage nc = new NotifyClientMessage(list);
		Gson gson = new Gson();
		String msg = gson.toJson(nc);
		sendDirectMessage(id, msg);
	}

	public String listClients(String myID) {
		String list = "";
		for (int i = 0; i < sessions.size(); i++) {
			if (!sessions.get(i).getId().equals(myID)) {
				list += sessions.get(i).getId() + ",";
			}

		}
		return list;
	}

	public void alertNewClient(String idE, String msg) {
		for (int i = 0; i < sessions.size(); i++) {
			if (!sessions.get(i).getId().equals(idE)) {
				Session s = sessions.get(i);
				s.getEmisor().sendMessage(msg);
			}
		}
	}

	public void eraseClient(String reID) {
		int clientOut = Integer.parseInt(reID);
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getSocket().getPort() == clientOut) {
				String id = sessions.get(i).getId();
				sessions.get(i).getReceptor().setTerminar(true);
				sessions.remove(i);
				ExitMessage em = new ExitMessage(id);
				Gson gson = new Gson();
				String text = gson.toJson(em);
				sendBroadcast(text);
			}
		}
		
	}

}
