package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.type.TypeBuilder;

public class AddToString implements LanguageVisitor<XMethod> {
    private static final String methodName = "toString";
    private final XClass clazz;

    public AddToString(XClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        if (clazz.getFields().stream().filter(f -> !f.isStaticed()).count() == 0) {
            return null;
        }
        XMethod method = new XMethod(methodName, clazz);
        String init = clazz.hasExtend() ? "super.toString()" : "";
        Writer body = new Writer();
        body.println(String.format("StringBuilder sb = new StringBuilder(%s);", init));
        final int tab = 2;
        clazz.getFields().stream().filter(f -> !f.isStaticed()).forEach(f -> {
            body.println(tab, String.format("sb.append(\"%s=\").append(this.%s).append(\",\");", f.getName(), f.getName()));
        });
        body.print(tab, "return sb.toString();");
        method.overrided()
                .returned(TypeBuilder.STRING)
                .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
