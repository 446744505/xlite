package xlite.gen.visitor;

import xlite.coder.XClass;
import xlite.gen.GenContext;
import xlite.language.Java;

public class PrintConstructor implements LanguageVisitor<Void> {
    private final XClass clazz;
    private final GenContext context;

    public PrintConstructor(XClass clazz, GenContext context) {
        this.clazz = clazz;
        this.context = context;
    }

    @Override
    public Void visit(Java java) {
        int tab = 1;
        context.println(tab, String.format("public %s() {", clazz.getName()));
        clazz.getFields().stream().filter(f -> !f.isStaticed()).forEach(f -> {
            context.println(tab + 1, String.format("this.%s = %s;", f.getName(), java.accept(new DefaultValue(f.getType()))));
        });
        context.println(tab, "}");
        return null;
    }
}
