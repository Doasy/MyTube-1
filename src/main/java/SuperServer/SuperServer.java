package SuperServer;

import java.net.UnknownHostException;

import static java.lang.System.exit;


public class SuperServer {
    private static final String IDENTIFIER = "NODE ";


    private static void startMessage(){
        System.out.println("SuperServer Started");
    }

    private static void endMessage(){
        System.out.println("SuperServer Closing");
    }

    private static void threadLauncher() throws UnknownHostException {
        Thread theThread =
                new Thread(new SuperServerReader());
        theThread.start();
    }

    private static void killNode() { exit(0); }


    public static void main(String[] args) throws Exception {
        try {
            threadLauncher();
            startMessage();
            while(true) {
                Thread.sleep(1000);/*
                if(counter.getVoters() == MAXVOTERS) {
                    Sender.send(Packer.pack(IDENTIFIER, counter.getVotes()));
                    endMessage();
                    killNode();
                }*/
            }
        }
        catch (Exception se) {
            se.printStackTrace( );
        }
    }
}

