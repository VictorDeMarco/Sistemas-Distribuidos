package es.ubu.lsi.server;


import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServerImpl.ServerThreadForClient;

public interface ChatServer {
	public void startup ();
	public void shutdown ();
	public void broadcast(ChatMessage message,ServerThreadForClient cliente);
	public void remove(int id);
}
