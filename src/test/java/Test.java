import conf.Init;
import xlite.conf.ConfGenerator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws Exception {
        URL url = Test.class.getResource("conf.xml");
        new ConfGenerator(url, "gen", "java").gen(true);

        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        Init.loadAll(excelDir);
    }
}
