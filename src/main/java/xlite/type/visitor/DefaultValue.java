package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class DefaultValue implements TypeVisitor<String> {
    public static final DefaultValue INSTANCE = new DefaultValue();

    private DefaultValue() {}

    @Override
    public String visit(XLanguage language, XBool t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.defaultValue(t);
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        return language.defaultValue(t);
    }
}
