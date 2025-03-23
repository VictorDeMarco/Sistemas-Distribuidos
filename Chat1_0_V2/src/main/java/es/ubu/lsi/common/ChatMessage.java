/**
 * 
 */
package es.ubu.lsi.common;
import java.io.*;
// TODO: Auto-generated Javadoc

/**
 * The Class ChatMessage.
 *
 * @author Usuario
 */

/**
 * Message in chat system.
 * 
 * @author Raúl Marticorena
 * @author Joaquin P. Seco
 *
 */
public class ChatMessage implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 17L;

	/**
	 * Message type.
	 * 
	 * @author Raúl Marticorena
	 * @author Joaquin P. Seco
	 */
	public enum MessageType {
		/** Message. */
		MESSAGE,
		/** Shutdown server. */
		SHUTDOWN,		
		/** Logout client. */
		LOGOUT;		
	}
	
	/** Type. */
	private MessageType type;
	
	/** Text. */
	private String message;
	
	/** Client id. */
	private String username;
	
	/**
	 * Constructor.
	 *
	 * @param username the username
	 * @param type type
	 * @param message message
	 */
	public ChatMessage(String username, MessageType type, String message) {
		this.setUsername(username);
		this.setType(type);
		this.setMessage(message);
	}
	
	/**
	 * Gets type.
	 * 
	 * @return type
	 * @see #setType
	 */
	public MessageType getType() {
		return type;
	}
	
	/**
	 * Sets type.
	 *
	 * @param type the new type
	 * @see #getType()
	 */
	private void setType(MessageType type) {
		this.type = type;
	}
	
	/**
	 * Gets message.
	 * 
	 * @return message
	 * @see #setMessage
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets message.
	 * 
	 * @param message message
	 * @see #getMessage
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
