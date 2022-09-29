package xlite.conf;

import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XType;
import xlite.type.visitor.SimpleName;

public class PrintEnumValueMethod implements LanguageVisitor<XMethod> {
    public static final String ENUM_METHOD_VALUE = "value";
    private final ConfEnum e;

    public PrintEnumValueMethod(ConfEnum e) {
        this.e = e;
    }

    @Override
    public XMethod visit(Java java) {
        XType inner = e.getInner();
        String fieldName = "fieldName";
        int tab = 2;
        Writer body = new Writer();
        body.println("try {");
        String returnType = inner.accept(SimpleName.INSTANCE, java);
        body.println(tab + 1, String.format("return (%s) %s.class.getField(%s).get(null);", returnType, e.getName(), fieldName));
        body.println(tab, "} catch (Exception e) {");
        body.println(tab + 1, String.format("throw new RuntimeException(%s + \" not exist in enum %s\", e);", fieldName, e.getName()));
        body.print(tab,"}");
        XMethod valuer = new XMethod(ENUM_METHOD_VALUE, e);
        valuer.returned(inner)
                .staticed()
                .addParam(new XField(fieldName, TypeBuilder.STRING, valuer))
                .addBody(body.getString());
        e.addMethod(valuer);
        return valuer;
    }
}
