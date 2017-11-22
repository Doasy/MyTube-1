package Client;

import ServerRemoteInterface.MyTubeCallbackInterface;
import ServerRemoteInterface.MyTubeInterface;

import ServerInterfaceImpl.*;

import Utils.Reader;
import Utils.FileDissasembler;
import Utils.FileAssembler;
import Utils.Printer;
import Utils.Parser;
import Utils.ExceptionMessageThrower;

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
    private MyTubeInterface stub;
    private String userName;
    private MyTubeCallbackInterface callbackObject;

    private Client(String ip, int port, String userName){
        this.port = port;
        this.ip = ip;
        this.userName = userName;
        this.rmi_name = "MyTube";
    }

    //Server Connected Methods
    @Override
    public void exit() {
        System.out.print("Disconnecting from the server...");
        disconnectFromTheServer();
        System.out.println("Thanks for using MyTube! ");
        System.out.println("See you soon ;) ");
        System.exit(0);
    }

    @Override
    public void search(String keyWord) throws RemoteException {
        StringBuilder listToPrint = new StringBuilder();
        List<String> listOfSearchedItems = searchAsList(keyWord);

        for(String content : listOfSearchedItems){
            listToPrint.append(content).append("\n");
        }

        System.out.println("The list of contents with keyword " + keyWord + " is:");
        System.out.println(listToPrint);
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

    @Override
    public void upload(String contentPath, String description) {
        String uploadResponse;
        String title = Parser.getTitleFromPath(contentPath);

        try{
            byte[] buffer = FileDissasembler.fileDissasembler(contentPath);

            uploadResponse = stub.uploadContent(title, description, buffer, userName);

        }catch(FileNotFoundException e){
            System.err.println("There's no file in this path. Please, try again");
            uploadResponse = "Something was wrong :S";
        } catch (IOException e) {
            ExceptionMessageThrower.ioExceptionMessage(e);
            uploadResponse = "Something was wrong :S";
        }

        System.out.println(uploadResponse);
    }

    @Override
    public void deleteContent(){
        try {
            List<String> userFiles = stub.showOwnFiles(userName);
            if(userFiles.size() > 0) {
                Printer.printLists(userFiles);
                String id = Reader.idReader();
                System.out.println(stub.deleteContent(id, userName));
            }else{
                System.out.println("You can't delete any files");
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
            Printer.printLists(userFiles);

            String id = Reader.idReader();
            String title = Reader.titleReader();
            String description = Reader.descriptionReader();

            modifyResponse = stub.modifyContent(id, title, description);
            System.out.println(modifyResponse);
        }else{
            System.out.println("You can't modify any files");
        }
    }

    @Override
    public void download() throws RemoteException{
        String response = Reader.responseReader();

        if(response.toLowerCase().equals("y")){
            downloadWhitID();
        }else{
            downloadWithTitle();
        }
    }

    //Distributed Methods

    @Override
    public void showAllDistrubutedContent() throws RemoteException {
        List<String> ownFiles = stub.searchAll();
        List<String> distrubutedFiles = stub.showAllDistributedContent();
        List<String> toShow = new ArrayList<>();
        for(String files: distrubutedFiles){
            if(!ownFiles.contains(files)){
                toShow.add(files);
            }
        }
        Printer.printLists(toShow);
    }

    @Override
    public void searchDistributedFromKeyword(String keyword) throws RemoteException{
        List<String> ownFiles = stub.searchFromKeyword(keyword);
        List<String> distrubutedFiles = stub.searchDistributedFromKeyword(keyword);
        List<String> toShow = new ArrayList<>();
        for(String files: distrubutedFiles){
            if(!ownFiles.contains(files)){
                toShow.add(files);
            }
        }
        Printer.printLists(toShow);
    }

    @Override
    public void downloadDistributedContent(String id, String title, String user) throws RemoteException {
        String home = System.getProperty("user.home");
        try {
            byte[] filedata = stub.downloadDistributedContent(id, title, user);
            System.out.println("Downloading in directory " + home + "/Downloads/" + title);
            FileAssembler.fileAssembler(home, filedata, title);

        } catch (IOException e) {
            ExceptionMessageThrower.ioExceptionMessage(e);
        }
    }

    //Others

    private void downloadWhitID() throws RemoteException {
        int id = Integer.parseInt(Reader.idReader());

        if (isValidID(id)) {
            downloadContent(id);
        }else{
            System.out.println("Invalid ID");
        }
    }

    private void downloadWithTitle() throws RemoteException {
        String title = Reader.titleReader();

        search(title);
        downloadWhitID();
    }

    private void downloadContent(int contentID) throws RemoteException{
        String home = System.getProperty("user.home");
        try {
            byte[] filedata = stub.downloadContent(contentID);
            String title = stub.getTitleFromKey(contentID);
            System.out.println("Downloading in directory "+home + "/Downloads/" + title+"...");

            FileAssembler.fileAssembler(home, filedata, title);
        }catch(IOException e) {
            ExceptionMessageThrower.ioExceptionMessage(e);
        }
    }

    private boolean isValidID(int fileID) throws RemoteException {
        return stub.isValidID(fileID);
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

    private List<String> searchAsList(String keyWord) throws RemoteException {
        List<String> contents;

        contents = stub.searchFromKeyword(keyWord);

        return contents;
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

    private static void options(Client client) throws InterruptedException, IOException {
        int option;
        String keyword;
        while(true) {
            Printer.optionsMenu();
            option = Integer.parseInt(Reader.optionReader());
            switch (option) {
                case 0:
                    client.exit();
                    break;
                case 1:
                    String path = Reader.pathReader();
                    String description = Reader.descriptionReader();
                    client.upload(path, description);
                    break;
                case 2:
                    client.download();
                    break;
                case 3:
                    client.listAll();
                    break;
                case 4:
                    keyword = Reader.keywordReader();
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
                    keyword = Reader.keywordReader();
                    client.searchDistributedFromKeyword(keyword);
                    break;
                case 9:
                    String id = Reader.idReader();
                    String title = Reader.titleReader();
                    String uploader = Reader.uploaderReader();
                    client.downloadDistributedContent(id, title, uploader);
                    break;
                default:
                    System.out.println("Incorrect Option.");
            }
        }
    }

    public static void main(String args[]) {
        String ip;
        int port;
        try {
            ip = Reader.ipServerReader();
            port = Reader.portServerReader();
            String userName = Reader.nickNameReader();

            final Client client = new Client(ip, port, userName);
            client.connectToTheServer();
            options(client);
        }
        catch (Exception e) {
            System.out.println("Exception in Client: "+  e);
        }
    }
}