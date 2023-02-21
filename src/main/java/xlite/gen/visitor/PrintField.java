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
        String defaultVal = field.getDefaultVal();
        if (Util.isEmpty(defaultVal)) {
            defaultVal = field.getType().accept(DefaultValue.INSTANCE, java);
        }
        if (field.isConsted()) {
            define = String.format("public static final %s %s = %s;", sampleName, field.getName(), defaultVal);
        } else if (field.isStaticed()) {
            define = String.format("%s static %s %s = %s;", field.isPublic() ? "public" : "private", sampleName, field.getName(), defaultVal);
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
