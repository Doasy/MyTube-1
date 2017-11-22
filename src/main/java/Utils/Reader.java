package Utils;

import java.util.Scanner;

public class Reader {
    private static Scanner scanner = new Scanner(System.in);

    public static String ipServerReader(){
        System.out.println("Provide server's IP:");
        return scanner.nextLine();
    }

    public static int portServerReader(){
        System.out.println("Provide server's port:");
        return Integer.parseInt(scanner.nextLine());
    }

    public static String ipSuperServerReader(){
        System.out.println("Provide SuperServer's IP:");
        return scanner.nextLine();
    }

    public static int portSuperServerReader(){
        System.out.println("Provide SuperServer's port:");
        return Integer.parseInt(scanner.nextLine());
    }

    public static String keywordReader(){
        System.out.println("Enter a keyword to search for content:");
        return scanner.nextLine();
    }

    public static String titleReader(){
        System.out.println("Write a title:");
        return scanner.nextLine();
    }

    public static String idReader(){
        System.out.println("Write an id:");
        return scanner.nextLine();
    }

    public static String descriptionReader(){
        System.out.println("Write a description:");
        return scanner.nextLine();
    }

    public static String uploaderReader(){
        System.out.println("Write the uploader:");
        return scanner.nextLine();
    }

    public static String pathReader(){
        System.out.println("Path of the file to upload");
        return scanner.nextLine();
    }

    public static String nickNameReader(){
        System.out.println("Hi! What's your nickname?");
        return scanner.nextLine();
    }

    public static String optionReader(){
        return scanner.nextLine();
    }

    public static String responseReader(){
        System.out.println("Do you know the file ID (Yy/Nn)?");
        return scanner.nextLine();
    }
}
