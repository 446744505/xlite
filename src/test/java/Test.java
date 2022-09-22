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

        generator.genData(ConfGenerator.ENDPOINT_ALL);
//        Init.loadAll(excelDir, generator);//TODO 动态编译

//        generator.genCode(ConfGenerator.ENDPOINT_ALL);
//        Loader.loadAll(new File("conf"));
    }
}
