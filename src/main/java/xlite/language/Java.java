package xlite.language;

import xlite.coder.XClass;
import xlite.gen.visitor.LanguageVisitor;
import xlite.type.*;
import xlite.type.visitor.BoxName;

import java.util.Objects;

public class Java implements XLanguage {
    public static final Java INSTANCE = new Java();

    private Java() {}

    @Override
    public <T> T accept(LanguageVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String simpleName(XInt t) {
        return "int";
    }

    @Override
    public String simpleName(XString t) {
        return "String";
    }

    @Override
    public String simpleName(XList t) {
        String boxName = t.getValue().accept(BoxName.INSTANCE, this);
        return String.format("java.util.List<%s>", boxName);
    }

    @Override
    public String boxName(XInt t) {
        return "Integer";
    }

    @Override
    public String boxName(XString t) {
        return "String";
    }

    @Override
    public String boxName(XList t) {
        throw new UnsupportedOperationException("list cannot be placed in a container");
    }

    @Override
    public String defaultValue(XInt t) {
        return "0";
    }

    @Override
    public String simpleName(XFloat t) {
        return "float";
    }

    @Override
    public String boxName(XFloat t) {
        return "Float";
    }

    @Override
    public String defaultValue(XFloat t) {
        return "0f";
    }

    @Override
    public String defaultValue(XString t) {
        return "\"\"";
    }

    @Override
    public String defaultValue(XList t) {
        return "new java.util.ArrayList<>()";
    }

    @Override
    public String simpleName(XMap t) {
        String keyBoxName = t.getKey().accept(BoxName.INSTANCE, this);
        String valueBoxName = t.getValue().accept(BoxName.INSTANCE, this);
        return String.format("java.util.Map<%s, %s>", keyBoxName, valueBoxName);
    }

    @Override
    public String boxName(XMap t) {
        throw new UnsupportedOperationException("map cannot be placed in a container");
    }

    @Override
    public String defaultValue(XMap t) {
        return "new java.util.HashMap<>()";
    }

    @Override
    public String simpleName(XBean t) {
        Class clazz = t.getClazz();
        if (Objects.nonNull(clazz)) {
            return clazz.getSimpleName();
        }
        return XClass.getFullName(t.getName(), this);
    }

    @Override
    public String boxName(XBean t) {
        Class clazz = t.getClazz();
        if (Objects.nonNull(clazz)) {
            return clazz.getName();
        }
        return XClass.getFullName(t.getName(), this);
    }

    @Override
    public String defaultValue(XBean t) {
        return String.format("new %s()", boxName(t));
    }

    @Override
    public String simpleName(XVoid t) {
        return "void";
    }

    @Override
    public String boxName(XVoid t) {
        return "Void";
    }

    @Override
    public String defaultValue(XVoid t) {
        throw new UnsupportedOperationException("void cannot be a value");
    }
}
