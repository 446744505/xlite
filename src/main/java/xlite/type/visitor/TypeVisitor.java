package xlite.type.visitor;

import xlite.language.XLanguage;
import xlite.type.*;

public interface TypeVisitor<R> {
    R visit(XLanguage language, XInt t);
    R visit(XLanguage language, XFloat t);
    R visit(XLanguage language, XString t);
    R visit(XLanguage language, XList t);
    R visit(XLanguage language, XMap t);
    R visit(XLanguage language, XBean t);
    R visit(XLanguage language, XVoid t);
}
