package InterfaceImplement;

import Content.ContentInterface;
import RemoteInterface.MyTubeInterface;
import XMLDatabase.XMLCreator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MyTubeImpl extends UnicastRemoteObject implements MyTubeInterface {
    String systemFile= "./server01";
    static XMLCreator xmlcreator ;

    public MyTubeImpl() throws IOException, SAXException, ParserConfigurationException {
        super();
        xmlcreator = new XMLCreator();
    }

    @Override
    public ContentInterface getContentFromKey(int key) throws RemoteException {
        //TODO
        return null;
    }

    @Override
    public ContentInterface getContentFromTitle(String title) throws RemoteException {
        //TODO
        return null;
    }


    @Override
    public List<String> searchFromKeyword(String keyword) throws RemoteException {
        //TODO
         return null;
    }

    @Override
    public List<String> searchAll() throws RemoteException {
        Process call = makeALinuxCall("ls ./server01");
        try {
            readSystemCallAsList(call);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public String uploadContent(String title, String description, byte[] fileData) throws RemoteException {
        //TODO HASH
        String hash="";
        String response = "";
        BufferedOutputStream output;
        File file = new File("./server/" + hash +  title);
        try {
            output = new BufferedOutputStream(new FileOutputStream(file.getName()));
            output.write(fileData, 0, fileData.length);
            output.flush();
            output.close();
            response = "Successful upload!";
        }catch(FileNotFoundException e){
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
    public ContentInterface downloadContent() throws RemoteException {
        return null;
    }

    //per que acabi de funciona obviament necessitem el sistema de fitxers montat i un metode que ens doni

    //el path al fitxer que conté el nom passat per parametre
    @Override
    public byte[] downloadContent(String contentName) throws RemoteException{
        try{
            File file = new File(contentName);
            byte buffer[] = new byte[(int)file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(contentName));

            input.read(buffer, 0, buffer.length);
            input.close();

            return(buffer);

        }catch(Exception e){
            System.out.println("FileImpl " + e.getMessage());
            e.printStackTrace();

            return(null);
        }
    }
    @Override
    public void exit() throws RemoteException{
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
            response+=line;
        }
        return response;
    }

    private List<String> readSystemCallAsList(Process p) throws IOException {
        List<String>  response= new ArrayList<>();
        String line ="";
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
