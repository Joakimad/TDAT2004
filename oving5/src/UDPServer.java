package opg1;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(3333);
        byte[] dataIn = new byte[1024];
        byte[] dataOut = new byte[1024];

        System.out.println("Server started...");
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(dataIn, dataIn.length);
            serverSocket.receive(receivePacket);

            String data = new String(receivePacket.getData()).trim();
            System.out.println(data);
            String[] dataFromUser = data.split(" ");
            if (dataFromUser.length != 3) {
                dataOut = "Wrong number of arguments".getBytes();
            } else if (!(dataFromUser[1].equals("+") || dataFromUser[1].equals("-"))) {
                dataOut = "Did not use '+' or '-'!".getBytes();
            } else {
                int no1 = Integer.parseInt(dataFromUser[0].trim());
                int no2 = Integer.parseInt(dataFromUser[2].trim());
                switch (dataFromUser[1]) {
                    case "+":
                        int resultAdd = no1 + no2;
                        String outADD = String.valueOf(resultAdd);
                        dataOut = outADD.getBytes();
                        break;
                    case "-":
                        int resultSub = no1 - no2;
                        String outSub = String.valueOf(resultSub);
                        dataOut = outSub.getBytes();
                        break;
                }
            }
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(dataOut, dataOut.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}