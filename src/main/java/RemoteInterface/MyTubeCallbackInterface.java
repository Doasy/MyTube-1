package RemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Class used to notify the addition of new contents to the Clients and that the
 * Server ends his execution
 *
 */
public interface MyTubeCallbackInterface extends Remote {

    public void notifyNewContent(String tittle) throws RemoteException;

    public void notifyServerStopped() throws RemoteException;

}
