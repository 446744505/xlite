package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class SimpleName implements TypeVisitor<String> {
    public static final SimpleName INSTANCE = new SimpleName();

    private SimpleName() {}

    @Override
    public String visit(XLanguage language, XBool t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        return language.simpleName(t);
    }
}
