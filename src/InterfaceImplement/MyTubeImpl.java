package InterfaceImplement;

import Content.ContentInterface;
import RemoteInterface.MyTubeInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MyTubeImpl extends UnicastRemoteObject implements MyTubeInterface {
    public MyTubeImpl() throws RemoteException {
        super();
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
    public List<ContentInterface> searchFromKeyword(String keyword) throws RemoteException {
        //TODO
        return null;
    }

    @Override
    public ContentInterface uploadContent(String title, String description, byte[] fileData) throws RemoteException {
        try {
            File file = new File("path where we want to save the file" + title);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
            output.write(fileData, 0, fileData.length);
            output.flush();
            output.close();
        }catch(Exception e){
            System.err.println("FileServer Exception " + e.getMessage());
            e.printStackTrace();
        }
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
}
