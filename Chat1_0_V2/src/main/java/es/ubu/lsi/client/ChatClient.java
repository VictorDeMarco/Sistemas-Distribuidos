/**
 * 
 */
package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

// TODO: Auto-generated Javadoc
/**
 * The Interface ChatClient.
 *
 * @author Victor De Marco Velasco
 */
public interface ChatClient {
	
	/**
	 * Start.
	 *
	 * @return true, if successful
	 */
	public boolean start ();
	
	/**
	 * Send message.
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	public boolean sendMessage(ChatMessage msg);
	
	/**
	 * Disconnect.
	 */
	public void disconnect();
}
