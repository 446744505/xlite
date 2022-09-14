package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.Java;
import xlite.type.TypeBuilder;

public class AddToString implements LanguageVisitor<Void> {
    private final XClass clazz;
    private final XField field;

    public AddToString(XClass clazz, XField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public Void visit(Java java) {
        String methodName = "toString";
        XMethod toString = new XMethod(methodName, clazz);
        String init = clazz.hasExtend() ? "super.toString()" : "";
        StringBuilder body = new StringBuilder(String.format("StringBuilder sb = new StringBuilder(%s);\n", init));
        for (XField field : clazz.getFields()) {
            body.append(String.format("        sb.append(\"%s=\").append(this.%s).append(\",\");\n", field.getName(), field.getName()));
        }
        body.append("        return sb.toString();");
        toString.overrided()
                .returned(TypeBuilder.STRING)
                .addBody(body.toString());
        clazz.addMethod(toString);
        return null;
    }
}
