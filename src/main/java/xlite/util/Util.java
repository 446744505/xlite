package xlite.util;

import java.io.File;
import java.util.Arrays;

public class Util {
    public static String firstToUpper(String s) {
        char[] cs = s.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static void cleanDir(File dir) {
        if (dir.isFile()) {
            dir.delete();
        } else {
            Arrays.stream(dir.listFiles()).forEach(Util::cleanDir);
        }
    }
}
