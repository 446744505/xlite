package xlite.conf;

import lombok.Getter;
import lombok.Setter;
import xlite.coder.XCoder;
import xlite.coder.XEnumer;
import xlite.coder.XEnumField;
import xlite.gen.GenContext;
import xlite.gen.visitor.PrintEnumField;

import java.util.Objects;

public class ConfEnum extends XEnumer {
    @Getter @Setter private String fromExcel;

    public ConfEnum(String name, XCoder parent) {
        super(name, parent);
    }

    @Override
    protected void printField(GenContext context) {
        fields.stream()
            .map(f -> (ConfEnumField) f)
            .filter(f -> Objects.nonNull(f.getValue()))
            .forEach(f -> context.println(1, new PrintEnumField((XEnumField) f)));
    }
}
