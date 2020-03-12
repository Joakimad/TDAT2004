package opg2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {

        final int port = 80;
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server running on port " + port);
        Socket connection = ss.accept();
        System.out.println("Client connected");

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        String headerParts = reader.readLine();

        ArrayList<String> header = new ArrayList<>();
        while (!headerParts.equals("")) {
            header.add(headerParts);
            System.out.println(headerParts);
            headerParts = reader.readLine();
        }

        writer.println("HTTP/1.0 200 OK\r");
        writer.println("Content-Type: text/html; charset=utf-8\r");
        writer.println("Server: SimpleWebServer\r");
        writer.println("\r\n");
        writer.println("\r\n");

        writer.println("<HTML>\r\n<BODY>\r\n");
        writer.println("<h1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\r\n");
        writer.println("---Header---\r\n");
        writer.println("<ul>\r");
        for (String line : header) {
            writer.println("<li>" + line + "</li>\r");
        }
        writer.println("</ul>------\r");
        writer.println("</BODY>\r\n</HTML>\r");

        reader.close();
        writer.close();
        connection.close();
    }
}
