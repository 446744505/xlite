package xlite.coder;

import xlite.language.XLanguage;

public enum Scope {
    PRIVATE,
    PROTECTED,
    PACKAGE,
    PUBLIC;

    public String scopeName(XLanguage language) {
        return language.scopeName(this);
    }
}
