package xlite.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Util {
    public static final int SECOND = 1000;
    public static final int MIN = 60 * SECOND;
    public static final int HOUR = 60 * MIN;
    public static final int DAY = 24 * HOUR;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public static LocalDateTime strToDate(String val) {
        if (val.length() == 10) {
            val += " 00:00:00";
        }
        return LocalDateTime.parse(val, formatter);
    }

    public static long strToTime(String val) {
        val = val.toLowerCase();
        long ms = findBeforeNum(val, "ms");
        val = val.replace("ms", "");//ms与m字母m冲突，所以干掉
        long s = findBeforeNum(val, "s");
        long m = findBeforeNum(val, "m");
        long h = findBeforeNum(val, "h");
        long d = findBeforeNum(val, "d");
        return d * DAY + h * HOUR + m * MIN + s * SECOND + ms;
    }

    private static long findBeforeNum(String val, String flag) {
        int idx = val.indexOf(flag);
        if (idx < 0) {
            return 0;
        }
        int num = 0;
        int unit = 1;
        for (int i = idx - 1; i >= 0; i--) {
            char c = val.charAt(i);
            if (Character.isDigit(c)) {
                num += (c - '0') * unit;
                unit *= 10;
                continue;
            }
            break;
        }
        return num;
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

    public static List<File> javaFiles(File file) {
        List<File> files = new ArrayList<>();
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(f -> {
                files.addAll(javaFiles(f));
            });
            return files;
        }
        String ext = FilenameUtils.getExtension(file.getName());
        if ("java".equals(ext)) {
            files.add(file);
        }
        return files;
    }
}
