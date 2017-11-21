package ServerRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MyTubeCallbackInterface extends Remote {

    void notifyNewContent(String tittle) throws RemoteException;

    void notifyServerStopped() throws RemoteException;

}
