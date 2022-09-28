package xlite.language;

import xlite.gen.visitor.LanguageVisitor;
import xlite.type.*;

public interface XLanguage {
    <T> T accept(LanguageVisitor<T> visitor);

    String simpleName(XBool t);
    String boxName(XBool t);
    String defaultValue(XBool t);
    String valueOf(XBool t, String v);

    String simpleName(XByte t);
    String boxName(XByte t);
    String defaultValue(XByte t);
    String valueOf(XByte t, String v);

    String simpleName(XShort t);
    String boxName(XShort t);
    String defaultValue(XShort t);
    String valueOf(XShort t, String v);

    String simpleName(XInt t);
    String boxName(XInt t);
    String defaultValue(XInt t);
    String valueOf(XInt t, String v);

    String simpleName(XLong t);
    String boxName(XLong t);
    String defaultValue(XLong t);
    String valueOf(XLong t, String v);

    String simpleName(XFloat t);
    String boxName(XFloat t);
    String defaultValue(XFloat t);
    String valueOf(XFloat t, String v);

    String simpleName(XDouble t);
    String boxName(XDouble t);
    String defaultValue(XDouble t);
    String valueOf(XDouble t, String v);

    String simpleName(XString t);
    String boxName(XString t);
    String defaultValue(XString t);
    String valueOf(XString t, String v);

    String simpleName(XList t);
    String boxName(XList t);
    String defaultValue(XList t);
    String valueOf(XList t, String v);

    String simpleName(XMap t);
    String boxName(XMap t);
    String defaultValue(XMap t);
    String valueOf(XMap t, String v);

    String simpleName(XBean t);
    String boxName(XBean t);
    String defaultValue(XBean t);
    String valueOf(XBean t, String v);

    String simpleName(XVoid t);
    String boxName(XVoid t);
    String defaultValue(XVoid t);
    String valueOf(XVoid t, String v);

    String simpleName(XAny t);
    String boxName(XAny t);
    String defaultValue(XAny t);
    String valueOf(XAny t, String v);

    String simpleName(XEnum t);
    String boxName(XEnum t);
    String defaultValue(XEnum t);
    String valueOf(XEnum t, String v);

    String simpleName(XRange t);
    String boxName(XRange t);
    String defaultValue(XRange t);
    String valueOf(XRange t, String v);

    String simpleName(XTime t);
    String boxName(XTime t);
    String defaultValue(XTime t);
    String valueOf(XTime t, String v);

    String simpleName(XDate t);
    String boxName(XDate t);
    String defaultValue(XDate t);
    String valueOf(XDate t, String v);
}
