package InterfaceImplement;

import RemoteInterface.MyTubeCallbackInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MyTubeCallbackImpl extends UnicastRemoteObject implements MyTubeCallbackInterface {

    public MyTubeCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void notifyNewContent(String tittle) throws RemoteException{

    }

    @Override
    public void notifyServerStopped() throws RemoteException {

    }
}
