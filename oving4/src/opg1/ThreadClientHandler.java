package opg1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadClientHandler extends Thread {
    private Socket connection;

    public ThreadClientHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            System.out.println(connection.toString());

            InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readConnection);
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            writer.println("You are connected to the socket server...");

            String data = reader.readLine();

            while (data != null) {
                String[] result = data.split(" ");
                if (result.length != 3) {
                    writer.println("Wrong number of arguments");
                } else if (!(result[1].equals("+") || result[1].equals("-"))) {
                    writer.println("Did not use '+' or '-'!");
                } else {
                    int no1 = Integer.parseInt(result[0]);
                    int no2 = Integer.parseInt(result[2]);
                    switch (result[1]) {
                        case "+":
                            try {
                                int resultOfOperation = no1 + no2;
                                writer.println(no1+ " + " + no2 + " = " + resultOfOperation);
                            } catch (NumberFormatException e) {
                                writer.println(e);
                            }
                            break;
                        case "-":
                            try {
                                int resultOfOperation = no1 - no2;
                                writer.println(no1+ " - " + no2 + " = " + resultOfOperation);
                            } catch (NumberFormatException e) {
                                writer.println(e);
                            }
                            break;
                    }
                }
                data = reader.readLine();
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

