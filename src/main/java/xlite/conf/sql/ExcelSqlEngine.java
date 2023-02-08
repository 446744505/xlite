package xlite.conf.sql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import xlite.coder.XClass;
import xlite.coder.XEnumer;
import xlite.coder.XPackage;
import xlite.conf.ConfClass;
import xlite.conf.ConfFactory;
import xlite.excel.*;
import xlite.util.Util;
import xlite.xml.XParser;
import xlite.xml.XmlContext;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExcelSqlEngine {
    private final File xml;
    private final File excelDir;
    private SparkSession spark;

    public ExcelSqlEngine(File xml, File excelDir) {
        this.xml = xml;
        this.excelDir = excelDir;
    }

    public void init() throws Exception {
        XmlContext context = new XmlContext(new ConfFactory());
        XParser parser = new XParser(xml, context);
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        List<XClass> allClass = new ArrayList<>();
        List<XEnumer> allEnum = new ArrayList<>();
        xPackage.getAllClass(allClass);
        xPackage.getAllEnum(allEnum);

        XReader reader = new XReader(excelDir, new Hook());
        Map<String, XExcel> allExcel = reader.read();

        spark = SparkSession.builder()
            .appName("xlite-excel")
            .master("local")
            .getOrCreate();

        allClass.stream()
            .map(c -> (ConfClass)c)
            .filter(c -> Util.notEmpty(c.getFromExcel()))
            .forEach(c -> {
                List<String> excels = Util.getExcels(c.getFromExcel());
                Dataset<Row> table = null;
                for (String name : excels) {
                    XExcel excel = allExcel.get(name);
                    for (XSheet sheet : excel) {
                        Dataset<Row> df = spark.read()
                            .format("com.crealytics.spark.excel")
                            .option("header", "true")
                            .option("dataAddress", String.format("'%s'!A1", sheet.getSheetName()))
                            .load(excel.getPath());
                        if (Objects.isNull(table)) {
                            table = df;
                        } else {
                            table = table.union(df);
                        }
                    }
                }
                if (Objects.nonNull(table)) {
                    table.createOrReplaceTempView(c.getName().toLowerCase());
                }
            });
    }

    public void sql(String sql) {
        spark.sql(sql).show();
    }

    private static class Hook implements XExcelHook {
        @Override
        public void checkHeader(int colIndex, String title, XSheet sheet) {
            return;
        }

        @Override
        public Object key(XRow row) {
            return null;
        }

        @Override
        public boolean isLoadExcel(String fileName) {
            return true;
        }

        @Override
        public boolean isParseKey() {
            return false;
        }
    }
}
