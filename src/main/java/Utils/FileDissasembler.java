package Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileDissasembler {
    public static byte[] fileDissasembler(String contentPath) throws IOException {
        File file = new File(contentPath);
        byte buffer[] = new byte[(int)file.length()];
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

        input.read(buffer, 0, buffer.length);
        input.close();
        return buffer;
    }
}
