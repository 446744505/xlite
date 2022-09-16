package xlite.gen.visitor;

import xlite.coder.XEnumField;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;
import xlite.util.Util;

public class Enumer implements LanguageVisitor<String> {
    private final XEnumField field;

    public Enumer(XEnumField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        String define = String.format("public static final %s %s = %s;",
                field.getType().accept(SimpleName.INSTANCE, java), field.getName(), field.getValue());
        String comment = field.getComment();
        if (Util.notEmpty(comment)) {
            define += String.format(" //%s", comment);
        }
        return define;
    }
}
