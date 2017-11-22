package Utils;

public class ExceptionMessageThrower {

    public static void ioExceptionMessage(Exception e) {
        System.out.println("IO Error: " + e.getMessage());
        e.printStackTrace();
    }

    public static void fileNotFoundException(Exception e){
        System.err.println("File Not Found " + e.getMessage());
        e.printStackTrace();
    }
}
