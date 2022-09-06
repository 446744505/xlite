package xlite.gen.visitor;

import xlite.language.Java;
import xlite.type.XType;

public class DefaultValue implements LanguageVisitor<String> {
    private final XType type;

    public DefaultValue(XType type) {
        this.type = type;
    }

    @Override
    public String visit(Java java) {
        return type.accept(new xlite.type.visitor.DefaultValue(), java);
    }
}
