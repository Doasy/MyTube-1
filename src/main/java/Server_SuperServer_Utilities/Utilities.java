package Server_SuperServer_Utilities;

import InterfaceImplement.MyTubeImpl;
import RemoteInterface.MyTubeInterface;
import SuperServer.SuperServer;
import SuperServerInterfaceImpl.SuperServerImpl;
import SuperServerRemoteInterface.SuperServerRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
