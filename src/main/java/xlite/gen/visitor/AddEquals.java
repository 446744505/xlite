package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XBean;

public class AddEquals implements LanguageVisitor<XMethod> {
    private static final String methodName = "equals";
    private final XClass clazz;

    public AddEquals(XClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        if (clazz.getFields().stream().filter(f -> !f.isStaticed()).count() == 0) {
            return null;
        }
        final String paramName = "o";
        XMethod method = new XMethod(methodName, clazz);
        Writer body = new Writer();
        if (clazz.hasExtend()) {
            body.println(String.format("if (!super.equals(%s)) return false;", paramName));
        }
        final int tab = 2;
        boolean isFirstLine = body.length() == 0;
        body.println(isFirstLine ? 0 : tab, String.format("if (this == %s) return true;", paramName));
        body.println(tab, String.format("if (%s == null || !(%s instanceof %s)) return false;", paramName, paramName, clazz.getName()));
        body.println(tab, String.format("%s other = (%s) o;", clazz.getName(), clazz.getName()));
        clazz.getFields().stream().filter(f -> !f.isStaticed()).forEach(field -> {
            body.println(tab, String.format("if (!java.util.Objects.equals(%s, other.%s)) return false;", field.getName(), field.getName()));
        });

        body.print(tab, "return true;");
        method.overrided()
                .returned(TypeBuilder.BOOL)
                .addParam(new XField(paramName, new XBean(Object.class), method))
                .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
