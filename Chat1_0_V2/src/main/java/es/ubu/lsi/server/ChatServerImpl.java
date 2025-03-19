/**
 * 
 */
package es.ubu.lsi.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import es.ubu.lsi.common.ChatMessage;

/**
 * @author Víctor De Marco Velasco
 *
 */
public class ChatServerImpl implements ChatServer {
	public static final int DEFAULT_PORT = 1500;
	public static int clientid;
	public static SimpleDateFormat sdf;
	public int port;
	public boolean alive = true;

	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public Socket socket;
	
	public HashMap<Integer, String> mapa = new HashMap<Integer, String>();
	
	public ChatServerImpl(int port) {
		this.port = port;
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			System.out.println("Escuchando...");
			while (true) {
				Socket cliente = serverSocket.accept();
				System.out.println("Conexión aceptada: " + cliente.getRemoteSocketAddress());
				ServerThreadForClient stfc = new ServerThreadForClient(cliente,cliente.getPort());
				Thread th = new Thread(stfc);
				th.start();
				
			}

		} catch (IOException e) {
			System.out.println("Error al escuchar por el puerto:" + DEFAULT_PORT);
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadcast(ChatMessage message) {

		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		ChatServerImpl server = new ChatServerImpl(DEFAULT_PORT);
		server.startup();

	}

	public class ServerThreadForClient extends Thread {
		public int id;
		public String username;

		public ServerThreadForClient(Socket socket,int id) {
			super();
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(socket.getInputStream());
				this.id = id;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public synchronized void run() {
			while (alive) {
				try {
					ChatMessage msn = (ChatMessage) ois.readObject();
					username = msn.getUsername();
					System.out.println("\n"+username + " Mensaje:" + msn.getMessage());
					if(mapa.get(id)==null) {
					  mapa.put(id, username);
					}
					System.out.println("El usuario es: "+mapa.get(id));
					broadcast(msn);

				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void sendMessage(ChatMessage msg) {
			System.out.print(">> Ingrese un nombre de usuario: ");
			try {
				msg = new ChatMessage(username, ChatMessage.MessageType.MESSAGE, "Hola, deseo conectarme!");
				oos.flush();
				oos.writeObject(msg);
				oos.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Información de usuarios conectados
			System.out.println("_____________________\n\n" + "Usuarios conectados\n---------------------\n");
		}
	}
}
