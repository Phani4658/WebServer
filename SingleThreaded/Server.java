package SingleThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    public void run() throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000); // Optional timeout

        System.out.println("Server is Listening on port " + port);

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection Accepted from client: " + clientSocket.getRemoteSocketAddress());

                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                toClient.println("Hello From the Server"); // Will flush automatically due to PrintWriter's autoFlush=true

                String line = fromClient.readLine();
                System.out.println("Response from the client is: " + line);

                toClient.close();
                fromClient.close();
                clientSocket.close(); // Close client connection, not the server socket
            } catch (SocketTimeoutException e) {
                System.out.println("No client connected within timeout period. Waiting again...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}