package es.ubu.lsi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServerImpl;

public class ChatClientImpl implements ChatClient, Serializable {

    private static final long serialVersionUID = 1L;
    private String server;
    private String username;
    private int port;
    private boolean carryOn = true;
    private Socket socket;
    private ObjectOutputStream oos; // SOLO para enviar mensajes

    public ChatClientImpl(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    @Override
    public boolean start() {
        try {
            socket = new Socket(server, port);

            // Crear OutputStream primero
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            // Iniciar el listener en un hilo separado
            new ChatClientListener(socket).start();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendMessage(ChatMessage msg) {
        BufferedReader inUsuario = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">> Ingrese un mensaje: ");
        try {
            String msn = inUsuario.readLine();
            if (msn.equalsIgnoreCase("exit")) {
                carryOn = false;
                return false;
            }

            msg = new ChatMessage(this.username, ChatMessage.MessageType.MESSAGE, msn);
            //System.out.println("El mensaje que vas a enviar es: " + msg);

            // Enviar el mensaje
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void disconnect() {
        try {
            carryOn = false;
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(">> Ingrese un nombre de usuario: ");

            ChatClientImpl cliente1 = new ChatClientImpl("localhost", ChatServerImpl.DEFAULT_PORT, entrada.readLine());
            if (!cliente1.start()) {
                System.err.println("No se pudo conectar al servidor.");
                return;
            }

            while (cliente1.sendMessage(null));

            entrada.close();
            cliente1.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 🔹 ChatClientListener ahora gestiona su propio ObjectInputStream
    public class ChatClientListener extends Thread {
        private ObjectInputStream ois;
        private Socket socket;

        public ChatClientListener(Socket socket) {
            this.socket = socket;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void run() {
            while (carryOn) {
                try {
                    Object obj = ois.readObject();

                    if (obj instanceof ChatMessage) {
                        ChatMessage msn = (ChatMessage) obj;
                        System.out.println("\n>> " + msn.getUsername() + " ---- Mensaje: " + msn.getMessage());
                        System.out.print(">> Ingrese un mensaje: ");
                    } else {
                        System.err.println("Se recibió un objeto inesperado: " + obj.getClass().getName());
                    }
                } catch (IOException e) {
                    System.err.println("Se perdió la conexión con el servidor.");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // Cerrar el flujo cuando el cliente se desconecta
            try {
                if (ois != null) ois.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
