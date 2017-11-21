package InterfaceImplement;

import RemoteInterface.MyTubeCallbackInterface;
import RemoteInterface.MyTubeInterface;
import Server.Server;
import XMLDatabase.XMLCreator;
import XMLDatabase.XMLParser;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyTubeImpl extends UnicastRemoteObject implements MyTubeInterface {
    private static XMLCreator xmlcreator;
    private XMLParser xmlParser;
    private Set<MyTubeCallbackInterface> callbackObjects;

    public MyTubeImpl() throws IOException, SAXException, ParserConfigurationException {
        super();
        xmlcreator = new XMLCreator();
        xmlParser = new XMLParser();
        callbackObjects = new HashSet<>();
    }

    @Override
    public String getTitleFromKey(int key) throws RemoteException {

        return xmlParser.getNameById(Integer.toString(key));
    }

    @Override
    public String getContentFromKey(int key) throws RemoteException {
        String contentName;
        String pathToFile = "./server01/" + Integer.toString(key) + "/";

        contentName = xmlParser.getNameById(Integer.toString(key));
        pathToFile = pathToFile + contentName;

        return pathToFile;
    }

    @Override
    public boolean isValidID(int ID) throws RemoteException{
        return xmlParser.idExists(String.valueOf(ID));
    }

    @Override
    public String getContentFromTitle(String title) throws RemoteException {
        int id;
        String path = "";
        try{
            id = xmlParser.XMLFindIdByTitle(title);
            path = getContentFromKey(id);
        }catch(IOException ex ){
            ex.printStackTrace();
        }

        return path;
    }

    @Override
    public List<String> searchFromKeyword(String keyword) throws RemoteException {
        List<String> contentFound;
        contentFound =  xmlParser.XMLFindByKeyWord(keyword);

        return contentFound;
    }

    @Override
    public List<String> searchAll() throws RemoteException {
        List<String> allContent;
        allContent = xmlParser.XMLShowALL();

        return allContent;
    }


    @Override
    public synchronized String uploadContent(String title, String description, byte[] fileData, String userName) throws RemoteException {

        String hash = xmlParser.newID();
        String response;
        String pathOfFile = "." + File.separator + "server01" + File.separator + hash + File.separator + title;
        System.out.println(title);
        Utils.SystemCalls.makeALinuxCall("mkdir ./server01/" + hash);

        try {
            Utils.FileAssembler.fileAssembler(pathOfFile, fileData, title);

            response = "Successful upload!";

        } catch (FileNotFoundException e) {
            System.err.println("FileServer Exception " + e.getMessage());
            response = "There has been a problem with the file :S";
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("FileServer Exception " + e.getMessage());
            response = "There has been a IO problem :S";
            e.printStackTrace();
        }

        notifyAllNewContent(title);
        xmlcreator.addElement(hash, title, description, userName);

        return response;
    }

    @Override
    public String modifyContent(String id, String title, String description) throws RemoteException{
        try {
            String actualTitle = Utils.SystemCalls.readSystemCallAsString(
                    Utils.SystemCalls.makeALinuxCall("ls ./server01/" + id));
            String oldTitle = "./server01/" + id + "/" + actualTitle;
            String newTitle = "./server01/" + id + "/" + title;
            Utils.SystemCalls.makeALinuxCall("mv " + oldTitle + " " + newTitle);
            return xmlcreator.updateElement(id, title, description);
        } catch (Exception e) {
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();
            return "Problem modifying content";
        }
    }

    @Override
    public String deleteContent(String id, String userName) throws RemoteException {
        try {
            if(xmlParser.userIsUploader(userName, id)){
                Utils.SystemCalls.makeALinuxCall("rm -r ./server01/" + id);
                return xmlcreator.deleteElement(id);
            }else{
                return "Sorry, this file isn't yours.";
            }
        }catch(TransformerException ex){
            ex.printStackTrace();
            return "There has been a problem deleting the content.";
        }
    }

    @Override
    public byte[] downloadContent(int id) throws RemoteException {
        String path;
        try {
            path = getContentFromKey(id);

            return Utils.FileDissasembler.fileDissasembler(path);

        } catch (Exception e) {
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();

            return (null);
        }
    }



    public List<String> showOwnFiles(String userName) throws RemoteException {
        return xmlParser.XMLFindByUserName(userName);
    }

    @Override
    public void addCallback(MyTubeCallbackInterface callbackObject)
            throws RemoteException {
        callbackObjects.add(callbackObject);
        System.out.println("New client registered on callback list. (" + callbackObjects.size() + " clients)");
    }

    @Override
    public void removeCallback(MyTubeCallbackInterface callbackObject) throws RemoteException {
        callbackObjects.remove(callbackObject);
        System.out.println("A client have been removed "
                + "from the callback list. (" + callbackObjects.size() + " clients)");

    }

    @Override
    public List<String> showAllDistributedContent() throws RemoteException{
        return Server.showAllDistributed();
    }

    @Override
    public List<String> searchDistributedFromKeyword(String keyword) throws RemoteException{
        return Server.searchDistributedFromKeyword(keyword);
    }

    private void notifyAllNewContent(String title) {
        for (MyTubeCallbackInterface callback : callbackObjects) {
            try {
                callback.notifyNewContent(title);
            } catch (RemoteException ex) {
                System.err.println("Can't notify new content to all clients");
            }
        }
    }

}
