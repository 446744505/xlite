package xlite.gen.visitor;


import xlite.language.Java;

public interface LanguageVisitor<R> {
    R visit(Java java);
}
