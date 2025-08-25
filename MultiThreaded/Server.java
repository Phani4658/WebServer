package MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;

public class Server {

    public Consumer<Socket> getConsumer() {
        return clientSocket -> {
            try (PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                // Send greeting to the client (auto-flushed)
                toClient.println("Hello From the multithreaded server");

                // Read message from client
                String message = fromClient.readLine();
                System.out.println("Message from the client: " + message);

            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void main(String[] args) {
        int port = 8080;
        Server server = new Server();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(10000);
            System.out.println("Server Listening on port " + port);

            while (true) {
                try {
                    Socket acceptedSocket = serverSocket.accept();
                    Thread thread = new Thread(() -> server.getConsumer().accept(acceptedSocket));
                    thread.start();
                } catch (SocketTimeoutException e) {
                    System.out.println("No client connected in last 10 seconds, still listening...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}