package Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static final int PORT = 40000;
    private static final String RMI_NAME = "rmi://localhost:" + PORT + "/hello";

    public static void main(String[] args) {
        try {
            //Implementation implementation = new Implementation();
            startRegistry();
            //Naming.rebind(RMI_NAME, implementation);
            System.out.println("RMI_HelloWorld.Server Bound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRegistry()
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.list();

        } catch (RemoteException ex) {
            System.out.println("RMI registry cannot be located at port " + PORT);
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("RMI registry created at port " + PORT);
        }
    } // end startRegistry
}
