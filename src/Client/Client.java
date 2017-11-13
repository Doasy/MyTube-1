package Client;

import RemoteInterface.MyTubeInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.Naming.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private int port;
    private String ip;
    private String rmi_name;
    private Registry registry;
    private MyTubeInterface stub;

    Client(String ip, int port){
        this.port = port;
        this.ip = ip;
        rmi_name = "rmi://" + ip + ":" + port + "/MyTube";
    }


    private static void optionsMenu(){
        System.out.println("Welcome to MyTube, tell us what you want to do.\n" +
                "1: Upload\n"+
                "2: Download\n" +
                "3: List the digital available.");
    }

    private static String readInput() throws IOException {
        String input;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        input = br.readLine();

        return input;
    }
    public void connectToTheServer() throws NotBoundException {
        try {
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            registry = LocateRegistry.getRegistry(ip, port);
            stub = (MyTubeInterface) registry.lookup(rmi_name);
            //String registryURL = "rmi://"  + ip  + ":" +  port+"/"  + registry;
            //callbackObject = new MyTubeCallbackImpl();
            //stub.addCallback(callbackObject);
            //ColoredString.printlnSuccess("MyTube client connected on: "+  registryURL);
        } catch (RemoteException ex) {
            System.out.println("Can not connect to the server");
            System.exit(1);
        }
    }


    public static void main(String args[]) {
        String option;
        try {
            if (args.length < 2) {
                System.err.println("Parameters: <ip> <port>");
                System.exit(1);
            }

            final Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.connectToTheServer();
            //TODO: POSAR TOTES LES REMOTES INTERFACES QUE FEM SERVIR
            optionsMenu();
            option = readInput();
        }
        catch (Exception e) {
            System.out.println("Exception in Client: "+  e);
        }
    }
}
