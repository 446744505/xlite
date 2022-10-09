package xlite.conf;

import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.XType;
import xlite.type.visitor.BoxName;

public class PrintConferBody implements LanguageVisitor<Void> {
    public static final String fieldName = "I";

    private final ConfClass clazz;

    public PrintConferBody(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public Void visit(Java java) {
        final int tab = 1;
        Writer body = new Writer();
        XType idType = clazz.getIdField().getType();
        String key = idType.accept(BoxName.INSTANCE, java);
        String val = clazz.getName();
        body.println(tab, String.format("public static final xlite.conf.Confer<%s, %s> %s = new xlite.conf.Confer<%s, %s>() {", key, val, fieldName, key, val));
        body.println(tab + 1, String.format("@Override public Map<%s, %s> all() { return %s.%s(); }", key, val, val, PrintAllMethod.methodName));
        body.println(tab + 1, String.format("@Override public %s one(%s id) { return %s.%s(id); }", val, key, val, PrintOneMethod.methodName));
        body.println(tab, "};");
        clazz.addContent(body.getString());
        return null;
    }
}
