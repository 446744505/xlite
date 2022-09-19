package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XEnumField;
import xlite.excel.cell.*;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.TypeVisitor;

import java.util.Objects;

public class ConfEnumField extends XEnumField {
    @Getter @Setter private String fromCol;
    @Getter @Setter private String excel;

    public ConfEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public void setCellValue(XCell cell) {
        XType type = getType();
        if (type instanceof XAny) {
            setType(new XAny());
        }
        String val = getType().accept(new V(cell), Java.INSTANCE);
        setValue(val);
    }

    private static class V implements TypeVisitor<String> {
        private final XCell cell;

        private V(XCell cell) {
            this.cell = cell;
        }

        @Override
        public String visit(XLanguage language, XBool t) {
            return String.valueOf(cell.asBoolean());
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
        public String visit(XLanguage language, XVoid t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String visit(XLanguage language, XAny t) {
            XType inner = null;
            String rst = cell.asString();;
            if (cell instanceof BoolCell) {
                inner = TypeBuilder.BOOL;
            } else if (cell instanceof NumberCell) {
                double num = cell.asDouble();
                int intNum = (int) num;
                if (num == intNum) {
                    inner = TypeBuilder.INT;
                    rst = String.valueOf(cell.asInteger());
                } else {
                    inner = TypeBuilder.DOUBLE;
                }
            } else if (cell instanceof BlankCell) {
                inner = TypeBuilder.STRING;
                rst = "\"\"";
            } else if (cell instanceof StringCell) {
                String val = cell.asString();
                try {
                    Integer.parseInt(val);
                    inner = TypeBuilder.INT;
                    rst = String.valueOf(cell.asInteger());
                } catch (NumberFormatException e1) {
                    try {
                        Double.parseDouble(val);
                        inner = TypeBuilder.DOUBLE;
                    } catch (NumberFormatException e2) {
                        if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
                            inner = TypeBuilder.BOOL;
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
            t.setInner(inner);
            return rst;
        }
    }
}
