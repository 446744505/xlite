package xlite.conf;

import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XString;
import xlite.type.XType;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.DefaultValue;
import xlite.util.Util;

public class PrintLoadMethod implements LanguageVisitor<XMethod> {
    public static final String methodName = "load";
    public static final String dataFieldName = "confs";

    private final ConfClass clazz;

    public PrintLoadMethod(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        final String idName = "id";
        XType idType = clazz.getIdField().getType();
        XType dataFieldType = TypeBuilder.build(TypeBuilder.TYPE_MAP, idType.name(), clazz.getName(), null);
        XField dataField = new XField(dataFieldName, dataFieldType, clazz);
        dataField.staticed();
        clazz.addImport("com.fasterxml.jackson.core.type.TypeReference")
                .addImport("org.apache.commons.io.FilenameUtils")
                .addImport("xlite.conf.formatter.DataFormatter")
                .addImport("xlite.conf.SplitMeta")
                .addImport("java.io.File")
                .addImport("java.io.FilenameFilter")
                .addImport("java.util.Collections")
                .addImport("java.util.Map")
                .addImport("java.util.TreeMap")
                .addImport("java.util.Objects")
                .addField(dataField);
        XMethod method = new XMethod(methodName, clazz);
        int tab = 2;
        Writer body = new Writer();
        final String varFilesName = "files";
        body.println(String.format("File[] %s = %s.%s.%s.listFiles(new FilenameFilter() {", varFilesName,
                clazz.getFullName(java).split("\\.")[0], AddLoaderClass.className, AddLoaderClass.dataDirName));
        body.println(tab + 1, "@Override public boolean accept(File dir, String name) {");
        body.println(tab + 2, String.format("return name.startsWith(\"%s\");", clazz.getName()));
        body.println(tab + 1, "}");
        body.println(tab, "});");

        final String varConfName = "conf";
        final String varRefName = "ref";
        String keyBoxName = idType.accept(BoxName.INSTANCE, java);
        body.println(tab, String.format("Map<%s, %s> %s;", keyBoxName, clazz.getName(), varConfName));
        body.println(tab, String.format("TypeReference %s = new TypeReference<TreeMap<%s, %s>>() {};", varRefName, keyBoxName, clazz.getName()));
        if (idType instanceof XString) {
            body.println(tab, String.format("if (Objects.isNull(%s)) {", idName));
        } else {
            String idDefaultVal = clazz.getIdField().getType().accept(DefaultValue.INSTANCE, java);
            body.println(tab, String.format("if (%s == %s) {", idName, idDefaultVal));
        }
        body.println(tab + 1, String.format("%s = %s.loadAll(%s, %s);",
                varConfName, PrintConferBody.instanceFieldName, varFilesName, varRefName));
        body.println(tab, "} else {");
        body.println(tab + 1, String.format("%s = %s.load(%s, %s, %s, new TypeReference<SplitMeta<%s>>() {});",
                varConfName, PrintConferBody.instanceFieldName, varFilesName, idName, varRefName, keyBoxName));
        body.println(tab + 1, String.format("%s.putAll(%s);", varConfName, dataFieldName));
        body.println(tab + 1, String.format("%s.freshIsLoadAll();", PrintConferBody.instanceFieldName));
        body.println(tab, "}");
        body.println(tab, String.format("%s = Collections.unmodifiableMap(%s);", dataFieldName, varConfName));

        for (ConfBeanField field : clazz.getIndexFields()) {
            String fieldName = "Index" + Util.firstToUpper(field.getName());
            body.println(tab, String.format("%s.index(conf);", fieldName));
        }
        body.deleteEnd(1);

        XField loadIdParam = new XField(idName, idType, method);

        method.staticed()
            .addParam(loadIdParam)
            .addBody(body.getString())
            .throwed("Exception");
        clazz.addMethod(method);
        return method;
    }

}
