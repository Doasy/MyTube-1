package SuperServerInterfaceImpl;


import SuperServer.SuperServer;
import SuperServerRemoteInterface.SuperServerRemoteInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SuperServerImpl extends UnicastRemoteObject implements SuperServerRemoteInterface{

    public SuperServerImpl() throws RemoteException {
        super();
    }

    public List<String> getAllDistributedContent() throws RemoteException{
        return SuperServer.getAllDistributedContent();
    }

    public List<String> searchDistributedFromKeyword(String keyword) throws RemoteException{
        return SuperServer.searchDistributedFromKeyword(keyword);
    }
}
