package libcore.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by y2k on 6/2/16.
 */
public class IoUtils {
    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
