package MultiThreaded;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public Runnable getRunnable(int clientId) {
        return () -> {
            int port = 8080;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                try (Socket socket = new Socket(address, port);
                     PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    socket.setSoTimeout(10000);

                    // Read server's greeting
                    String serverMessage = fromSocket.readLine();
                    System.out.println("Client " + clientId + " received: " + serverMessage);

                    // Send message to server
                    toSocket.println("Hello from client " + clientId);

                }
            } catch (Exception e) {
                System.err.println("Client " + clientId + " encountered an error: " + e.getMessage());
                // Optionally: e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        Client client = new Client();
        
        // Use a thread pool instead of creating 100 raw threads
        ExecutorService executor = Executors.newFixedThreadPool(20); // 20 concurrent threads
        for (int i = 0; i < 100; i++) {
            executor.submit(client.getRunnable(i));
        }
        executor.shutdown();
    }
}