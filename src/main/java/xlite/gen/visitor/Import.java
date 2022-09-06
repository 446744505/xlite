package xlite.gen.visitor;

import xlite.language.Java;

public class Import implements LanguageVisitor<String> {
    private final String line;

    public Import(String line) {
        this.line = line;
    }

    @Override
    public String visit(Java java) {
        return "import " + line + ";";
    }
}
