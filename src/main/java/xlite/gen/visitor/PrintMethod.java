package xlite.gen.visitor;

import xlite.coder.XMethod;
import xlite.gen.GenContext;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;

public class PrintMethod implements LanguageVisitor<Void> {
    private final XMethod method;
    private final GenContext context;

    public PrintMethod(XMethod method, GenContext context) {
        this.method = method;
        this.context = context;
    }

    @Override
    public Void visit(Java java) {
        int tab = 1;
        String returned = method.getReturned().accept(SimpleName.INSTANCE, context.getLanguage());
        StringBuilder param = new StringBuilder();
        method.getParams().forEach(p -> {
            String t = p.getType().accept(SimpleName.INSTANCE, java);
            param.append(t).append(" ").append(p.getName()).append(", ");
        });
        if (param.length() > 0) {
            param.delete(param.length() - 2, param.length());
        }
        if (method.isOverride()) {
            context.println(tab, "@Override");
        }
        if (method.isStatic()) {
            context.println(tab, String.format("public static %s %s(%s) {", returned, method.getName(), param.toString()));
        } else {
            context.println(tab, String.format("public %s %s(%s) {", returned, method.getName(), param.toString()));
        }
        context.println(tab + 1, method.getBody());
        context.println(tab, "}");
        return null;
    }
}
