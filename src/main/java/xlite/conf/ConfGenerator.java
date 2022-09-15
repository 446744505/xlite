package xlite.conf;

import org.dom4j.DocumentException;
import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.coder.XPackage;
import xlite.gen.Writer;
import xlite.gen.XGenerator;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.type.visitor.BoxName;
import xlite.xml.XParser;
import xlite.xml.XmlContext;
import xlite.xml.element.PackageElement;
import xlite.xml.element.XElement;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfGenerator {
    public static final String ENDPOINT_ALL = "a";
    public static final String ENDPOINT_SERVER = "s";
    public static final String ENDPOINT_CLIENT = "c";

    private final XParser parser;
    private final XGenerator generator;
    private final XmlContext context;

    public ConfGenerator(URL xml, String out, String language) throws DocumentException {
        ConfFactory factory = new ConfFactory();
        context = new XmlContext(factory);
        parser = new XParser(xml, context);
        generator = new XGenerator(out, language, factory);
    }

    public void gen(boolean isLoadCode, String endPoint) {
        XElement root = parser.parse();
        if (!(root instanceof PackageElement)) {
            throw new RuntimeException("conf xml root must be package");
        }
        context.setConfLoadCode(isLoadCode);
        context.setEndPoint(endPoint);
        PackageElement packageElement = (PackageElement) root;
        XPackage xPackage = packageElement.build(context);
        if (isLoadCode) {
            genInit(xPackage);
        }
        generator.gen(xPackage);
    }

    private void genInit(XPackage root) {
        Writer body = new Writer();
        body.println( "Map<String, XExcel> excels = new XReader(excelDir, new ConfExcelHook()).read();");
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
            .map(c -> (ConfClass)c)
            .forEach(c -> {
                String fromExcel = c.getFromExcel();
                if (!Objects.isNull(fromExcel) && !fromExcel.isEmpty()) {
                    for (String excel : fromExcel.split(",")) {
                        if (Objects.isNull(excel) || excel.trim().isEmpty()) {
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
