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
    public String valueOf(XBool t, String v) {
        return v;
    }

    private String baseCheck(TypeBase t, String fieldName, String format) {
        String boxName = t.accept(BoxName.INSTANCE, this);
        return String.format("check%s(%s, \"%s\")", boxName, fieldName, format);
    }

    @Override
    public String rangeCheck(XBool t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XByte t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XByte t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XShort t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XShort t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XInt t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XInt t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XLong t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XLong t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XFloat t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XFloat t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
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
    public String valueOf(XDouble t, String v) {
        return v;
    }

    @Override
    public String rangeCheck(XDouble t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
    }

    @Override
    public String defaultValue(XString t) {
        return "\"\"";
    }

    @Override
    public String valueOf(XString t, String v) {
        return "\"" + v + "\"";
    }

    @Override
    public String rangeCheck(XString t, String fieldName, String format) {
        return baseCheck(t, fieldName, format);
    }

    @Override
    public String defaultValue(XList t) {
        return "new java.util.ArrayList<>()";
    }

    @Override
    public String valueOf(XList t, String v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String rangeCheck(XList t, String fieldName, String format) {
        return String.format("checkList(%s, \"%s\")", fieldName, format);
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
    public String valueOf(XMap t, String v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String rangeCheck(XMap t, String fieldName, String format) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String simpleName(XBean t) {
        Class clazz = t.getClazz();
        if (Objects.nonNull(clazz)) {
            return clazz.getSimpleName();
        }
        return XClass.getFullName(t.name(), this);
    }

    @Override
    public String boxName(XBean t) {
        Class clazz = t.getClazz();
        if (Objects.nonNull(clazz)) {
            return clazz.getName();
        }
        return XClass.getFullName(t.name(), this);
    }

    @Override
    public String defaultValue(XBean t) {
        return String.format("new %s()", boxName(t));
    }

    @Override
    public String valueOf(XBean t, String v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String rangeCheck(XBean t, String fieldName, String format) {
        return fieldName + ".check()";
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
    public String valueOf(XVoid t, String v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String rangeCheck(XVoid t, String fieldName, String format) {
        throw new UnsupportedOperationException();
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
    public String valueOf(XAny t, String v) {
        return valueOf(t.getValue(), v);
    }

    @Override
    public String rangeCheck(XAny t, String fieldName, String format) {
        return rangeCheck(t.getValue(), fieldName, format);
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
    public String valueOf(XEnum t, String v) {
        t.assertInner();
        return valueOf(t.getInner(), v);
    }

    @Override
    public String rangeCheck(XEnum t, String fieldName, String format) {
        t.assertInner();
        return rangeCheck(t.getInner(), fieldName, format);
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
        return String.format("xlite.type.inner.Range.toRange(\"%s\", \"\")", t.getValue().name());
    }

    @Override
    public String valueOf(XRange t, String v) {
        return String.format("new xlite.type.inner.toRange(\"%s\", \"%s\")", t.getValue().name(), v);
    }

    @Override
    public String rangeCheck(XRange t, String fieldName, String format) {
        return "";
    }

    @Override
    public String simpleName(XTime t) {
        return simpleName(TypeBuilder.LONG);
    }

    @Override
    public String boxName(XTime t) {
        return boxName(TypeBuilder.LONG);
    }

    @Override
    public String defaultValue(XTime t) {
        return defaultValue(TypeBuilder.LONG);
    }

    @Override
    public String valueOf(XTime t, String v) {
        return String.format("xlite.util.Util.strToTime(\"%s\")", v);
    }

    @Override
    public String rangeCheck(XTime t, String fieldName, String format) {
        return rangeCheck(TypeBuilder.LONG, fieldName, format);
    }

    @Override
    public String simpleName(XDate t) {
        return "xlite.type.inner.DateTime";
    }

    @Override
    public String boxName(XDate t) {
        return "xlite.type.inner.DateTime";
    }

    @Override
    public String defaultValue(XDate t) {
        return "new xlite.type.inner.DateTime()";
    }

    @Override
    public String valueOf(XDate t, String v) {
        return String.format("new xlite.type.inner.DateTime(\"%s\")", v);
    }

    @Override
    public String rangeCheck(XDate t, String fieldName, String format) {
        return String.format("checkDate(%s, \"%s\")", fieldName, format);
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
        } else if (inner instanceof XDate) {
            return simpleName((XDate) inner);
        }  else if (inner instanceof XTime) {
            return simpleName((XTime) inner);
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
        } else if (inner instanceof XDate) {
            return boxName((XDate) inner);
        } else if (inner instanceof XTime) {
            return boxName((XTime) inner);
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
        } else if (inner instanceof XDate) {
            return defaultValue((XDate) inner);
        } else if (inner instanceof XTime) {
            return defaultValue((XTime) inner);
        }
        throw new UnsupportedOperationException();
    }

    private String valueOf(XType inner, String v) {
        if (inner instanceof XBool) {
            return valueOf((XBool) inner, v);
        } else if (inner instanceof XByte) {
            return valueOf((XByte) inner, v);
        } else if (inner instanceof XShort) {
            return valueOf((XShort) inner, v);
        } else if (inner instanceof XInt) {
            return valueOf((XInt) inner, v);
        } else if (inner instanceof XLong) {
            return valueOf((XLong) inner, v);
        } else if (inner instanceof XFloat) {
            return valueOf((XFloat) inner, v);
        } else if (inner instanceof XDouble) {
            return valueOf((XDouble) inner, v);
        } else if (inner instanceof XString) {
            return valueOf((XString) inner, v);
        } else if (inner instanceof XDate) {
            return valueOf((XDate) inner, v);
        } else if (inner instanceof XTime) {
            return valueOf((XTime) inner, v);
        }
        throw new UnsupportedOperationException();
    }

    private String rangeCheck(XType inner, String fieldName, String format) {
        if (inner instanceof XBool) {
            return rangeCheck((XBool) inner, fieldName, format);
        } else if (inner instanceof XByte) {
            return rangeCheck((XByte) inner, fieldName, format);
        } else if (inner instanceof XShort) {
            return rangeCheck((XShort) inner, fieldName, format);
        } else if (inner instanceof XInt) {
            return rangeCheck((XInt) inner, fieldName, format);
        } else if (inner instanceof XLong) {
            return rangeCheck((XLong) inner, fieldName, format);
        } else if (inner instanceof XFloat) {
            return rangeCheck((XFloat) inner, fieldName, format);
        } else if (inner instanceof XDouble) {
            return rangeCheck((XDouble) inner, fieldName, format);
        } else if (inner instanceof XString) {
            return rangeCheck((XString) inner, fieldName, format);
        } else if (inner instanceof XDate) {
            return rangeCheck((XDate) inner, fieldName, format);
        } else if (inner instanceof XTime) {
            return rangeCheck((XTime) inner, fieldName, format);
        }
        throw new UnsupportedOperationException();
    }
}
