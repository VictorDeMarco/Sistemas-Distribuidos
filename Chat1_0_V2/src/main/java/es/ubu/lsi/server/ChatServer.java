package es.ubu.lsi.server;


import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServerImpl.ServerThreadForClient;

// TODO: Auto-generated Javadoc
/**
 * The Interface ChatServer.
 */
public interface ChatServer {
	
	/**
	 * Startup.
	 */
	public void startup ();
	
	/**
	 * Shutdown.
	 */
	public void shutdown ();
	
	/**
	 * Broadcast.
	 *
	 * @param message the message
	 * @param cliente the cliente
	 */
	public void broadcast(ChatMessage message,ServerThreadForClient cliente);
	
	/**
	 * Removes the.
	 *
	 * @param id the id
	 */
	public void remove(int id);
}
