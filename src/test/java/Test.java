import conf.Loader;
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

//        generator.setDataConf("conf/data", "json");
//        generator.genData(ConfGenerator.ENDPOINT_ALL);
//        Init.loadAll(excelDir, generator);//TODO 动态编译

        generator.genCode(ConfGenerator.ENDPOINT_ALL);
        URL dataURL = Test.class.getResource("data");
        File dataDir = Paths.get(dataURL.toURI()).toFile();
        Loader.loadAll(dataDir);
    }
}
