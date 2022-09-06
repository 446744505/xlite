package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class SimpleName implements TypeVisitor<String> {
    @Override
    public String visit(XLanguage language, XInt t) {
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
    public String visit(XLanguage language, XBean t) {
        return language.simpleName(t);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.simpleName(t);
    }
}
