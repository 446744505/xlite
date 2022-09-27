package xlite.conf;

import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XBean;
import xlite.type.XType;
import xlite.type.visitor.BoxName;

import java.io.File;

public class PrintLoadMethod implements LanguageVisitor<XMethod> {
    public static final String methodName = "load";
    public static final String dataFieldName = "confs";
    private static final String dataDirName = "dataDir";

    private final ConfClass clazz;

    public PrintLoadMethod(ConfClass clazz) {
        this.clazz = clazz;
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
                .addImport("java.util.Collections")
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
        body.println(tab, String.format("Map<%s, %s> conf = formatter.load(file, new TypeReference<Map<%s, %s>>(){});", keyBoxName, clazz.getName(), keyBoxName, clazz.getName()));
        body.print(tab, String.format("%s = conf;", dataFieldName));
        XField paramDataDir = new XField(dataDirName, new XBean(File.class), method);
        method.staticed()
            .addParam(paramDataDir)
            .addBody(body.getString())
            .throwed("Exception");
        clazz.addMethod(method);
        return method;
    }

}
