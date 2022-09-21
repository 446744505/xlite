package xlite.conf;

import org.dom4j.DocumentException;
import xlite.coder.*;
import xlite.conf.formatter.DataFormatter;
import xlite.excel.XExcel;
import xlite.excel.XReader;
import xlite.excel.cell.XCell;
import xlite.gen.Writer;
import xlite.gen.XGenerator;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.XBean;
import xlite.type.XEnum;
import xlite.type.XType;
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
    private final XLanguage language;
    private String dataOut;
    private String dataFormat;
    private final File excelDir;//enum用

    public ConfGenerator(URL xml, File excelDir, String srcOut, String lan) throws DocumentException {
        ConfFactory factory = new ConfFactory();
        context = new XmlContext(factory);
        parser = new XParser(xml, context);
        language = factory.createLanguage(lan);
        generator = new XGenerator(srcOut, language, factory);
        this.excelDir = excelDir;
    }

    public void genCode(String endPoint) throws Exception {
        gen(false, endPoint);
    }

    public void genData(String endPoint) throws Exception {
        if (Util.isEmpty(dataOut)) {
            dataOut = "conf";
        }
        if (Util.isEmpty(dataFormat)) {
            dataFormat = "json";
        }
        File dir = new File(dataOut);
        dir.mkdirs();
        Util.cleanDir(dir);
        gen(true, endPoint);
    }

    private void gen(boolean isReadCode, String endPoint) throws Exception {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        context.setConfReadCode(isReadCode);
        context.setEndPoint(endPoint);
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        loadEnumField(xPackage);
        xPackage.check();
        if (isReadCode) {
            addReadMethod(xPackage);
            addInitClass(xPackage);
        } else {
            addLoadMethod(xPackage);
            addLoaderClass(xPackage);
        }
        generator.gen(xPackage);
    }

    private void loadEnumField(XPackage root) throws Exception {
        List<XEnumer> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        ConfExcelHook hook = new ConfExcelHook(true);
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                getExcels(e, f).forEach(excel -> hook.registerEnumExcel(excel, f.getName()));
            });
        });

        Map<String, XExcel> excels = new XReader(excelDir, hook).read();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            List<ConfEnumField> newFields = new ArrayList<>();
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                getExcels(e, f).forEach(excel -> {
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

    private void addReadMethod(XPackage root) {
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .forEach(c -> new PrintReadMethod(c, null).make());
    }

    private void addLoadMethod(XPackage root) {
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .filter(c -> Util.notEmpty(c.getFromExcel()))
                .forEach(c -> new PrintLoadMethod(c, null).make());
    }

    private List<String> getExcels(ConfEnum e, ConfEnumField field) {
        List<String> enumExcels = Util.getExcels(e.getFromExcel());
        List<String> fieldExcels = Util.getExcels(field.getExcel());
        fieldExcels.addAll(enumExcels);
        return fieldExcels;
    }

    private void addInitClass(XPackage root) {
        final String paramGeneratorName = "generator";
        List<XEnumer> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        Map<String, String> allEnumExcels = new HashMap<>();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            e.getFields().stream()
                    .map(f -> (ConfEnumField)f).filter(f -> Objects.isNull(f.getValue())).forEach(f -> {
                getExcels(e, f).forEach(excel -> allEnumExcels.put(excel, f.getName()));
            });
        });

        Writer body = new Writer();
        body.println("ConfExcelHook hook = new ConfExcelHook(false);");
        allEnumExcels.forEach((excel, keyCol) -> {
            body.println(2, String.format("hook.registerEnumExcel(\"%s\", \"%s\");", excel, keyCol));
        });
        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                XField idField = c.getIdField();
                List<String> excels = Util.getExcels(c.getFromExcel());
                for (String excel : excels) {
                    XType idType = idField.getType();
                    if (idType instanceof XEnum) {
                        XEnum e = ((XEnum) idType);
                        //这里不用BoxName,因为BoxName返回了inner的BoxName
                        String fullName = XClass.getFullName(e.getName(), Java.INSTANCE);
                        body.println(2, String.format("hook.registerEnumIdExcel(\"%s\", %s.class);", excel, fullName));
                    }
                }});

        body.println(2, "Map<String, XExcel> excels = new XReader(excelDir, hook).read();");

        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                XField idField = c.getIdField();
                for (String excel : Util.getExcels(c.getFromExcel())) {
                    String boxName = new XBean(c.getName()).accept(BoxName.INSTANCE, Java.INSTANCE);
                    int tab = 2;
                    body.println(tab, "{");
                    body.println(tab + 1, String.format("XExcel excel = excels.get(\"%s\");", excel));
                    String idBoxName = idField.getType().accept(BoxName.INSTANCE, Java.INSTANCE);
                    body.println(tab +1, String.format("java.util.Map<%s, %s> conf = new java.util.HashMap<>();", idBoxName, boxName));
                    body.println(tab + 1, "excel.iterator().forEachRemaining(sheet -> sheet.rows().forEach(row -> {");
                    body.println(tab + 2, String.format("%s obj = new %s();", boxName, boxName));
                    body.println(tab + 2, "obj.read(row, 0);");
                    String idGetter = "get" + Util.firstToUpper(idField.getName());
                    body.println(tab + 2, String.format("conf.put(obj.%s(), obj);", idGetter));
                    body.println(tab + 1, "}));");
                    XClass topParent = c.getTopParent();
                    if (Objects.isNull(topParent)) {
                        topParent = c;
                    }
                    body.println(tab + 1, String.format("%s.export(conf, %s.class, %s.class);", paramGeneratorName, boxName, topParent.getFullName(Java.INSTANCE)));
                    body.println(tab, "}");
                }
            });
        body.deleteEnd(1);//去掉最后一个换行

        XClass clazz = new XClass("Init", root);
        clazz.addImport("java.io.File")
            .addImport("java.util.Map")
            .addImport("xlite.conf.ConfExcelHook")
            .addImport("xlite.excel.XExcel")
            .addImport("xlite.conf.ConfGenerator")
            .addImport("xlite.excel.XReader");
        XMethod load = new XMethod("loadAll", clazz);
        XField paramExcelDir = new XField("excelDir", new XBean(File.class), load);
        XField paramGenerator = new XField(paramGeneratorName, new XBean(ConfGenerator.class), load);
        load.staticed()
            .addParam(paramExcelDir)
            .addParam(paramGenerator)
            .throwed("Exception")
            .addBody(body.getString());
        clazz.addMethod(load);
        root.addClass(clazz);
    }

    private void addLoaderClass(XPackage root) {
        XClass clazz = language.accept(new AddLoaderClass(root));
        root.addClass(clazz);
    }

    public void setDataConf(String dataOut, String dataFormat) {
        this.dataOut = dataOut;
        this.dataFormat = dataFormat;
    }

    private final Map<Class, Set<Object>> allIds = new HashMap<>();

    public void export(Map<?, ?> conf, Class clazz, Class parent) throws Exception {
        Set<Object> ids = allIds.get(parent);
        if (Objects.isNull(ids)) {
            ids = new HashSet<>();
            allIds.put(parent, ids);
        }
        for (Object id : conf.keySet()) {
            if (!ids.add(id)) {
                throw new IllegalStateException(String.format("multi id %s at %s", id, parent.getName()));
            }
        }
        DataFormatter.createFormatter(dataFormat).export(conf, clazz, new File(dataOut));
    }
}
