
import xlite.conf.sql.ExcelSqlEngine;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class SqlTest {
    public static void main(String[] args) throws Exception {
        URL xmlUrl = Test.class.getResource("conf.xml");
        File xml = Paths.get(xmlUrl.toURI()).toFile();
        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        ExcelSqlEngine sqlEngine = new ExcelSqlEngine(xml, excelDir);
        sqlEngine.init();
        sqlEngine.sql("select * from equip where `攻击`=30");
    }
}
