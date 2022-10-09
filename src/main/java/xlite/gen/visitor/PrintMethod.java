package xlite.gen.visitor;

import xlite.coder.XMethod;
import xlite.gen.GenContext;
import xlite.gen.Writer;
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
        Writer exWriter = new Writer();
        if (!method.getExceptions().isEmpty()) {
            exWriter.print(" throws ");
            method.getExceptions().forEach(ex -> {
                exWriter.print(ex, ",");
            });
            exWriter.deleteEnd(1);
        }

        String scope = method.getScope().scopeName(java);
        if (method.isStatic()) {
            context.println(tab, String.format("%s static %s %s(%s)%s {", scope, returned, method.getName(), param.toString(), exWriter.getString()));
        } else {
            context.println(tab, String.format("%s %s %s(%s)%s {", scope, returned, method.getName(), param.toString(), exWriter.getString()));
        }
        context.println(tab + 1, method.getBody());
        context.println(tab, "}");
        return null;
    }
}
