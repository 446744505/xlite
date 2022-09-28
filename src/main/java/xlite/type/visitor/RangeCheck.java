package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class RangeCheck implements TypeVisitor<String> {
    private final String fieldName;
    private final String format;

    public RangeCheck(String fieldName, String format) {
        this.fieldName = fieldName;
        this.format = format;
    }

    @Override
    public String visit(XLanguage language, XBool t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XByte t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XEnum t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XRange t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XTime t) {
        return language.rangeCheck(t, fieldName, format);
    }

    @Override
    public String visit(XLanguage language, XDate t) {
        return language.rangeCheck(t, fieldName, format);
    }
}
