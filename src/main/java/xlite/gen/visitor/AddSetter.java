package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.util.Util;

public class AddSetter implements LanguageVisitor<Void> {
    private final XClass clazz;
    private final XField field;

    public AddSetter(XClass clazz, XField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public Void visit(Java java) {
        String methodName = "set" + Util.firstToUpper(field.getName());
        XMethod setter = new XMethod(methodName, clazz);
        final String paramName = "v";
        setter.returned(TypeBuilder.VOID)
                .addParam(new XField(paramName, field.getType(), setter))
                .addBody(String.format("this.%s = %s;", field.getName(), paramName));
        clazz.addMethod(setter);
        return null;
    }
}
