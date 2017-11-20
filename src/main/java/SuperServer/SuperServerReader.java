package SuperServer;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


public class SuperServerReader implements Runnable {

    private static final String CLIENT = "CLIENT";
    private static final String ADDRESS = "224.0.0.1";
    private static final int MAX_LEN = 30;
    private static final int PORT = 40000;
    private InetAddress group;

    SuperServerReader() throws UnknownHostException {
        this.group = InetAddress.getByName(ADDRESS);
    }


    public void run() {
        MulticastSocket socket;
        try {
            socket = new MulticastSocket(PORT);
            socket.joinGroup(group);
            waitAndProcessClientMessage(socket);
        }
        catch (Exception exception) {
            exception.printStackTrace( );
        }
    }


    private void waitAndProcessClientMessage(MulticastSocket socket) throws IOException {
        String message;
        while (true) {
            DatagramPacket packet = getDatagramPacket();
            message = readMessage(socket, packet);
            if(message.contains(CLIENT)){
                processClientMessage(message.split("\\s")[1]);
            }
        }
    }

    private synchronized void processClientMessage(String message) {
        //TODO
    }


    private String readMessage(MulticastSocket socket, DatagramPacket packet) throws IOException {
        String message;
        socket.receive(packet);
        message = new String(packet.getData());
        return message;
    }


    private DatagramPacket getDatagramPacket() {
        byte[] data = new byte[MAX_LEN];
        return new DatagramPacket(data, data.length, group, PORT);
    }
}
