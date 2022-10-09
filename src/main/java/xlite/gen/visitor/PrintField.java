package xlite.gen.visitor;

import xlite.coder.XField;
import xlite.language.Java;
import xlite.type.visitor.DefaultValue;
import xlite.type.visitor.SimpleName;
import xlite.util.Util;

public class PrintField implements LanguageVisitor<String> {
    private final XField field;

    public PrintField(XField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        String define;
        String sampleName = field.getType().accept(SimpleName.INSTANCE, java);
        if (field.isConsted()) {
            String defaultVal = field.getType().accept(DefaultValue.INSTANCE, java);
            define = String.format("private static final %s %s = %s;", sampleName, field.getName(), defaultVal);
        } else if (field.isStaticed()) {
            String defaultVal = field.getType().accept(DefaultValue.INSTANCE, java);
            define = String.format("private static %s %s = %s;", sampleName, field.getName(), defaultVal);
        } else {
            define = String.format("private %s %s;", sampleName, field.getName());
        }
        String comment = field.getComment();
        if (Util.notEmpty(comment)) {
            define += String.format(" //%s", comment);
        }
        return define;
    }
}
