package Client;

import RemoteInterface.MyTubeInterface;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Client implements ClientInterface{
    private int port;
    private String ip;
    private String rmi_url;
    private String rmi_name;
    private Registry registry;
    private MyTubeInterface stub;
    private String userName;

    Client(String ip, int port, String userName){
        this.port = port;
        this.ip = ip;
        this.userName = userName;
        this.rmi_name = "MyTube";
        rmi_url = "rmi://" + ip + ":" + port + "/" + this.rmi_name;
    }


    private static void optionsMenu(){
        System.out.println("Welcome to MyTube, tell us what you want to do.\n" +
                "1: Upload\n"+
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
    public void connectToTheServer() throws NotBoundException {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            registry = LocateRegistry.getRegistry(ip, port);
            stub = (MyTubeInterface) registry.lookup("MyTube");
            //callbackObject = new MyTubeCallbackImpl();
            //stub.addCallback(callbackObject);
            //ColoredString.printlnSuccess("MyTube client connected on: "+  registryURL);
        } catch (RemoteException ex) {
            System.out.println("Can not connect to the server");
            System.exit(1);
        }
    }


    public static void main(String args[]) {
        int option;
        try {
            if (args.length < 2) {
                System.err.println("Parameters: <ip> <port>");
                System.exit(1);
            }
            String userName = registerIntoApp();
            final Client client = new Client(args[0], Integer.parseInt(args[1]), userName);
            client.connectToTheServer();
            optionsMenu();
            option = Integer.parseInt(readInput());
            switch (option){
                case 0:
                    client.exit();
                    break;
                case 1:
                    //TODO
                    client.upload(null);
                    break;
                case 2:
                    client.download("contentName");
                    break;
                case 3:
                    client.listAll();
                    break;
                case 4:
                    //TODO
                    client.search(null);
                    break;

            }
        }
        catch (Exception e) {
            System.out.println("Exception in Client: "+  e);
        }
    }

    private static String registerIntoApp() throws IOException {
        System.out.println("Hi! What's your nikname?");
        return readInput();
    }


    @Override
    public void search(String keyWord) {
        StringBuilder listToPrint = new StringBuilder();
        List<String> listOfSearchedItems= searchAsList(keyWord);;
        for(String content : listOfSearchedItems){
            listToPrint.append(content).append("\n");
        }
        System.out.println("The list of contents with keyword" + keyWord + "is:");
        System.out.println(listToPrint);
    }

    private List<String> searchAsList(String keyWord) {
        List<String> contents = new ArrayList<>();
        try {
            contents = stub.searchFromKeyword(keyWord);
        } catch (RemoteException e) {
            System.err.println("Problem searching files");
        }
        return contents;
    }

    @Override
    public void listAll() {
        StringBuilder listToPrint = new StringBuilder();
        List<String> listOfContents= listAllAsList();
        for(String content : listOfContents){
            listToPrint.append(content).append("\n");
        }
        System.out.println("The list of all contents is:");
        System.out.println(listToPrint);
    }

    private List<String> listAllAsList() {
        List<String> contents = new ArrayList<>();
        try {
            contents = stub.searchAll();
        } catch (RemoteException e) {
            System.err.println("Problem searching files");
        }
        return contents;
    }

    public void download() {
        int contentID = getContentID();
        if (contentID == -1) {
            optionsMenu();
        }
        try {
            byte[] filedata = stub.downloadContent(contentID);
            File file = new File("path where we want to save the file" + contentID);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
            output.write(filedata, 0, filedata.length);
            output.flush();
            output.close();
        }catch(Exception e) {
            System.err.println("FileServer Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getContentID() {
        System.out.println("Do you know the file ID (Yy/Nn)?  ");
        if (isAnswerYes()) {
            return getFileIDFromID();
        }
        return getFileIDFromName();
    }

    private int getFileIDFromID() {
        System.out.println("Introduce the file ID: ");
        int fileID = Integer.parseInt(readFromInput());
        if (isValidID(fileID)) {
            System.out.println("Downloading...");
            return fileID;
        }
        return invalidIDTreatment();
    }

    private boolean isValidID(int fileID) {
        //TODO: IMPLEMENT
        return true;
    }

    private int invalidIDTreatment() {
        System.out.println("Invalid ID. Try again (Yy/Nn)? ");
        if (isAnswerYes()) {
            return getFileIDFromID();
        }
        return -1;
    }

    private boolean isAnswerYes() {
        String knowsID = null;
        try {
            knowsID = readInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (knowsID.equals("Y") || knowsID.equals("y"));
    }

    private int getFileIDFromName() {
        System.out.println("Introduce the file name: ");
        String fileName = readFromInput();
        search(fileName);
        int fileID = Integer.parseInt(readFromInput());
        return fileID;
    }

    private String readFromInput() {
        try {
            return readInput();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public String upload(String content) {
        String uploadResponse = "";
        try{
            File file = new File(content);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

            input.read(buffer, 0, buffer.length);
            input.close();

            uploadResponse = stub.uploadContent("title", "description", buffer, userName);

            System.out.println(uploadResponse);

        }catch(Exception e){
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();

            uploadResponse = "Something was wrong :S";
        }
        return uploadResponse;
    }

    @Override
    public void exit() {
        //TODO
    }
}