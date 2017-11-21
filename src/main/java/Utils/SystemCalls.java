package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemCalls {

    public static Process makeALinuxCall(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static String readSystemCallAsString(Process p) throws IOException {
        String response = "";
        String line;

        BufferedReader stdInput = readSystemCall(p);
        while ((line = stdInput.readLine()) != null) {
            response += line;
        }
        return response;
    }

    private static BufferedReader readSystemCall(Process p) {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

        return stdInput;
    }
}
