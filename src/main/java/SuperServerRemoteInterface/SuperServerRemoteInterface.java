package SuperServerRemoteInterface;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SuperServerRemoteInterface extends Remote {
    List<String> getAllDistributedContent() throws RemoteException;
    List<String> searchDistributedFromKeyword(String keyword) throws RemoteException;
    byte[] downloadSpecificContent(String id, String title, String user) throws RemoteException;
}
