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
    public String simpleName(XBool t) {
        return "boolean";
    }

    @Override
    public String boxName(XBool t) {
        return "Boolean";
    }

    @Override
    public String defaultValue(XBool t) {
        return "false";
    }

    @Override
    public String simpleName(XByte t) {
        return "byte";
    }

    @Override
    public String boxName(XByte t) {
        return "Byte";
    }

    @Override
    public String defaultValue(XByte t) {
        return "0";
    }

    @Override
    public String simpleName(XShort t) {
        return "short";
    }

    @Override
    public String boxName(XShort t) {
        return "Short";
    }

    @Override
    public String defaultValue(XShort t) {
        return "0";
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
    public String simpleName(XLong t) {
        return "long";
    }

    @Override
    public String boxName(XLong t) {
        return "Long";
    }

    @Override
    public String defaultValue(XLong t) {
        return "0l";
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
    public String simpleName(XDouble t) {
        return "double";
    }

    @Override
    public String boxName(XDouble t) {
        return "Double";
    }

    @Override
    public String defaultValue(XDouble t) {
        return "0";
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

    @Override
    public String simpleName(XAny t) {
        return simpleName(t.getValue());
    }

    @Override
    public String boxName(XAny t) {
        return boxName(t.getValue());
    }

    @Override
    public String defaultValue(XAny t) {
        return defaultValue(t.getValue());
    }

    @Override
    public String simpleName(XEnum t) {
        t.assertInner();
        return simpleName(t.getInner());
    }

    @Override
    public String boxName(XEnum t) {
        t.assertInner();
        return boxName(t.getInner());
    }

    @Override
    public String defaultValue(XEnum t) {
        t.assertInner();
        return defaultValue(t.getInner());
    }

    @Override
    public String simpleName(XRange t) {
        String valBoxName = t.getValue().accept(BoxName.INSTANCE, this);
        return String.format("xlite.type.inner.Range<%s>", valBoxName);
    }

    @Override
    public String boxName(XRange t) {
        String valBoxName = t.getValue().accept(BoxName.INSTANCE, this);
        return String.format("xlite.type.inner.Range<%s>", valBoxName);
    }

    @Override
    public String defaultValue(XRange t) {
        String valBoxName = t.getValue().accept(BoxName.INSTANCE, this);
        return String.format("xlite.type.inner.Range.toRange(\"%s\", \"\")", valBoxName);
    }

    private String simpleName(XType inner) {
        if (inner instanceof XBool) {
            return simpleName((XBool) inner);
        } else if (inner instanceof XByte) {
            return simpleName((XByte) inner);
        } else if (inner instanceof XShort) {
            return simpleName((XShort) inner);
        } else if (inner instanceof XInt) {
            return simpleName((XInt) inner);
        } else if (inner instanceof XLong) {
            return simpleName((XLong) inner);
        } else if (inner instanceof XFloat) {
            return simpleName((XFloat) inner);
        } else if (inner instanceof XDouble) {
            return simpleName((XDouble) inner);
        } else if (inner instanceof XString) {
            return simpleName((XString) inner);
        }
        throw new UnsupportedOperationException("unsupported inner type " + inner.name());
    }

    private String boxName(XType inner) {
        if (inner instanceof XBool) {
            return boxName((XBool) inner);
        } else if (inner instanceof XByte) {
            return boxName((XByte) inner);
        } else if (inner instanceof XShort) {
            return boxName((XShort) inner);
        } else if (inner instanceof XInt) {
            return boxName((XInt) inner);
        } else if (inner instanceof XLong) {
            return boxName((XLong) inner);
        } else if (inner instanceof XFloat) {
            return boxName((XFloat) inner);
        } else if (inner instanceof XDouble) {
            return boxName((XDouble) inner);
        } else if (inner instanceof XString) {
            return boxName((XString) inner);
        }
        throw new UnsupportedOperationException();
    }

    private String defaultValue(XType inner) {
        if (inner instanceof XBool) {
            return defaultValue((XBool) inner);
        } else if (inner instanceof XByte) {
            return defaultValue((XByte) inner);
        } else if (inner instanceof XShort) {
            return defaultValue((XShort) inner);
        } else if (inner instanceof XInt) {
            return defaultValue((XInt) inner);
        } else if (inner instanceof XLong) {
            return defaultValue((XLong) inner);
        } else if (inner instanceof XFloat) {
            return defaultValue((XFloat) inner);
        } else if (inner instanceof XDouble) {
            return defaultValue((XDouble) inner);
        } else if (inner instanceof XString) {
            return defaultValue((XString) inner);
        }
        throw new UnsupportedOperationException();
    }
}
