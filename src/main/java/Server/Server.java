package Server;

import InterfaceImplement.MyTubeImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private MyTubeImpl stub;
    private Registry registry;
    private final String host;
    private final int port;
    private final String registryName;
    private final String registryURL;

    /**
     * Creates a Server instance
     *
     * @param host the server waits for client connections on this IP
     * @param port port where the server listens for client petitions
     * @param registryName name of the registered service on RMI Registry
     */
    public Server(String host, int port, String registryName) throws IOException {
        this.host = host;
        this.port = port;
        this.registryName = registryName;
        registryURL = "rmi://" + host + ":" + port
                + "/" + registryName;

        createDirectory();
    }

    /**
     * ./server01 will be our system files
     * @throws IOException
     */
    private void createDirectory() throws IOException {
        Process p = Runtime.getRuntime().exec("mkdir ./server01");

        //p = Runtime.getRuntime().exec("ls");

        //readSystemCall(p);
    }


    public static void main(String args[]) throws IOException {

        String registryName = "Mytube";
        if (args.length < 2) {
            System.err.println("Parameters: <host> <port> "
                    + "[registryName] [dbName]");
            System.exit(1);
        }


        final Server s = new Server(args[0], Integer.parseInt(args[1]),
                registryName);

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void start() {
                System.out.println("Stopping server!");
                try {
                    mainThread.join();
                } catch (Exception e) {
                    System.out.println("Can not finalize main process");
                }
                try {
                    s.stopServer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        s.runServer();

    }

    /**
     * Runs the Server
     */
    public void runServer() {
        try {
            System.setProperty("java.rmi.server.hostname", host);
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            stub = new MyTubeImpl();
            registry = getRegistry();
            registry.rebind(registryName, stub);
            System.out.println("MyTube Server ready on: " + registryURL);
        } catch (Exception ex) {
            System.err.println("Server error: " + ex.toString());
        }
    }

    /**
     * Stopps the Server
     */
    public void stopServer() throws RemoteException {
        if (stub != null) {
            stub.exit();
        }
        try {
            registry.unbind(registryName);
            UnicastRemoteObject.unexportObject(stub, true);
            System.out.println("Server stopped correctly");
        } catch (Exception ex) {
            System.err.println("Server failed on stop");
        }
    }

    private Registry getRegistry() throws RemoteException {
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
