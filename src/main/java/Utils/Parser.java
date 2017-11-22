package Utils;

public class Parser {

    public static String getTitleFromPath(String contentPath){
        String[] splitedPath = contentPath.split("/");

        return splitedPath[splitedPath.length-1];
    }
}
