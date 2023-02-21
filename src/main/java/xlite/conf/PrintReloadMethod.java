package xlite.conf;

import xlite.coder.XField;
import xlite.coder.XMethod;
import xlite.gen.Writer;
import xlite.gen.visitor.LanguageVisitor;
import xlite.language.Java;
import xlite.type.TypeBuilder;
import xlite.type.visitor.DefaultValue;

public class PrintReloadMethod implements LanguageVisitor<XMethod> {
    public static final String methodName = "reload";

    private final ConfClass clazz;

    public PrintReloadMethod(ConfClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public XMethod visit(Java java) {
        final String paramIsLoadAll = "isLoadAll";
        XMethod method = new XMethod(methodName, clazz);

        XField isLoadAll = new XField(paramIsLoadAll, TypeBuilder.BOOL, method);
        method.addParam(isLoadAll);

        int tab = 2;
        Writer body = new Writer();
        String idDefaultVal = clazz.getIdField().getType().accept(DefaultValue.INSTANCE, java);
        body.println(String.format("if (%s) {", paramIsLoadAll));
        body.println(tab + 1, String.format(PrintLoadMethod.methodName + "(%s);", idDefaultVal));
        body.println(tab,"} else {");
        body.println(tab + 1, String.format("%s.reset();", PrintConferBody.instanceFieldName));
        body.println(tab + 1, String.format("%s = Collections.unmodifiableMap(Collections.EMPTY_MAP);", PrintLoadMethod.dataFieldName));
        body.print(tab,  "}");
        method.staticed()
            .addBody(body.getString())
            .throwed("Exception");
        clazz.addMethod(method);
        return method;
    }

}
