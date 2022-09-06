package xlite.gen.visitor;

import xlite.language.Java;

public class FileName implements LanguageVisitor<String> {
    private final String fileName;

    public FileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String visit(Java java) {
        return fileName + ".java";
    }
}
