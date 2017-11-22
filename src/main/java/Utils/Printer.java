package Utils;

import java.util.List;

public class Printer {

    public static void printLists(List<String> listToPrint){
        for(String string: listToPrint){
            System.out.println(string);
        }
    }

    public static void optionsMenu() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Welcome to MyTube, tell us what do you want to do.\n" +
                "0: Exit\n"+
                "1: Upload\n"+
                "2: Download\n" +
                "3: List the available content\n" +
                "4: Search by keyWord\n" +
                "5: Delete Content\n" +
                "6: Modify Content\n" +
                "7: List content from all the servers \n" +
                "8: Search all the servers by keyword\n" +
                "9: Download distributed content");
    }
}
