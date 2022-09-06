package xlite.language;

import xlite.gen.visitor.LanguageVisitor;
import xlite.type.*;

public interface XLanguage {
    <T> T accept(LanguageVisitor<T> visitor);

    String simpleName(XInt t);
    String boxName(XInt t);
    String defaultValue(XInt t);

    String simpleName(XFloat t);
    String boxName(XFloat t);
    String defaultValue(XFloat t);

    String simpleName(XString t);
    String boxName(XString t);
    String defaultValue(XString t);

    String simpleName(XList t);
    String boxName(XList t);
    String defaultValue(XList t);

    String simpleName(XMap t);
    String boxName(XMap t);
    String defaultValue(XMap t);

    String simpleName(XBean t);
    String boxName(XBean t);
    String defaultValue(XBean t);

    String simpleName(XVoid t);
    String boxName(XVoid t);
    String defaultValue(XVoid t);
}
