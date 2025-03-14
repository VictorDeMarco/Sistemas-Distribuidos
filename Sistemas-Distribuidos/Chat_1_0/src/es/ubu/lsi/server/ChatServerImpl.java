/**
 * 
 */
package es.ubu.lsi.server;

import java.text.SimpleDateFormat;

import es.ubu.lsi.common.ChatMessage;

/**
 * @author Víctor De Marco Velasco
 *
 */
public class ChatServerImpl implements ChatServer {
	public static final int DEFAULT_PORT=1500;
	public static int clientid;
	public static SimpleDateFormat sdf;
	public int port;
	public boolean alive;
	
	public ChatServerImpl (int port) {
		this.port = port;
	}
	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcast(ChatMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub
		
	}
	
	public void main(String[] args) {
		
	}
	
	public class ServerThreadForClient {
		public int id;
		public String Username;
		public void run() {
			
		}
	}
}
