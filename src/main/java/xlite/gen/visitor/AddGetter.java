package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.Java;
import xlite.util.Util;

public class AddGetter implements LanguageVisitor<Void> {
    private final XClass clazz;
    private final XField field;

    public AddGetter(XClass clazz, XField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public Void visit(Java java) {
        String methodName = "get" + Util.firstToUpper(field.getName());
        XMethod getter = new XMethod(methodName, clazz);
        getter.returned(field.getType())
                .addBody(String.format("return %s;", field.getName()));
        clazz.addMethod(getter);
        return null;
    }
}
