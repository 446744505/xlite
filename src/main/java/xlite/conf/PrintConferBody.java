package xlite.conf;

import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.XType;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.DefaultValue;
import xlite.type.visitor.SimpleName;
import xlite.util.Util;

public class PrintConferBody implements LanguageVisitor<Void> {
    public static final String instanceFieldName = "I";

    private final ConfClass clazz;

    public PrintConferBody(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public Void visit(Java java) {
        final int tab = 1;
        Writer body = new Writer();
        XType idType = clazz.getIdField().getType();
        String idDefaultVal = idType.accept(DefaultValue.INSTANCE, java);
        String keyBox = idType.accept(BoxName.INSTANCE, java);
        String keySimple = idType.accept(SimpleName.INSTANCE, java);
        String val = clazz.getName();
        final String varConf = "conf";
        final String paraId = "id";
        body.println(tab, String.format("public static final xlite.conf.Confer<%s, %s> %s = new xlite.conf.Confer<%s, %s>() {", keyBox, val, instanceFieldName, keyBox, val));
        body.println(tab + 1, String.format("@Override public Map<%s, %s> all() {", keyBox, val));
        body.println(tab + 2, String.format("if (!isAllLoaded) {"));
        body.println(tab + 3, String.format("try { %s.%s(%s); } catch (Exception e) {throw new RuntimeException(e);}",
                val, PrintLoadMethod.methodName, idDefaultVal));
        body.println(tab + 2, "}");
        body.println(tab + 2, String.format("return %s.%s();", val, PrintAllMethod.methodName));
        body.println(tab + 1, "};");
        body.println(tab + 1, String.format("public %s one(%s %s) {", val, keySimple, paraId));
        body.println(tab + 2, String.format("%s %s = %s.%s(%s);", val, varConf, val, PrintOneMethod.methodName, paraId));
        body.println(tab + 2, String.format("if (java.util.Objects.nonNull(%s)) {", varConf));
        body.println(tab + 3, String.format("return %s;", varConf));
        body.println(tab + 2, String.format("} else {"));
        body.println(tab + 3, String.format("try { %s.%s(%s); } catch (Exception e) {throw new RuntimeException(e);}",
                val, PrintLoadMethod.methodName, paraId));
        body.println(tab + 3, String.format("return %s.%s(%s);", val, PrintOneMethod.methodName, paraId));
        body.println(tab + 2, String.format("}"));
        body.println(tab + 1, "}");
        body.println(tab, "};");

        //index处理
        for (ConfBeanField field : clazz.getIndexFields()) {
            String fieldName = "Index" + Util.firstToUpper(field.getName());
            String methodName = "get" + Util.firstToUpper(field.getName()) + "()";
            body.println(tab, String.format("public static final xlite.conf.Index<%s, %s> %s = new xlite.conf.Index<>(val -> val.%s);",
                    field.getType().accept(BoxName.INSTANCE, java), val, fieldName, methodName));
        }
        clazz.addContent(body.getString());
        return null;
    }
}
