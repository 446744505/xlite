import xlite.conf.ConfGenerator;
import java.net.URL;

public class Test {
    public static void main(String[] args) throws Exception {
        URL url = Test.class.getResource("conf.xml");
        new ConfGenerator(url, "gen", "java").gen();
    }
}
