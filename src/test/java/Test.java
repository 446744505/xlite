import conf.Equip;
import conf.Item;
import conf.Material;
import xlite.conf.ConfExcelHook;
import xlite.conf.ConfGenerator;
import xlite.excel.XExcel;
import xlite.excel.XReader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        URL url = Test.class.getResource("conf.xml");
        new ConfGenerator(url, "gen", "java").gen(true);

        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        Map<String, XExcel> excels = new XReader(excelDir, new ConfExcelHook()).read();

        XExcel excel = excels.get("道具.xlsx");
        excel.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {
            Item item = new Item();
            item.load(row, 0);
            System.out.println(item);
        }));

        excel = excels.get("装备.xlsx");
        excel.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {
            Equip equip = new Equip();
            equip.load(row, 0);
            System.out.println(equip);
        }));

        excel = excels.get("材料.xlsx");
        excel.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {
            Material material = new Material();
            material.load(row, 0);
            System.out.println(material);
        }));
    }
}
