package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.Java;
import xlite.type.TypeBuilder;

public class AddToString implements LanguageVisitor<XMethod> {
    private final XClass clazz;
    private final XField field;

    public AddToString(XClass clazz, XField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public XMethod visit(Java java) {
        if (clazz.getFields().stream().filter(f -> !f.isStaticed()).count() == 0) {
            return null;
        }
        String methodName = "toString";
        XMethod toString = new XMethod(methodName, clazz);
        String init = clazz.hasExtend() ? "super.toString()" : "";
        StringBuilder body = new StringBuilder(String.format("StringBuilder sb = new StringBuilder(%s);\n", init));
        clazz.getFields().stream().filter(f -> !f.isStaticed()).forEach(f -> {
            body.append(String.format("        sb.append(\"%s=\").append(this.%s).append(\",\");\n", f.getName(), f.getName()));
        });
        body.append("        return sb.toString();");
        toString.overrided()
                .returned(TypeBuilder.STRING)
                .addBody(body.toString());
        clazz.addMethod(toString);
        return toString;
    }
}
