package SuperServerInterfaceImpl;


import RemoteInterface.MyTubeInterface;
import SuperServerRemoteInterface.SuperServerRemoteInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SuperServerImpl extends UnicastRemoteObject implements SuperServerRemoteInterface{

    public SuperServerImpl() throws RemoteException {
        super();
    }

    public List<String> getAllDistributedContent(ArrayList<MyTubeInterface> stubs) throws RemoteException{
        List<String> allcontent = new ArrayList<>();
        for(MyTubeInterface stub:stubs){
            allcontent.addAll(stub.searchAll());
        }

        return allcontent;
    }

}
