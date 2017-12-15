package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAssembler {

    public static void fileAssembler(String home, byte[] filedata, String title) throws IOException {
        File file = new File(home + title);
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileOutputStream output = new FileOutputStream(file);

        output.write(filedata, 0, filedata.length);
        output.flush();
        output.close();
    }
}
