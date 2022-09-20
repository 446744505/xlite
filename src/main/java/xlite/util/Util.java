package xlite.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Util {
    public static String firstToUpper(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String firstToLower(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static void cleanDir(File dir) {
        if (dir.isFile()) {
            dir.delete();
        } else {
            Arrays.stream(dir.listFiles()).forEach(Util::cleanDir);
        }
    }

    public static boolean isEmpty(String s) {
        return Objects.isNull(s) || s.isEmpty();
    }

    public static boolean notEmpty(String s) {
        return Objects.nonNull(s) && !s.trim().isEmpty();
    }

    public static List<String> getExcels(String conf) {
        List<String> excels = new ArrayList<>();
        if (notEmpty(conf)) {
            for (String excel : conf.split(",")) {
                if (notEmpty(excel)) {
                    excels.add(excel);
                }
            }
        }
        return excels;
    }
}
