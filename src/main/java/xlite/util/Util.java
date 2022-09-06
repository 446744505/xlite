package xlite.util;

public class Util {
    public static String firstToUpper(String s) {
        char[] cs = s.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
