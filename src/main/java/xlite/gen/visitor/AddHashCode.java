package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.type.TypeBuilder;

public class AddHashCode implements LanguageVisitor<XMethod> {
    private static final String methodName = "hashCode";
    private final XClass clazz;

    public AddHashCode(XClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        if (clazz.getFields().stream().filter(f -> !f.isStaticed()).count() == 0) {
            return null;
        }
        XMethod method = new XMethod(methodName, clazz);
        Writer body = new Writer();
        if (clazz.hasExtend()) {
            body.println(String.format("int hash = super.%s();", methodName));
            body.print(2, "return java.util.Objects.hash(hash, ");
        } else {
            body.print("return java.util.Objects.hash(");
        }
        clazz.getFields().stream().filter(f -> !f.isStaticed()).forEach(field -> body.print(field.getName() + ", "));
        body.deleteEnd(2);
        body.print(");");
        method.overrided()
            .returned(TypeBuilder.INT)
            .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
