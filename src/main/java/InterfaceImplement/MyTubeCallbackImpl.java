package InterfaceImplement;

import RemoteInterface.MyTubeCallbackInterface;

import java.rmi.RemoteException;

public class MyTubeCallbackImpl implements MyTubeCallbackInterface {
    @Override
    public void notifyNewContent(String tittle) throws RemoteException {

    }

    @Override
    public void notifyServerStopped() throws RemoteException {

    }
}
