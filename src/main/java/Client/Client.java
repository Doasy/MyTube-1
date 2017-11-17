package Client;

import InterfaceImplement.MyTubeCallbackImpl;
import RemoteInterface.MyTubeCallbackInterface;
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
    private String rmi_name;
    private Registry registry;
    private MyTubeInterface stub;
    private String userName;
    private MyTubeCallbackInterface callbackObject;

    private Client(String ip, int port, String userName){
        this.port = port;
        this.ip = ip;
        this.userName = userName;
        this.rmi_name = "MyTube";
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

     int getContentID() {
        System.out.println("Do you know the file ID (Yy/Nn)?  ");

        if (ClientUtilities.isAnswerYes()) {
            return ClientUtilities.fileIDTreatment();
        }

        return getFileIDFromName();
    }

     private int getFileIDFromName() {
        System.out.println("Introduce the file name: ");

        String fileName = ClientUtilities.readFromInput();
        search(fileName);

        return Integer.parseInt(ClientUtilities.readFromInput());
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
            ClientUtilities.optionsMenu();
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

    @Override
    public String upload(String contentPath, String description) {
        String uploadResponse = "";
        String title = ClientUtilities.getTitleFromPath(contentPath);

        try{
            File file = new File(contentPath);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

            input.read(buffer, 0, buffer.length);
            input.close();

            uploadResponse = stub.uploadContent(title, description, buffer, userName);

            System.out.println(uploadResponse);

        }catch(Exception e){
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();

            uploadResponse = "Something was wrong :S";
        }
        return uploadResponse;
    }


    @Override
    public void deleteContent(){
        try {
            List<String> userFiles = stub.showOwnFiles(userName);
            if(userFiles.size() > 0) {
                ClientUtilities.printLists(userFiles);

                System.out.println("Select the id from the file that will be modified.");
                String id = ClientUtilities.readFromInput();
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
        String modifyResponse = "";
        List<String> userFiles = stub.showOwnFiles(userName);
        if(userFiles.size() > 0){
            ClientUtilities.printLists(userFiles);

            System.out.println("Select the id from the file that will be modified.");
            String id = ClientUtilities.readFromInput();
            System.out.println("Write a new title: ");
            String title = ClientUtilities.readFromInput();
            System.out.println("Write a new description: ");
            String description = ClientUtilities.readFromInput();
            modifyResponse = stub.modifyContent(id, title, description);

            System.out.println(modifyResponse);
        }else{
            System.out.println("You can't modify any files");
        }
    }

    @Override
    public void exit() {
        System.out.print("Disconnecting from the server...");
        disconnectFromTheServer();
        System.out.println("Thanks for using MyTube! ");
        System.out.println("See you soon ;) ");
        System.exit(0);
    }

    private void connectToTheServer() throws NotBoundException {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            registry = LocateRegistry.getRegistry(ip, port);
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
        String[] fileInfo;
        try {
            if (args.length < 2) {
                System.err.println("Parameters: <ip> <port>");
                System.exit(1);
            }

            String userName = ClientUtilities.registerIntoApp();
            final Client client = new Client(args[0], Integer.parseInt(args[1]), userName);
            client.connectToTheServer();

            while(true) {
                ClientUtilities.optionsMenu();
                option = Integer.parseInt(ClientUtilities.readFromInput());
                switch (option) {
                    case 0:
                        client.exit();
                        break;
                    case 1:
                        fileInfo = ClientUtilities.getInfoUpload();
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
                        String keyword = ClientUtilities.readFromInput();
                        client.search(keyword);
                        break;
                    case 5:
                        client.deleteContent();
                        break;
                    case 6:
                        client.modifyContent();
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
}