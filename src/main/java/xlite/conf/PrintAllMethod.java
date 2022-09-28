package xlite.conf;

import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.XType;

public class PrintAllMethod implements LanguageVisitor<XMethod> {
    private final ConfClass clazz;

    public PrintAllMethod(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        XType idType = clazz.getIdField().getType();
        XMethod method = new XMethod("all" + clazz.getName(), clazz);
        Writer body = new Writer();
        body.print(String.format("return Collections.unmodifiableMap(%s);", PrintLoadMethod.dataFieldName));
        XType returnType = TypeBuilder.build(TypeBuilder.TYPE_MAP, idType.name(), clazz.getName(), null);
        method.staticed()
                .returned(returnType)
                .addBody(body.getString());
        clazz.addMethod(method);
        return method;
    }
}
