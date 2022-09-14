package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public class BoxName implements TypeVisitor<String> {
    public static final BoxName INSTANCE = new BoxName();

    private BoxName() {}

    @Override
    public String visit(XLanguage language, XInt t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XList t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        return language.boxName(t);
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        return language.boxName(t);
    }
}
