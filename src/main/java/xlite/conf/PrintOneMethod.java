package xlite.conf;

import xlite.coder.Scope;
import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.XBean;
import xlite.type.XType;

public class PrintOneMethod implements LanguageVisitor<XMethod> {
    public static final String methodName = "one";
    private static final String paramIdName = "id";

    private final ConfClass clazz;

    public PrintOneMethod(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        XType idType = clazz.getIdField().getType();
        XMethod method = new XMethod(methodName, clazz);
        Writer body = new Writer();
        body.print(String.format("return %s.get(%s);", PrintLoadMethod.dataFieldName, paramIdName));
        XField paramId = new XField(paramIdName, idType, method);
        method.staticed()
            .scope(Scope.PRIVATE)
            .addParam(paramId)
            .returned(new XBean(clazz.getName()))
            .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
