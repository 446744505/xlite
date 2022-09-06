package xlite.gen.visitor;

import xlite.coder.XField;
import xlite.language.Java;
import xlite.type.visitor.SimpleName;

public class Field implements LanguageVisitor<String> {
    private final XField field;

    public Field(XField field) {
        this.field = field;
    }

    @Override
    public String visit(Java java) {
        return String.format("private %s %s;", field.getType().accept(new SimpleName(), java), field.getName());
    }
}
