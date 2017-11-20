package Server;

import InterfaceImplement.MyTubeImpl;
import Server_SuperServer_Utilities.Utilities;

import java.io.IOException;
import java.rmi.RemoteException;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private MyTubeImpl stub;
    private Registry registry;
    private final String host;
    private final int port;
    private final String registryName;
    private final String registryURL;


    /***
     * In charge of starting the thread.
     * @throws UnknownHostException
     */
    private static void threadLauncher() throws UnknownHostException {
        Thread theThread = new Thread();
        theThread.start();
    }

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
    }


    public static void main(String args[]) throws IOException {

        String registryName = "MyTube";
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
                    stopServer(s.getRegistry(), registryName, s.getStub());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        s.runServer();
        threadLauncher();
    }

    /**
     * Stopps the Server
     */
    private static void stopServer(Registry registry, String registryName, MyTubeImpl stub) throws RemoteException {
        try{
            registry.unbind(registryName);
            UnicastRemoteObject.unexportObject(stub, true);
            System.out.println("Server stopped correctly");
        } catch (Exception ex) {
            System.err.println("Server failed on stop");
        }
    }

    /**
     * Runs the Server
     */
    private void runServer() {
        try {
            System.setProperty("java.rmi.server.hostname", host);
            System.setProperty("java.security.policy", "security.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            stub = new MyTubeImpl();
            registry = Utilities.getRegistry(host, port);
            registry.rebind(registryName, stub);
            System.out.println("MyTube Server ready on: " + registryURL);
        } catch (Exception ex) {
            System.err.println("Server error: " + ex.toString());
        }
    }

    private Registry getRegistry(){
        return this.registry;
    }

    private MyTubeImpl getStub(){
        return this.stub;
    }

}
