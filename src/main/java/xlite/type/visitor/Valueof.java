package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class Valueof implements TypeVisitor<String> {
    private final String val;

    public Valueof(String val) {
        this.val = val;
    }

    @Override
    public String visit(XLanguage language, XBool t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XByte t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XEnum t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XRange t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XTime t) {
        return language.valueOf(t, val);
    }

    @Override
    public String visit(XLanguage language, XDate t) {
        return language.valueOf(t, val);
    }
}
