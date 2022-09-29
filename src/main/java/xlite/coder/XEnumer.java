package xlite.coder;

import lombok.Getter;
import lombok.Setter;
import xlite.gen.GenContext;
import xlite.gen.visitor.PrintConst;
import xlite.type.XType;

public class XEnumer extends XClass {
    @Getter @Setter private XType inner;

    public XEnumer(String name, XCoder parent) {
        super(name, parent);
    }

    public void print(GenContext context) {
        printPackage(context);
        context.println();
        printImport(context);
        context.println();
        printDefine(context);
        printField(context);
        context.println();
        printMethod(context);
        context.println("}");
    }

    @Override
    protected void printField(GenContext context) {
        fields.stream().map(f -> (XEnumField)f).forEach(f -> context.println(1, new PrintConst(f)));
    }
}
