package opg1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        InetAddress IPAddress = InetAddress.getByName("localhost");

        byte[] dataIn;
        byte[] dataOut = new byte[1024];

        String data = "";
        while (!data.equals("exit")) {
            System.out.println("Enter operation separated with space (1 + 2). Write 'exit' to stop program");
            data = reader.readLine().trim();

            DatagramSocket clientSocket = new DatagramSocket();
            dataIn = data.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(dataIn, dataIn.length, IPAddress, 3333);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(dataOut, dataOut.length);
            clientSocket.receive(receivePacket);
            String resultFromServer = new String(receivePacket.getData()).trim();
            System.out.println("= " + resultFromServer);
            clientSocket.close();
        }

    }
}
