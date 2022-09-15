package xlite.gen.visitor;

import xlite.coder.XField;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;

import java.util.Objects;

public class Field implements LanguageVisitor<String> {
    private final XField field;

    public Field(XField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        String define = String.format("private %s %s;", field.getType().accept(SimpleName.INSTANCE, java), field.getName());
        String comment = field.getComment();
        if (!Objects.isNull(comment) && !comment.isEmpty()) {
            define += String.format(" //%s", comment);
        }
        return define;
    }
}
