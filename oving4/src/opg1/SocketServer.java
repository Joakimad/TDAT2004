package opg1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) {
        final int PORT_NUMBER = 1234;
        Socket socket = null;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Starting server...");

            while (true) {
                socket = serverSocket.accept();
                Thread newClientThread = new ThreadClientHandler(socket);
                newClientThread.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                assert socket != null;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}