/**
 * 
 */
package es.ubu.lsi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;
import es.ubu.lsi.server.ChatServerImpl;

/**
 * @author Víctor De Marco Velasco
 *
 */
public class ChatClientImpl implements ChatClient,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String server;
	public String username;
	public int port;
	public boolean carryOn = true;
	public int id;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public Socket socket;

	public ChatClientImpl(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}

	@Override
	public boolean start() {
		try {
			socket = new Socket(server,port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			new ChatClientListener().start();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean sendMessage(ChatMessage msg) {
		BufferedReader inUsuario = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(">> Ingrese un mensaje: ");
		try {
			String msn = inUsuario.readLine();
			if(msn.equalsIgnoreCase("exit")) {
				return false;
			}
			msg = new ChatMessage(this.username, ChatMessage.MessageType.MESSAGE, msn);
			System.out.println("El mensaje que vas a enviar es:"+msg);
			System.out.println("El String que vas a enviar es:"+msn);
			oos.writeObject(msg);

		} catch (IOException e) {
			return false;
		}
		// Información de usuarios conectados
		return true;
	}

	@Override
	public void disconnect() {
		System.exit(0);
	}

	public static void main(String[] args) {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			System.out.print(">> Ingrese un nombre de usuario: ");
	 
			ChatClientImpl cliente1 = new ChatClientImpl("localhost", ChatServerImpl.DEFAULT_PORT, entrada.readLine());
			cliente1.start();
		
			while (true) {
				if (cliente1.sendMessage(null)==false)
					break;
			}
		
			entrada.close();
			cliente1.disconnect();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public class ChatClientListener extends Thread {
		public synchronized void run() {
			 while(carryOn){
	                try {
	                    ChatMessage msn = (ChatMessage) ois.readObject();
	                    System.out.println("\n>> "+ username+"----Mensaje:" + msn.getMessage());
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                } catch (ClassNotFoundException ex) {
	                	ex.printStackTrace();
	                }
	            }    

		}
	}

}
