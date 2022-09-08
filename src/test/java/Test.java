import conf.Item;
import xlite.conf.ConfExcelHook;
import xlite.excel.XExcel;
import xlite.excel.XReader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
//        URL url = Test.class.getResource("conf.xml");
//        new ConfGenerator(url, "gen", "java").gen();

        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        Map<String, XExcel> excels = new XReader(excelDir, new ConfExcelHook()).read();
        XExcel excel = excels.get("道具.xlsx");
        excel.iterator().forEachRemaining(sheet -> sheet.rows("TEST").forEach(row -> {
            Item item = new Item();
            item.load(row);
            System.out.println(item);
        }));
    }
}
