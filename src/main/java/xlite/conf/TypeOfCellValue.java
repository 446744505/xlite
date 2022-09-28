package xlite.conf;

import xlite.excel.cell.*;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.TypeVisitor;
import xlite.util.Util;

import java.time.format.DateTimeParseException;
import java.util.Objects;

public class TypeOfCellValue implements TypeVisitor<String> {
    private final XCell cell;

    public TypeOfCellValue(XCell cell) {
        this.cell = cell;
    }

    @Override
    public String visit(XLanguage language, XBool t) {
        return String.valueOf(cell.asBoolean());
    }

    @Override
    public String visit(XLanguage language, XByte t) {
        return String.valueOf(cell.asByte());
    }

    @Override
    public String visit(XLanguage language, XInt t) {
        return String.valueOf(cell.asInteger());
    }

    @Override
    public String visit(XLanguage language, XShort t) {
        return String.valueOf(cell.asShort());
    }

    @Override
    public String visit(XLanguage language, XLong t) {
        return String.valueOf(cell.asLong());
    }

    @Override
    public String visit(XLanguage language, XFloat t) {
        return String.valueOf(cell.asFloat());
    }

    @Override
    public String visit(XLanguage language, XDouble t) {
        return String.valueOf(cell.asDouble());
    }

    @Override
    public String visit(XLanguage language, XString t) {
        return cell.asString();
    }

    @Override
    public String visit(XLanguage language, XList t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XMap t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XBean t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XEnum t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XVoid t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XAny t) {
        XType inner = null;
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
            rst = "";
        } else if (cell instanceof StringCell) {
            String val = cell.asString();
            try {
                Util.strToDate(val);
                inner = TypeBuilder.DATE;
                rst = val;
            } catch (DateTimeParseException e0) {
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
                            long time = Util.strToTime(val);
                            if (time > 0) {
                                inner = TypeBuilder.TIME;
                            } else {
                                inner = TypeBuilder.STRING;
                            }
                        }
                    }
                }
            }
        }
        if (Objects.isNull(inner)) {
            throw new UnsupportedOperationException("cellVal=" + cell.asString());
        }
        t.setValue(inner);
        return rst.toString();
    }

    @Override
    public String visit(XLanguage language, XRange t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XTime t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(XLanguage language, XDate t) {
        throw new UnsupportedOperationException();
    }
}
