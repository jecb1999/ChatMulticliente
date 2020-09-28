package comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import comm.Receptor.OnMessageListener;
import comm.TCPConnection.OnConnectionListener;

public class TCPConnection extends Thread {

	private static TCPConnection instance = null;

	private TCPConnection() {
	}

	public static synchronized TCPConnection getInstance() {

		if (instance == null) {
			instance = new TCPConnection();
		}

		return instance;

	}

	private Socket socket;
	private String ip;
	private int puerto;
	private Receptor receptor;
	private Emisor emisor;
	private OnMessageListener listener;
	private OnConnectionListener connectionListener;

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public void run() {
		try {

			System.out.println("Conectadome al servidor");
			socket = new Socket(ip, puerto);
			System.out.println("socket cliente: "+socket);
			System.out.println("Conectado");

			receptor = new Receptor(socket.getInputStream());
			receptor.start();

			emisor = new Emisor(socket.getOutputStream());
			connectionListener.onConnection();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setListenerOfMessages(OnMessageListener listener) {
		System.out.println("set listener of message");
		if (listener == null) {
			System.out.println("listener null");
		}
		System.out.println("receptor: " + this.receptor);
		this.receptor.setListener(listener);
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setConnectionListener(OnConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public interface OnConnectionListener {
		public void onConnection();
	}
	
	public Socket getSocket() {
		return socket;
	}

}
