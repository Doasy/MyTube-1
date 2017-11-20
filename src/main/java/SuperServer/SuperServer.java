package SuperServer;


import RemoteInterface.MyTubeInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class SuperServer {

    private static final String rmi_name = "MyTube";


    static public MyTubeInterface connectToTheServer(String ip, int port) throws NotBoundException {
        MyTubeInterface stub = null;

        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            Registry registry = LocateRegistry.getRegistry(ip, port);
            stub = (MyTubeInterface) registry.lookup(rmi_name);

            System.out.println("SuperServer connected on: "+  rmi_name);

        } catch (RemoteException ex) {
            System.err.println("Can't connect to the server");
            System.exit(1);
        }

        return stub;
    }

    public static void main(String args[]) throws RemoteException {
        ArrayList<MyTubeInterface> stubs = new ArrayList<>();

        while(true){
            Scanner keyboard = new Scanner(System.in);
            System.out.println("IP:");
            String ip = keyboard.nextLine();
            System.out.println("Port:");
            int port = Integer.parseInt(keyboard.nextLine());
            try {
                stubs.add(connectToTheServer(ip, port));
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
