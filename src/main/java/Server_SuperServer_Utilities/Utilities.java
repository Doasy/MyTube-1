package Server_SuperServer_Utilities;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Utilities {

    public static Registry getRegistry(String host, int port) throws RemoteException {
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port " + port);
        } catch (RemoteException ex) {
            reg = LocateRegistry.getRegistry(host, port);
            System.err.println("RMI registry is already created on port "
                    + port);
        }
        return reg;
    }
}
