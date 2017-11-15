package InterfaceImplement;

import RemoteInterface.MyTubeInterface;
import XMLDatabase.XMLCreator;
import XMLDatabase.XMLParser;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MyTubeImpl extends UnicastRemoteObject implements MyTubeInterface {
    private String systemFile = "./server01";
    private static XMLCreator xmlcreator;

    public MyTubeImpl() throws IOException, SAXException, ParserConfigurationException {
        super();
        xmlcreator = new XMLCreator();
    }

    @Override
    public String getContentFromKey(int key) throws RemoteException {
        String pathToFile = "./server01/" + Integer.toString(key) + "/";

        return null;
    }

    @Override
    public String getContentFromTitle(String title) throws RemoteException {
        int id;
        String path = "";
        try{
            XMLParser xmlParser = new XMLParser();
            id = xmlParser.XMLFindIdByTitle(title);
            path = getContentFromKey(id);
        }catch(JDOMException | IOException ex ){
            ex.printStackTrace();
        }

        return path;
    }

    @Override
    public List<String> searchFromKeyword(String keyword) throws RemoteException {
        List<String> contentFound = new ArrayList<>();
        try{
            XMLParser xmlParser = new XMLParser();
            contentFound =  xmlParser.XMLFindByKeyWord(keyword);

        }catch(JDOMException | IOException ex ){
            ex.printStackTrace();
        }
        return contentFound;
    }

    @Override
    public List<String> searchAll() throws RemoteException {
        List<String> allContent = new ArrayList<>();
        try{
            XMLParser xmlParser = new XMLParser();
            allContent = xmlParser.XMLShowALL();

        }catch(JDOMException | IOException ex){
            ex.printStackTrace();
        }

        return allContent;
    }


    @Override
    public String uploadContent(String title, String description, byte[] fileData) throws RemoteException {
        //TODO HASH
        String hash = "";
        String response = "";
        BufferedOutputStream output;
        File file = new File("./server/" + hash + title);

        try {
            output = new BufferedOutputStream(new FileOutputStream(file.getName()));
            output.write(fileData, 0, fileData.length);
            output.flush();
            output.close();
            response = "Successful upload!";

        } catch (FileNotFoundException e) {
            System.err.println("FileServer Exception " + e.getMessage());
            response = "there has been a problem with the file :S";
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("FileServer Exception " + e.getMessage());
            response = "there has been a IO problem :S";
            e.printStackTrace();
        }

        xmlcreator.addElement(hash, title, description, "patata");

        return response;
    }

    @Override
    public byte[] downloadContent(String contentName) throws RemoteException {
        try {
            File file = new File(contentName);
            byte buffer[] = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(contentName));

            input.read(buffer, 0, buffer.length);
            input.close();

            return (buffer);

        } catch (Exception e) {
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();

            return (null);
        }
    }

    @Override
    public void exit() throws RemoteException {
        //TODO
    }

    private Process makeALinuxCall(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    private String readSystemCallAsString(Process p) throws IOException {
        String response = "";
        String line;

        BufferedReader stdInput = readSystemCall(p);
        while ((line = stdInput.readLine()) != null) {
            response += line;
        }
        return response;
    }

    private List<String> readSystemCallAsList(Process p) throws IOException {
        List<String> response = new ArrayList<>();
        String line = "";
        BufferedReader stdInput = readSystemCall(p);
        while ((line = stdInput.readLine()) != null) {
            response.add(line);
        }
        return response;
    }

    private BufferedReader readSystemCall(Process p) {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));
        return stdInput;
    }

}
