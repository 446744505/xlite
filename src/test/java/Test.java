import conf.Init;
import xlite.conf.ConfGenerator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws Exception {
        URL xmlUrl = Test.class.getResource("conf.xml");
        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        ConfGenerator generator = new ConfGenerator(xmlUrl, excelDir,"gen", "java");
        generator.gen(true, ConfGenerator.ENDPOINT_ALL);

        Init.loadAll(excelDir, generator);
    }
}
