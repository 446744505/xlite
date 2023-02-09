package xlite.conf;

import lombok.Getter;
import xlite.coder.*;
import xlite.conf.elem.ConfBeanElement;
import xlite.conf.formatter.DataFormatter;
import xlite.excel.XExcel;
import xlite.excel.XReader;
import xlite.excel.cell.XCell;
import xlite.gen.Writer;
import xlite.gen.XGenerator;
import xlite.language.XLanguage;
import xlite.type.XBean;
import xlite.type.XEnum;
import xlite.type.XType;
import xlite.type.visitor.BoxName;
import xlite.util.Util;
import xlite.xml.XParser;
import xlite.xml.XmlContext;
import xlite.xml.attr.XAttr;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class ConfGenerator {
    public static final String ENDPOINT_ALL = "all";
    public static final String ENDPOINT_SERVER = "server";
    public static final String ENDPOINT_CLIENT = "client";
    private static final String exportAllMethodName = "exportAll";

    private final XParser parser;
    private final XGenerator generator;
    private final XmlContext context;
    private final XLanguage language;
    private String dataOut;
    private String dataFormat;
    private String def;
    private final File excelDir;//enum用
    /*
    * 部分导表：
    * 1、enum永远导出，这是为了简单，不用去分析bean对enum的依赖
    * 2、（外部找出增加/修改的xml/excel传入，通过addPartExcel/addPartXml），再找出xml里的所有excel(比如在xml里删除了某个bean的一个from excel)
    * 3、再找出跟上面两种excel在同一个bean里的excel则为本次部分打表的所有excel
    * */
    private final Set<String> partExcels = new HashSet<>();
    /*
     * 不会直接使用。先找出里面所有的excel，然后加入partExcels使用
     */
    private final Set<String> partXmls = new HashSet<>();
    private final Map<Class, Set<Object>> allIds = new HashMap<>();
    @Getter private final Map<Class, Map<Object, Object>> allConf = new HashMap<>();

    public ConfGenerator(File xml, File excelDir, String srcOut, String lan) throws Exception {
        ConfFactory factory = new ConfFactory();
        context = new XmlContext(factory);
        parser = new XParser(xml, context);
        language = factory.createLanguage(lan);
        generator = new XGenerator(srcOut, language, factory);
        this.excelDir = excelDir;
    }

    public void addPartExcel(String file) {
        partExcels.add(file);
    }

    public void addPartXml(String file) {
        partXmls.add(file);
    }

    public void readDef(String def) {
        this.def = def;
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
        if (!isPart()) {
            Util.cleanDir(dir);
        }
        gen(true, endPoint);
    }

    public boolean isPart() {
        return !partExcels.isEmpty() || !partXmls.isEmpty();
    }

    /***
     *
     * @param isExportCode true-生成导出数据的代码 false-生成加载数据的代码
     * @param endPoint
     * @throws Exception
     */
    private void gen(boolean isExportCode, String endPoint) throws Exception {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        context.setExportCode(isExportCode);
        context.setEndPoint(endPoint);
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        loadEnumField(xPackage);
        xPackage.check();

        XClass exportClass = null;
        if (isExportCode) {
            processPartXml();
            addEnumValueMethod(xPackage);
            addReadMethod(xPackage);
            exportClass = addExportClass(xPackage);
        } else {
            addLoadMethod(xPackage);
            addLoaderClass(xPackage);
        }
        generator.gen(xPackage);
        if (isExportCode) {
            ClassLoader classLoader = generator.compile();
            Class<?> clazz = classLoader.loadClass(exportClass.getFullName(language));
            Method loadMethod = clazz.getMethod(exportAllMethodName, File.class, ConfGenerator.class);
            loadMethod.invoke(null, excelDir, this);
        }
    }

    private void processPartXml() throws Exception {
        if (partXmls.isEmpty()) {
            return;
        }
        for (File file : parser.getAllXmls()) {
            if (!partXmls.contains(file.getName())) {
                continue;
            }

            XParser parser = new XParser(file, context);
            XElement pak = parser.parse();
            for (XElement child : pak.getChildren()) {
                //只需要读取bean里的excel就可以，enum的会永远导出
                if (child instanceof ConfBeanElement) {
                    ConfBeanElement beanElement = (ConfBeanElement) child;
                    XAttr excelAttr = beanElement.getAttr(XAttr.ATTR_EXCEL);
                    if (Objects.nonNull(excelAttr)) {
                        partExcels.addAll(Util.getExcels(excelAttr.getValue()));
                    }
                }
            }
        }
    }

    private void loadEnumField(XPackage root) throws Exception {
        List<XEnumer> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        ConfExcelHook hook = new ConfExcelHook(true);
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                Util.getEnumExcels(e, f).forEach(excel -> hook.registerEnumExcel(excel, f.getName()));
            });
        });

        Map<String, XExcel> excels = new XReader(excelDir, hook).read();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            List<ConfEnumField> newFields = new ArrayList<>();
            e.getFields().stream().map(f -> (ConfEnumField)f).forEach(f -> {
                Util.getEnumExcels(e, f).forEach(excel -> {
                    XExcel data = excels.get(excel);
                    data.iterator().forEachRemaining(sheet -> sheet.rows(def).forEach(row -> {
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
                .forEach(c -> {
                    language.accept(new PrintReadMethod(c, null));
                    language.accept(new ConfPrintCheckMethod(c));
                });
    }

    private void addEnumValueMethod(XPackage root) {
        List<XEnumer> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        allEnums.stream()
                .filter(e -> Objects.nonNull(e.getInner()))
                .map(e -> (ConfEnum)e).forEach(e -> {
            language.accept(new PrintEnumValueMethod(e));
        });
    }

    private void addLoadMethod(XPackage root) {
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .filter(c -> Util.notEmpty(c.getFromExcel()))
                .forEach(c -> {
                    language.accept(new PrintConferBody(c));
                    language.accept(new PrintLoadMethod(c));
                    language.accept(new PrintAllMethod(c));
                    language.accept(new PrintOneMethod(c));
                });
    }

    private XClass addExportClass(XPackage root) {
        final String paramGeneratorName = "generator";
        List<XEnumer> allEnums = new ArrayList<>();
        root.getAllEnum(allEnums);
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        Map<String, String> allEnumExcels = new HashMap<>();
        allEnums.stream().map(e -> (ConfEnum)e).forEach(e -> {
            e.getFields().stream()
                    .map(f -> (ConfEnumField)f).filter(f -> Objects.isNull(f.getValue())).forEach(f -> {
                Util.getEnumExcels(e, f).forEach(excel -> allEnumExcels.put(excel, f.getName()));
            });
        });

        final int tab = 2;
        Writer body = new Writer();
        body.println("ConfExcelHook hook = new ConfExcelHook(false);");
        allEnumExcels.forEach((excel, keyCol) -> {
            body.println(tab, String.format("hook.registerEnumExcel(\"%s\", \"%s\");", excel, keyCol));
        });

        Set<String> allPartExcels = new HashSet<>();
        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                XField idField = c.getIdField();
                List<String> excels = Util.getExcels(c.getFromExcel());
                for (String excel : excels) {
                    //是部分导表并且部分表里包含当前bean的任一个excel，则该bean的所有excel都导
                    if (isPart() && partExcels.contains(excel)) {
                        allPartExcels.addAll(excels);
                    }
                    XType idType = idField.getType();//有excel则idField一定不为null
                    if (idType instanceof XEnum) {
                        XEnum e = ((XEnum) idType);
                        //这里不用BoxName,因为BoxName返回了inner的BoxName
                        String fullName = XClass.getFullName(e.name(), language);
                        body.println(tab, String.format("hook.registerEnumIdExcel(\"%s\", %s.class);", excel, fullName));
                    }
                }});

        body.println(2, "Map<String, XExcel> excels = new XReader(excelDir, hook).read();");

        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                XField idField = c.getIdField();
                for (String excel : Util.getExcels(c.getFromExcel())) {
                    if (!allPartExcels.isEmpty() && !allPartExcels.contains(excel)) {
                        continue;
                    }
                    String boxName = new XBean(c.getName()).accept(BoxName.INSTANCE, language);

                    body.println(tab, "{");
                    body.println(tab + 1, String.format("xlite.util.Util.log(\"start export %s\");", excel));
                    body.println(tab + 1, String.format("XExcel excel = excels.get(\"%s\");", excel));
                    String idBoxName = idField.getType().accept(BoxName.INSTANCE, language);
                    body.println(tab +1, String.format("java.util.Map<%s, %s> conf = new java.util.HashMap<>();", idBoxName, boxName));
                    String def = Util.isEmpty(this.def) ? "null" : "\"" + this.def + "\"";
                    body.println(tab + 1, String.format("excel.iterator().forEachRemaining(sheet -> sheet.rows(%s).forEach(row -> {", def));
                    body.println(tab + 2, String.format("%s obj = new %s();", boxName, boxName));
                    body.println(tab + 2, "obj.read(row, 0);");
                    body.println(tab + 2, "try {obj.check();} catch (CheckException e) {throw new RuntimeException(row.toString(), e);}");
                    String idGetter = "get" + Util.firstToUpper(idField.getName());
                    body.println(tab + 2, String.format("conf.put(obj.%s(), obj);", idGetter));
                    body.println(tab + 1, "}));");
                    XClass topParent = c.getTopParent();
                    if (Objects.isNull(topParent)) {
                        topParent = c;
                    }
                    body.println(tab + 1, String.format("%s.export(conf, %s.class, %s.class);", paramGeneratorName, boxName, topParent.getFullName(language)));
                    body.println(tab, "}");
                }
            });
        body.println(tab, String.format("xlite.conf.ForeignCheck.doForeignCheck(%s);", paramGeneratorName));
        body.println(tab, String.format("xlite.conf.MustCheck.doMustCheck(%s);", paramGeneratorName));
        body.print(tab, paramGeneratorName + ".flush();");

        XClass clazz = new XClass("Exporter", root);
        clazz.addImport("java.io.File")
            .addImport("java.util.Map")
            .addImport("xlite.conf.ConfExcelHook")
            .addImport("xlite.excel.XExcel")
            .addImport("xlite.conf.ConfGenerator")
            .addImport("xlite.CheckException")
            .addImport("xlite.excel.XReader");
        XMethod load = new XMethod(exportAllMethodName, clazz);
        XField paramExcelDir = new XField("excelDir", new XBean(File.class), load);
        XField paramGenerator = new XField(paramGeneratorName, new XBean(ConfGenerator.class), load);
        load.staticed()
            .addParam(paramExcelDir)
            .addParam(paramGenerator)
            .throwed("Exception")
            .addBody(body.getString());
        clazz.addMethod(load);
        root.addClass(clazz);
        return clazz;
    }

    private void addLoaderClass(XPackage root) {
        XClass clazz = language.accept(new AddLoaderClass(root));
        root.addClass(clazz);
    }

    public void setDataConf(String dataOut, String dataFormat) {
        this.dataOut = dataOut;
        this.dataFormat = dataFormat;
    }

    public void export(Map<?, ?> conf, Class clazz, Class parent) {
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
        Map<Object, Object> confs = allConf.get(clazz);
        if (Objects.isNull(confs)) {
            confs = new TreeMap<>();
            allConf.put(clazz, confs);
        }
        confs.putAll(conf);
        Util.log(String.format("%s export conf count = %s", clazz.getName(), conf.size()));
    }

    public void flush() throws Exception {
        File dataDir = new File(dataOut);
        for (Map.Entry<Class, Map<Object, Object>> en : allConf.entrySet()) {
            Class clazz = en.getKey();
            Map<Object, Object> conf = en.getValue();
            DataFormatter.createFormatter(dataFormat).export(conf, clazz, dataDir);
        }
    }
}
