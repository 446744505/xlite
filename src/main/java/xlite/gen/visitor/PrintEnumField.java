package xlite.gen.visitor;

import xlite.coder.XEnumField;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;
import xlite.type.visitor.Valueof;
import xlite.util.Util;

public class PrintEnumField implements LanguageVisitor<String> {
    private final XEnumField field;

    public PrintEnumField(XEnumField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        String val = field.getType().accept(new Valueof(field.getValue()), java);
        String define = String.format("public static final %s %s = %s;",
                field.getType().accept(SimpleName.INSTANCE, java), field.getName(), val);
        String comment = field.getComment();
        if (Util.notEmpty(comment)) {
            define += String.format(" //%s", comment);
        }
        return define;
    }
}
