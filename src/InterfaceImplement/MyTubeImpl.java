package InterfaceImplement;

import Content.ContentInterface;
import RemoteInterface.MyTubeInterface;

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
    public ContentInterface uploadContent(String title, String description) throws RemoteException {
        //TODO
        return null;
    }

    @Override
    public void exit(){
        //TODO
    }
}
