import xlite.conf.ConfGenerator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws Exception {
        URL xmlUrl = Test.class.getResource("conf.xml");
        File xml = Paths.get(xmlUrl.toURI()).toFile();
        URL excelURL = Test.class.getResource("excel");
        File excelDir = Paths.get(excelURL.toURI()).toFile();
        ConfGenerator generator = new ConfGenerator(xml, excelDir,"gen", "java");

//        generator.addPartExcel("角色装备.xlsx");
//        generator.readDef("TEST");
//        generator.genData(ConfGenerator.ENDPOINT_ALL);

        generator.genCode(ConfGenerator.ENDPOINT_ALL);
//        Loader.loadAll(new File("conf"));
//        System.out.println(Equip.one(ItemID.PetEquip2));
    }
}
