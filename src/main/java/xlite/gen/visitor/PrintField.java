package xlite.gen.visitor;

import xlite.coder.XField;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;
import xlite.util.Util;

public class PrintField implements LanguageVisitor<String> {
    private final XField field;

    public PrintField(XField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        String define = String.format("private %s %s;", field.getType().accept(SimpleName.INSTANCE, java), field.getName());
        String comment = field.getComment();
        if (Util.notEmpty(comment)) {
            define += String.format(" //%s", comment);
        }
        return define;
    }
}
