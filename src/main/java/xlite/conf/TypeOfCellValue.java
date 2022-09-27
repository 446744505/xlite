package xlite.conf;

import xlite.excel.cell.*;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.BoxName;
import xlite.type.visitor.TypeVisitor;

import java.util.Objects;

public class TypeOfCellValue implements TypeVisitor<Object> {
    private final XCell cell;

    public TypeOfCellValue(XCell cell) {
        this.cell = cell;
    }

    @Override
    public Object visit(XLanguage language, XBool t) {
        return cell.asBoolean();
    }

    @Override
    public Object visit(XLanguage language, XByte t) {
        return cell.asByte();
    }

    @Override
    public Object visit(XLanguage language, XInt t) {
        return cell.asInteger();
    }

    @Override
    public Object visit(XLanguage language, XShort t) {
        return cell.asShort();
    }

    @Override
    public Object visit(XLanguage language, XLong t) {
        return cell.asLong();
    }

    @Override
    public Object visit(XLanguage language, XFloat t) {
        return cell.asFloat();
    }

    @Override
    public Object visit(XLanguage language, XDouble t) {
        return cell.asDouble();
    }

    @Override
    public Object visit(XLanguage language, XString t) {
        return cell.asString();
    }

    @Override
    public Object visit(XLanguage language, XList t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(XLanguage language, XMap t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(XLanguage language, XBean t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(XLanguage language, XEnum t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(XLanguage language, XVoid t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(XLanguage language, XAny t) {
        TypeBase inner = null;
        Object rst = cell.asString();
        if (cell instanceof BoolCell) {
            inner = TypeBuilder.BOOL;
            rst = cell.asBoolean();
        } else if (cell instanceof NumberCell) {
            double num = cell.asDouble();
            int intNum = (int) num;
            if (num == intNum) {
                inner = TypeBuilder.INT;
                rst = cell.asInteger();
            } else {
                inner = TypeBuilder.DOUBLE;
                rst = num;
            }
        } else if (cell instanceof BlankCell) {
            inner = TypeBuilder.STRING;
            rst = "\"\"";
        } else if (cell instanceof StringCell) {
            String val = cell.asString();
            try {
                Integer.parseInt(val);
                inner = TypeBuilder.INT;
                rst = cell.asInteger();
            } catch (NumberFormatException e1) {
                try {
                    Double.parseDouble(val);
                    inner = TypeBuilder.DOUBLE;
                    rst = cell.asDouble();
                } catch (NumberFormatException e2) {
                    if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
                        inner = TypeBuilder.BOOL;
                        rst = cell.asBoolean();
                    } else {
                        inner = TypeBuilder.STRING;
                        rst = "\"" + val + "\"";
                    }
                }
            }
        }
        if (Objects.isNull(inner)) {
            throw new UnsupportedOperationException("cellVal=" + cell.asString());
        }
        t.setValue(inner);
        return rst;
    }

    @Override
    public Object visit(XLanguage language, XRange t) {
        String valBoxName = t.getValue().accept(BoxName.INSTANCE, language);
        return cell.asRange(valBoxName);
    }
}
