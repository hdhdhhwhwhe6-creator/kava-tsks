// Server.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Set<Socket> clientSockets = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("New client connected: " + clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    broadcastMessage("Client " + socket.getPort() + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket);
            } finally {
                try {
                    clientSockets.remove(socket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            for (Socket client : clientSockets) {
                try {
                    if (!client.isClosed()) {
                        PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                        writer.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// Client.java
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to chat server");

            new ReadThread(socket).start();
            new WriteThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReadThread extends Thread {
    private BufferedReader in;

    public ReadThread(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }
}

class WriteThread extends Thread {
    private PrintWriter out;
    private BufferedReader userInput;

    public WriteThread(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;
        try {
            while ((message = userInput.readLine()) != null) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
