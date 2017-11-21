package SuperServerInterfaceImpl;


import RemoteInterface.MyTubeInterface;
import SuperServer.SuperServer;
import SuperServerRemoteInterface.SuperServerRemoteInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SuperServerImpl extends UnicastRemoteObject implements SuperServerRemoteInterface{

    public SuperServerImpl() throws RemoteException {
        super();
    }

    public List<String> getAllDistributedContent() throws RemoteException{
        return SuperServer.getAllDistributedContent();
    }

}
