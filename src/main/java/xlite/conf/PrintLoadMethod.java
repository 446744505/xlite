package xlite.conf;

import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.TypeVisitor;

import java.io.File;

public class PrintLoadMethod implements LanguageVisitor<XMethod>, TypeVisitor<String> {
    private static final String methodName = "load";
    private static final String dataFieldName = "confs";
    private static final String dataDirName = "dataDir";

    private final ConfClass clazz;
    private final ConfBeanField field;

    public PrintLoadMethod(ConfClass clazz, ConfBeanField field) {
        this.clazz = clazz;
        this.field = field;
    }

    public void make() {
        Java.INSTANCE.accept(this);
    }

    @Override
    public XMethod visit(Java java) {
        XType idType = clazz.getIdField().getType();
        XType dataFieldType = TypeBuilder.build(TypeBuilder.TYPE_MAP, idType.name(), clazz.getName(), null);
        XField dataField = new XField(dataFieldName, dataFieldType, clazz);
        dataField.staticed();
        clazz.addImport("com.fasterxml.jackson.core.type.TypeReference")
                .addImport("org.apache.commons.io.FilenameUtils")
                .addImport("xlite.conf.formatter.DataFormatter")
                .addImport("java.io.File")
                .addImport("java.io.FilenameFilter")
                .addImport("java.util.Map")
                .addField(dataField);
        XMethod method = new XMethod(methodName, clazz);
        int tab = 2;
        Writer body = new Writer();
        body.println(String.format("File file = %s.listFiles(new FilenameFilter() {", dataDirName));
        body.println(tab + 1, "@Override public boolean accept(File dir, String name) {");
        body.println(tab + 2, String.format("return name.startsWith(\"%s\");", clazz.getName()));
        body.println(tab + 1, "}");
        body.println(tab, "})[0];");
        body.println(tab, "String ext = FilenameUtils.getExtension(file.getName());");
        body.println(tab, "DataFormatter formatter = DataFormatter.createFormatter(ext);");
        String keyBoxName = idType.accept(BoxName.INSTANCE, java);
        body.print(tab, String.format("%s.putAll(formatter.load(file, new TypeReference<Map<%s, %s>>(){}));", dataFieldName, keyBoxName, clazz.getName()));
        XField paramDataDir = new XField(dataDirName, new XBean(File.class), method);
        method.staticed()
            .addParam(paramDataDir)
            .addBody(body.getString())
            .throwed("Exception");
        clazz.addMethod(method);
        return method;
    }

    @Override
    public String visit(XLanguage language, XBool t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XByte t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XEnum t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return null;
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        return null;
    }
}
