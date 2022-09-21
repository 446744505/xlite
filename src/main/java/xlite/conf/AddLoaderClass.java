package xlite.conf;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.coder.XPackage;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.XBean;
import xlite.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddLoaderClass implements LanguageVisitor<XClass> {
    private final XPackage root;

    public AddLoaderClass(XPackage root) {
        this.root = root;
    }

    @Override
    public XClass visit(Java java) {
        final String paramDataDirName = "dataDir";
        Writer body = new Writer();
        List<XClass> allClass = new ArrayList<>();
        root.getAllClass(allClass);
        allClass.stream()
                .map(c -> (ConfClass)c)
                .filter(c -> Util.notEmpty(c.getFromExcel()))
                .forEach(c -> {
                    int tab = body.length() == 0 ? 0 : 2;
                    String boxName = c.getFullName(java);
                    body.println(tab, String.format("%s.%s(%s);", boxName, PrintLoadMethod.methodName, paramDataDirName));
                });
        body.deleteEnd(1);

        XClass clazz = new XClass("Loader", root);
        clazz.addImport("java.io.File");
        XMethod loader = new XMethod("loadAll", clazz);
        XField paramDataDir = new XField(paramDataDirName, new XBean(File.class), loader);
        loader.staticed()
            .addParam(paramDataDir)
            .throwed("Exception")
            .addBody(body.getString());
        clazz.addMethod(loader);
        return clazz;
    }
}
