package SuperServerRemoteInterface;

import RemoteInterface.MyTubeInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface SuperServerRemoteInterface extends Remote {
    List<String> getAllDistributedContent(ArrayList<MyTubeInterface> stubs) throws RemoteException;
}
