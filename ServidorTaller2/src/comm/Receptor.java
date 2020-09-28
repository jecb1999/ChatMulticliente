package comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Receptor extends Thread {

	private InputStream is;
	public OnMessageListener listener;
	private boolean terminar;

	public Receptor(InputStream is) {
		this.is = is;
		terminar = false;
	}
	
	public void setTerminar(boolean newTerminar) {
		this.terminar = newTerminar;
	}

	@Override
	public void run() {
		try {

			BufferedReader breader = new BufferedReader(new InputStreamReader(this.is));

			while (!terminar) {
				String msg = breader.readLine();
				if (listener != null)
					listener.OnMessage(msg);
				else
					System.out.println("No hay observer");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("");
		}

	}

	public void setListener(OnMessageListener listener) {
		this.listener = listener;
	}

	public interface OnMessageListener {
		public void OnMessage(String msg);
	}

}
