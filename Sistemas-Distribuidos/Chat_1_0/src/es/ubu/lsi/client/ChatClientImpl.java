/**
 * 
 */
package es.ubu.lsi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;
import es.ubu.lsi.server.ChatServerImpl;

/**
 * @author Víctor De Marco Velasco
 *
 */
public class ChatClientImpl implements ChatClient {

	public String server;
	public String username;
	public int port;
	public boolean carryOn = true;
	public int id;

	public ChatClientImpl(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}

	@Override
	public boolean start() {

		return false;
	}

	@Override
	public void sendMessge(ChatMessage msg) {
		//ChatServerImpl.broadcast(msg);
	}

	@Override
	public void disconnect() {
		System.exit(0);
	}

	public void main(String[] args) {
		ChatClientImpl cliente1 = new ChatClientImpl("localhost", ChatServerImpl.DEFAULT_PORT, "Victor");
		start();

	}

	public class ChatClientListener implements Runnable {
		public void run() {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				try {
					ChatMessage nuevo = new ChatMessage(id, MessageType.MESSAGE, entrada.readLine());
					sendMessge(nuevo);
					if (nuevo.getMessage().equalsIgnoreCase("exit"))
						break;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			try {
				entrada.close();
				disconnect();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

}
