/**
 * 
 */
package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

/**
 * @author Victor De Marco Velasco
 *
 */
public interface ChatClient {
	public boolean start ();
	public boolean sendMessage(ChatMessage msg);
	public void disconnect();
}
