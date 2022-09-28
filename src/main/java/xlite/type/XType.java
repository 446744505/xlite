package xlite.type;

import xlite.language.XLanguage;
import xlite.type.visitor.TypeVisitor;

public interface XType {
    <T> T accept(TypeVisitor<T> visitor, XLanguage language);
    String name();
}
