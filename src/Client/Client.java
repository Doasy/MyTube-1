package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming.*;
public class Client {
    private static final int PORT = 40000;
    private static final String RMI_NAME = "rmi://localhost:" + PORT + "/MyTube";

    public static void main(String args[]) {
        String option;
        try {
            optionsMenu();
            option = readInput();
        }
        catch (Exception e) {
            System.out.println("Exception in Client: " + e);
        }
    }

    private static void optionsMenu(){
        System.out.println("Welcome to MyTube, tell us what you want to do.\n" +
                "1: Upload\n" +
                "2: Download\n" +
                "3: List the digital available.");
    }

    private static String readInput() throws IOException {
        String input;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        input = br.readLine();

        return input;
    }
}
