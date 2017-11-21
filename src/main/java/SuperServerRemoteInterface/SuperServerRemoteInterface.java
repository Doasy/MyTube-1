package SuperServerRemoteInterface;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SuperServerRemoteInterface extends Remote {
    List<String> getAllDistributedContent() throws RemoteException;
}
