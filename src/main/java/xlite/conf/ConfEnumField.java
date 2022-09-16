package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XEnumField;
import xlite.excel.cell.XCell;
import xlite.language.Java;
import xlite.language.XLanguage;
import xlite.type.*;
import xlite.type.visitor.TypeVisitor;

public class ConfEnumField extends XEnumField {
    @Getter @Setter private String fromCol;
    @Getter @Setter private String excel;

    public ConfEnumField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public void setCellValue(XCell cell) {
        XType type = getType();
        String val = type.accept(new V(cell), Java.INSTANCE);
        setValue(val);
    }

    private static class V implements TypeVisitor<String> {
        private final XCell cell;

        private V(XCell cell) {
            this.cell = cell;
        }


        @Override
        public String visit(XLanguage language, XInt t) {
            return String.valueOf(cell.asInteger());
        }

        @Override
        public String visit(XLanguage language, XFloat t) {
            return String.valueOf(cell.asFloat());
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
    }
}
