package xlite.conf;

import xlite.coder.Scope;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XType;

public class PrintAllMethod implements LanguageVisitor<XMethod> {
    public static final String methodName = "all";

    private final ConfClass clazz;

    public PrintAllMethod(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        XType idType = clazz.getIdField().getType();
        XMethod method = new XMethod(methodName, clazz);
        Writer body = new Writer();
        body.print(String.format("return %s;", PrintLoadMethod.dataFieldName));
        XType returnType = TypeBuilder.build(TypeBuilder.TYPE_MAP, idType.name(), clazz.getName(), null);
        method.staticed()
                .returned(returnType)
                .scope(Scope.PRIVATE)
                .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
