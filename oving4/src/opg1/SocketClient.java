package opg1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) throws IOException {
        final int PORT_NUMBER = 1234;

        Scanner in = new Scanner(System.in);
        Socket connection = new Socket("localhost", PORT_NUMBER);

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        String data = reader.readLine();

        while (data != null) {
            System.out.println(data);
            System.out.println("Enter operation seperated with space (1 + 2)");
            String operation = in.nextLine();
            writer.println(operation);
            data = reader.readLine();
        }
    }
}
