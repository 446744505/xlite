package xlite.conf;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.coder.XPackage;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.type.visitor.DefaultValue;
import xlite.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddLoaderClass implements LanguageVisitor<XClass> {
    private final XPackage root;

    public static final String className = "Loader";
    public static final String dataDirName = "dataDir";
    public static final String isLoadAllName = "isLoadAll";

    public AddLoaderClass(XPackage root) {
        this.root = root;
    }

    @Override
    public XClass visit(Java java) {
        XClass clazz = new XClass(className, root);
        clazz.addImport("java.io.File");

        XField staticDataDir = new XField(dataDirName, new XBean(File.class), clazz).staticed();
        staticDataDir.setPublic(true);
        staticDataDir.setDefaultVal("null");
        clazz.addField(staticDataDir);

        XField staticIsLoadAll = new XField(isLoadAllName, TypeBuilder.BOOL, clazz).staticed();
        clazz.addField(staticIsLoadAll);

        printLoadAllMethod(java, clazz);
        printReloadMethod(java, clazz);

        return clazz;
    }

    private void printLoadAllMethod(Java java, XClass clazz) {
        Writer body = new Writer();
        body.println(String.format("%s.%s = true;", className, isLoadAllName));
        body.println(2, String.format("%s.%s = %s;", className, dataDirName, dataDirName));
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .filter(c -> Util.notEmpty(c.getFromExcel()))
                .forEach(c -> {
                    int tab = body.length() == 0 ? 0 : 2;
                    String boxName = c.getFullName(java);
                    String idDefaultVal = c.getIdField().getType().accept(DefaultValue.INSTANCE, java);
                    body.println(tab, String.format("%s.%s(%s);", boxName, PrintLoadMethod.methodName, idDefaultVal));
                });
        body.deleteEnd(1);

        XMethod loader = new XMethod("loadAll", clazz);
        XField paramDataDir = new XField(dataDirName, new XBean(File.class), loader);
        loader.staticed()
                .addParam(paramDataDir)
                .throwed("Exception")
                .addBody(body.getString());
        clazz.addMethod(loader);
    }

    private void printReloadMethod(Java java, XClass clazz) {
        Writer body = new Writer();
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .filter(c -> Util.notEmpty(c.getFromExcel()))
                .forEach(c -> {
                    int tab = body.length() == 0 ? 0 : 2;
                    String boxName = c.getFullName(java);
                    body.println(tab, String.format("%s.%s(%s);", boxName, PrintReloadMethod.methodName, isLoadAllName));
                });
        body.deleteEnd(1);

        XMethod loader = new XMethod("reload", clazz);
        loader.staticed()
                .throwed("Exception")
                .addBody(body.getString());
        clazz.addMethod(loader);
    }
}
