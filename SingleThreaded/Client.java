package SingleThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public void run() throws IOException {
        int port = 8080;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, port);
        socket.setSoTimeout(10000);

        // Auto-flush enabled for PrintWriter
        PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // First read the server's greeting
        String serverMessage = fromSocket.readLine();
        System.out.println("Response from the server is: " + serverMessage);

        // Then send the client's message
        toSocket.println("Hello from the client");

        // Close resources
        fromSocket.close();
        toSocket.close();
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.run();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}