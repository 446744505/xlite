import xlite.sql.JsonSqlEngine;

import java.io.File;

public class SqlTest {
    public static void main(String[] args) throws Exception {
//        URL xmlUrl = Test.class.getResource("conf.xml");
//        File xml = Paths.get(xmlUrl.toURI()).toFile();
//        URL excelURL = Test.class.getResource("excel");
//        File excelDir = Paths.get(excelURL.toURI()).toFile();
//        ExcelSqlEngine sqlEngine = new ExcelSqlEngine(xml, excelDir);
//        sqlEngine.init();
//        sqlEngine.sql("select * from equip where `攻击`=30");

        JsonSqlEngine sqlEngine = new JsonSqlEngine(new File("conf_sql"));
        sqlEngine.init();
        //查询哪些材料拆解后有2001这个道具
        sqlEngine.sql(
                "select tmp.itemId, tmp.name, tmp.isOk from " +
                    "(select m.itemId as itemId, m.name as name, " +
                    "   case " +
                    "       when (array_contains(m.awards, 2001)) then 'ok' " +
                    "   end as isOk " +
                    "from material as m) as tmp " +
                "where tmp.isOk='ok'");
    }
}
