package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class ClientUtilities {

    static void optionsMenu(){
        System.out.println("Welcome to MyTube, tell us what you want to do.\n" +
                "0: Exit\n"+
                "1: Upload\n"+
                "2: Download\n" +
                "3: List the digital available.\n" +
                "4: Search by keyWord\n" +
                "5: Delete Content\n" +
                "6: Modify Content");
    }

    static void printLists(List<String> listToPrint){
        for(String string: listToPrint){
            System.out.println(string);
        }
    }

    static String getTitleFromPath(String contentPath){
        String[] splitedPath = contentPath.split("/");

        return splitedPath[splitedPath.length-1];
    }

    static String[] getInfoUpload(){
        String[] uploadInfo = new String[2];

        System.out.println("Path of the file to upload");
        uploadInfo[0] = readFromInput();

        System.out.println("Add a Description to your file");
        uploadInfo[1] = readFromInput();

        return uploadInfo;
    }

    static String readFromInput() {
        try {

            return readInput();
        } catch (IOException e) {

            return "";
        }
    }

    static String readInput() throws IOException {
        String input;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        input = br.readLine();

        return input;
    }

    static boolean isAnswerYes() {
        String knowsID = "";
        try {
            knowsID = ClientUtilities.readInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (knowsID.toLowerCase().equals("y"));
    }

    static int fileIDTreatment() {
        System.out.println("Introduce the file ID: ");
        int fileID = Integer.parseInt(ClientUtilities.readFromInput());

        if (isValidID(fileID)) {
            return fileID;
        }
        return invalidIDTreatment();
    }

    static int invalidIDTreatment() {
        System.out.println("Invalid ID. Try again (Yy/Nn)? ");

        if (ClientUtilities.isAnswerYes()) {
            return fileIDTreatment();
        }

        return -1;
    }

    static boolean isValidID(int fileID) {
        //TODO: IMPLEMENT
        return true;
    }


    static String registerIntoApp() throws IOException {
        System.out.println("Hi! What's your nickname?");

        return ClientUtilities.readInput();
    }

}
