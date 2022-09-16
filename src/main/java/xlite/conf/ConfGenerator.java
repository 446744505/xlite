package xlite.conf;

import org.dom4j.DocumentException;
import xlite.coder.*;
import xlite.excel.XExcel;
import xlite.excel.XReader;
import xlite.excel.cell.XCell;
import xlite.gen.Writer;
import xlite.gen.XGenerator;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.type.visitor.BoxName;
import xlite.util.Util;
import xlite.xml.XParser;
import xlite.xml.XmlContext;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ConfGenerator {
    public static final String ENDPOINT_ALL = "all";
    public static final String ENDPOINT_SERVER = "server";
    public static final String ENDPOINT_CLIENT = "client";

    private final XParser parser;
    private final XGenerator generator;
    private final XmlContext context;
    private final File excelDir;//enum用

    public ConfGenerator(URL xml, File excelDir, String out, String language) throws DocumentException {
        ConfFactory factory = new ConfFactory();
        context = new XmlContext(factory);
        parser = new XParser(xml, context);
        generator = new XGenerator(out, language, factory);
        this.excelDir = excelDir;
    }

    public void gen(boolean isLoadCode, String endPoint) throws Exception {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        context.setConfLoadCode(isLoadCode);
        context.setEndPoint(endPoint);
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        xPackage.check();
        loadEnumField(xPackage);
        if (isLoadCode) {
            addLoadMethod(xPackage);
            addInitClass(xPackage);
        }
        generator.gen(xPackage);
    }

    private void loadEnumField(XPackage root) throws Exception {
        List<XEnum> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        ConfExcelHook hook = new ConfExcelHook(true);
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            List<String> enumExcels = Util.getExcels(e.getFromExcel());
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                List<String> fieldExcels = Util.getExcels(f.getExcel());
                fieldExcels.addAll(enumExcels);
                fieldExcels.forEach(excel -> hook.addEnumExcel(excel, f.getName()));
            });
        });

        Map<String, XExcel> excels = new XReader(excelDir, hook).read();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            List<String> enumExcels = Util.getExcels(e.getFromExcel());
            List<ConfEnumField> newFields = new ArrayList<>();
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                List<String> fieldExcels = Util.getExcels(f.getExcel());
                fieldExcels.addAll(enumExcels);
                fieldExcels.forEach(excel -> {
                    XExcel data = excels.get(excel);
                    data.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {
                        XCell nameCell = row.getCell(f.getName());
                        XCell valueCell = row.getCell(f.getFromCol());
                        XCell commentCell = row.getCell(f.getComment());
                        ConfEnumField field = new ConfEnumField(nameCell.asString(), f.getType(), e);
                        field.setCellValue(valueCell);
                        if (Objects.nonNull(commentCell)) {
                            field.setComment(commentCell.asString());
                        }
                        newFields.add(field);
                    }));
                });
            });
            newFields.forEach(f -> e.addField(f));
        });
    }

    private void addLoadMethod(XPackage root) {
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .forEach(c -> new PrintLoadMethod(c, null).make());
    }

    private void addInitClass(XPackage root) {
        List<XEnum> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        Map<String, String> allEnumExcels = new HashMap<>();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            List<String> enumExcels = Util.getExcels(e.getFromExcel());
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                List<String> fieldExcels = Util.getExcels(f.getExcel());
                fieldExcels.addAll(enumExcels);
                fieldExcels.forEach(excel -> allEnumExcels.put(excel, f.getName()));
            });
        });

        Writer body = new Writer();
        body.println("ConfExcelHook hook = new ConfExcelHook(false);");
        allEnumExcels.forEach((excel, keyCol) -> {
            body.println(2, String.format("hook.addEnumExcel(\"%s\", \"%s\");", excel, keyCol));
        });
        body.println(2, "Map<String, XExcel> excels = new XReader(excelDir, hook).read();");
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                for (String excel : Util.getExcels(c.getFromExcel())) {
                    if (Util.isEmpty(excel)) {
                        continue;
                    }
                    String boxName = new XBean(c.getName()).accept(BoxName.INSTANCE, Java.INSTANCE);
                    int tab = 2;
                    body.println(tab, "{");
                    body.println(tab + 1, String.format("XExcel excel = excels.get(\"%s\");", excel));
                    body.println(tab + 1, "excel.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {");
                    body.println(tab + 2, String.format("%s obj = new %s();", boxName, boxName));
                    body.println(tab + 2, "obj.load(row, 0);");
                    body.println(tab + 2, "System.out.println(obj);");
                    body.println(tab + 1, "}));");
                    body.println(tab, "}");
                }
            });
        body.deleteEnd(1);//去掉最后一个换行
        XClass clazz = new XClass("Init", root);
        clazz.addImport("java.io.File");
        clazz.addImport("java.util.Map");
        clazz.addImport("xlite.conf.ConfExcelHook");
        clazz.addImport("xlite.excel.XExcel");
        clazz.addImport("xlite.conf.ConfGenerator");
        clazz.addImport("xlite.excel.XReader");
        XMethod load = new XMethod("loadAll", clazz);
        XField file = new XField("excelDir", new XBean(File.class), load);
        load.staticed()
            .returned(TypeBuilder.VOID)
            .addParam(file)
            .throwed("Exception")
            .addBody(body.getString());
        clazz.addMethod(load);
        root.addClass(clazz);
    }
}
