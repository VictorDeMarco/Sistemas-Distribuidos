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
import java.util.ArrayList;
import java.util.List;
import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

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
	
	public Socket socket;
	private List<ServerThreadForClient> clients = new ArrayList<>();
	
	public ChatServerImpl(int port) {
		this.port = port;
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
		try {
			
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			System.out.println("Escuchando...");
			 new ServerConsoleListener().start();
			while (alive) {
				Socket cliente = serverSocket.accept();
				System.out.println("Conexión aceptada: " + cliente.getRemoteSocketAddress());
				ServerThreadForClient stfc = new ServerThreadForClient(cliente,cliente.getPort());
				clients.add(stfc);
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
		 alive = false;
	        try {
	            for (ServerThreadForClient client : clients) {
	                client.disconnect();
	            }
	            System.exit(0);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public synchronized void broadcast(ChatMessage message,ServerThreadForClient cliente) {
		for (ServerThreadForClient client : clients) {
            if (client != cliente) { // Evitar enviar al remitente original
                client.sendMessage(message,cliente);
            }
          }
		}
		
	@Override
	public synchronized void remove(int id) {
		boolean loc = true;
		for (ServerThreadForClient client : clients) {
            if (client.id == id) { 
            	 client.disconnect();
				 clients.remove(client);
				 loc =false;
				 break;
            }
          }
		if (loc==true) {
			System.out.println("No se encontró un cliente con ID " + id);
		}
       
    }

	public static void main(String[] args) {
		ChatServerImpl server = new ChatServerImpl(DEFAULT_PORT);
		server.startup();

	}
	
	private class ServerConsoleListener extends Thread {
        @Override
        public void run() {
            try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
                while (alive) {
                    String command = consoleReader.readLine();
                    if (command.startsWith("remove ")) {
                        try {
                            int id = Integer.parseInt(command.split(" ")[1]);
                            remove(id);
                        } catch (NumberFormatException e) {
                            System.out.println("Formato incorrecto. Usa: remove <id>");
                        }
                    } else if(command.startsWith("shutdown")) {
                    	 System.out.println("Apagando el servidor");
                    	 shutdown();
                    }else {
                    	 System.out.println("Comando desconocido. Usa: remove <id> o shutdown");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	

	public class ServerThreadForClient extends Thread {
	    public int id;
	    public String username;
	    private Socket socket;
	    private ObjectInputStream ois;
	    private ObjectOutputStream oos;
	    public List<String> banlist = new ArrayList<>();

	    public ServerThreadForClient(Socket socket, int id) {
	        super();
	        this.socket = socket;
	        this.id = id;
	        try {
	            // Asegurar el orden correcto de creación de streams
	            oos = new ObjectOutputStream(socket.getOutputStream());
	            oos.flush();
	            ois = new ObjectInputStream(socket.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


		public synchronized void run() {
	        while (true) {
	            try {
	                Object prueba = ois.readObject();
	                //System.out.println("El objeto es: " + prueba.getClass().getName());

	                if (prueba instanceof ChatMessage) {
	                    ChatMessage msn = (ChatMessage) prueba;
	                    username = msn.getUsername();
	                    if (msn.getType()== MessageType.LOGOUT) {
	                    	disconnect();
	                    }else if(msn.getMessage().startsWith("ban ")) {
	                    	banlist.add(msn.getMessage().split(" ")[1]);
	                    	System.out.println(this.username+" ha baneado a " + msn.getMessage().split(" ")[1]);
	                    	
	                    }else if(msn.getMessage().startsWith("unban ")) {
	                    	if (banlist.contains(msn.getMessage().split(" ")[1])) {
	                    		banlist.remove(msn.getMessage().split(" ")[1]);
		                    	System.out.println(this.username+" ha desbaneado a " + msn.getMessage().split(" ")[1]);
	                    	}else {
	                    		ChatMessage error =  new ChatMessage(this.username, ChatMessage.MessageType.MESSAGE, "En tu lista de usuarios baneados no se ha encontrado "+msn.getMessage().split(" ")[1]);
	                    		sendMessage(error,this);
	                    		//System.out.println("\nPor lo tanto no se ha podido llevar a cabo la opercion de desbaneo\n");
	                    	}
	                    	
	                    }else {
	                    	System.out.println("\n" + username + " Mensaje: " + msn.getMessage());
		                    // Enviar el mensaje a todos los clientes conectados
		                    broadcast(msn,this);
	                    }
	                } else {
	                    System.err.println("ERROR: Se recibió un objeto de tipo inesperado: " + prueba.getClass().getName());
	                }
	            } catch (IOException | ClassNotFoundException ex) {
	                System.err.println("Se ha desconectado el cliente con ID " + id);
	                break; // Salir del bucle si hay error
	            }
	        }

	    }

	    public void sendMessage(ChatMessage msg,ServerThreadForClient cliente) {
	        try {
	        		if(banlist.contains(cliente.username)) {
	        			
	        		}else {
	        			oos.writeObject(msg);
			            oos.flush();  
	        		}
	        		 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    public void disconnect() {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (socket != null) socket.close();
                clients.remove(this);
                System.out.println("\nCliente con ID " + id + " desconectado y eliminado. \n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}

