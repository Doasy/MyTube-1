package Client;

import RemoteInterface.MyTubeCallbackInterface;
import RemoteInterface.MyTubeInterface;

import InterfaceImplement.*;

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
    private MyTubeInterface stub;
    private String userName;
    private MyTubeCallbackInterface callbackObject;

    private Client(String ip, int port, String userName){
        this.port = port;
        this.ip = ip;
        this.userName = userName;
        this.rmi_name = "MyTube";
        rmi_url = "rmi://" + ip + ":" + port + "/" + this.rmi_name;
    }


    private static void optionsMenu() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Welcome to MyTube, tell us what you want to do.\n" +
                "0: Exit\n"+
                "1: Upload\n"+
                "2: Download\n" +
                "3: List the available content\n" +
                "4: Search by keyWord\n" +
                "5: Delete Content\n" +
                "6: Modify Content\n" +
                "7: List Distributed Content");
    }

    private static String readInput() throws IOException {
        String input;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        input = br.readLine();

        return input;
    }

    private void connectToTheServer() throws NotBoundException {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            Registry registry = LocateRegistry.getRegistry(ip, port);
            stub = (MyTubeInterface) registry.lookup(rmi_name);
            callbackObject = new MyTubeCallbackImpl();
            stub.addCallback(callbackObject);
            System.out.println("MyTube client connected on: "+  rmi_name);
        } catch (RemoteException ex) {
            System.err.println("Can't connect to the server");
            System.exit(1);
        }
    }

    private void disconnectFromTheServer() {
        try {
            stub.removeCallback(callbackObject);
        } catch (Exception ex) {
            System.err.println("Error disconnecting from the server");
        }
    }


    public static void main(String args[]) {
        int option;
        String keyword;
        String[] fileInfo;
        try {
            if (args.length < 2) {
                System.err.println("Parameters: <ip> <port>");
                System.exit(1);
            }
            String userName = registerIntoApp();
            final Client client = new Client(args[0], Integer.parseInt(args[1]), userName);
            client.connectToTheServer();
            while(true) {
                optionsMenu();
                option = Integer.parseInt(readInput());
                switch (option) {
                    case 0:
                        client.exit();
                        break;
                    case 1:
                        fileInfo = client.getInfoUpload();
                        client.upload(fileInfo[0], fileInfo[1]);
                        break;
                    case 2:
                        client.download();
                        break;
                    case 3:
                        client.listAll();
                        break;
                    case 4:
                        System.out.println("Enter a keyword to search for content: ");
                        keyword = client.readFromInput();
                        client.search(keyword);
                        break;
                    case 5:
                        client.deleteContent();
                        break;
                    case 6:
                        client.modifyContent();
                        break;
                    case 7:
                        client.showAllDistrubutedContent();
                        break;
                    case 8:
                        System.out.println("Enter a keyword to search for content: ");
                        keyword = client.readFromInput();
                        client.searchDistributedFromKeyword(keyword);
                        break;
                    default:
                        System.out.println("Incorrect Option.");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception in Client: "+  e);
        }
    }

    private static String registerIntoApp() throws IOException {
        System.out.println("Hi! What's your nickname?");

        return readInput();
    }


    @Override
    public void search(String keyWord) {
        StringBuilder listToPrint = new StringBuilder();
        List<String> listOfSearchedItems = searchAsList(keyWord);

        for(String content : listOfSearchedItems){
            listToPrint.append(content).append("\n");
        }

        System.out.println("The list of contents with keyword " + keyWord + " is:");
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

    public void download() throws RemoteException, InterruptedException {
        int contentID = getContentID();
        if (contentID == -1) {
            optionsMenu();
        }
        else {
            downloadContentWithID(contentID);
        }
    }

    private void downloadContentWithID(int contentID) {
        String home = System.getProperty("user.home");
        try {
            byte[] filedata = stub.downloadContent(contentID);
            String title = stub.getTitleFromKey(contentID);
            System.out.println("Downloading in directory "+home + "/Downloads/" + title+"...");

            File file = new File(home + "/Downloads/" + title);
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileOutputStream output = new FileOutputStream(file);

            output.write(filedata, 0, filedata.length);
            output.flush();
            output.close();
        }catch(Exception e) {
            System.err.println("FileServer Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getContentID() throws RemoteException{
        System.out.println("Do you know the file ID (Yy/Nn)?  ");

        if (isAnswerYes()) {
            return fileIDTreatment();
        }

        return getFileIDFromName();
    }

    private int fileIDTreatment() throws RemoteException {
        System.out.println("Introduce the file ID: ");
        int fileID = Integer.parseInt(readFromInput());

        if (isValidID(fileID)) {
            return fileID;
        }
        return invalidIDTreatment();
    }

    private boolean isValidID(int fileID) throws RemoteException {
        return stub.isValidID(fileID);
    }

    private int invalidIDTreatment() throws RemoteException {
        System.out.println("Invalid ID. Try again (Yy/Nn)? ");

        if (isAnswerYes()) {
            return fileIDTreatment();
        }

        return -1;
    }

    private boolean isAnswerYes() {
        String knowsID = "";
        try {
            knowsID = readInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (knowsID.toLowerCase().equals("y"));
    }

    private int getFileIDFromName() {
        System.out.println("Introduce the files name: ");

        String fileName = readFromInput();
        search(fileName);

        return Integer.parseInt(readFromInput());
    }

    private String readFromInput() {
        try {

            return readInput();
        } catch (IOException e) {

            return "";
        }
    }

    private String[] getInfoUpload(){
        String[] uploadInfo = new String[2];

        System.out.println("Path of the file to upload");
        uploadInfo[0] = readFromInput();

        System.out.println("Add a Description to your file");
        uploadInfo[1] = readFromInput();

        return uploadInfo;
    }

    @Override
    public String upload(String contentPath, String description) {
        String uploadResponse;
        String title = getTitleFromPath(contentPath);

        try{
            File file = new File(contentPath);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

            input.read(buffer, 0, buffer.length);
            input.close();

            uploadResponse = stub.uploadContent(title, description, buffer, userName);

            System.out.println(uploadResponse);

        }catch(FileNotFoundException e){
            System.err.println("There's no file in this path. Please, try again");
            uploadResponse = "Something was wrong :S";
        } catch (RemoteException e) {
            e.printStackTrace();
            uploadResponse = "Something was wrong :S";
        } catch (IOException e) {
            uploadResponse = "Something was wrong :S";
        }
        return uploadResponse;
    }


    private String getTitleFromPath(String contentPath){
        String[] splitedPath = contentPath.split("/");

        return splitedPath[splitedPath.length-1];
    }

    @Override
    public void deleteContent(){
        try {
            List<String> userFiles = stub.showOwnFiles(userName);
            if(userFiles.size() > 0) {
                printLists(userFiles);

                System.out.println("Select the id from the file that will be modified.");
                String id = readFromInput();
                System.out.println(stub.deleteContent(id, userName));
            }else{
                System.out.println("You can't modify any files");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyContent() throws RemoteException {
        String modifyResponse;
        List<String> userFiles = stub.showOwnFiles(userName);
        if(userFiles.size() > 0){
            printLists(userFiles);

            System.out.println("Select the id from the file that will be modified.");
            String id = readFromInput();
            System.out.println("Write a new title: ");
            String title = readFromInput();
            System.out.println("Write a new description: ");
            String description = readFromInput();
            modifyResponse = stub.modifyContent(id, title, description);

            System.out.println(modifyResponse);
        }else{
            System.out.println("You can't modify any files");
        }
    }

    private void showAllDistrubutedContent() throws RemoteException {
        List<String> ownFiles = stub.searchAll();
        List<String> distrubutedFiles = stub.showAllDistributedContent();
        List<String> toShow = new ArrayList<>();
        for(String files: distrubutedFiles){
            if(!ownFiles.contains(files)){
                toShow.add(files);
            }
        }
        printLists(toShow);
    }

    public void searchDistributedFromKeyword(String keyword) throws RemoteException{
        List<String> ownFiles = stub.searchFromKeyword(keyword);
        List<String> distrubutedFiles = stub.searchDistributedFromKeyword(keyword);
        List<String> toShow = new ArrayList<>();
        for(String files: distrubutedFiles){
            if(!ownFiles.contains(files)){
                toShow.add(files);
            }
        }
        printLists(toShow);
    }

    @Override
    public void exit() {
        System.out.print("Disconnecting from the server...");
        disconnectFromTheServer();
        System.out.println("Thanks for using MyTube! ");
        System.out.println("See you soon ;) ");
        System.exit(0);
    }

    private void printLists(List<String> listToPrint){
        for(String string: listToPrint){
            System.out.println(string);
        }
    }

}