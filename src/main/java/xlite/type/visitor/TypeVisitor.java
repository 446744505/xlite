package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public interface TypeVisitor<R> {
    R visit(XLanguage language, XBool t);
    R visit(XLanguage language, XByte t);
    R visit(XLanguage language, XInt t);
    R visit(XLanguage language, XShort t);
    R visit(XLanguage language, XLong t);
    R visit(XLanguage language, XFloat t);
    R visit(XLanguage language, XDouble t);
    R visit(XLanguage language, XString t);
    R visit(XLanguage language, XList t);
    R visit(XLanguage language, XMap t);
    R visit(XLanguage language, XBean t);
    R visit(XLanguage language, XEnum t);
    R visit(XLanguage language, XVoid t);
    R visit(XLanguage language, XAny t);
}
