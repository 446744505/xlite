package xlite.coder;

import lombok.Getter;
import xlite.type.XType;

public class XMethod extends XConstructor {
    @Getter private final String name;
    @Getter private XType returned;
    @Getter private boolean isStatic;
    @Getter private boolean isOverride;
    private final StringBuilder body = new StringBuilder();

    public XMethod(String name, XCoder parent) {
        super(parent);
        this.name = name;
    }

    public XMethod returned(XType type) {
        returned = type;
        return this;
    }

    public XMethod staticed() {
        isStatic = true;
        return this;
    }

    public XMethod overrided() {
        isOverride = true;
        return this;
    }

    public XMethod addBody(String txt) {
        body.append(txt);
        return this;
    }

    @Override
    public XMethod addParam(XField param) {
        super.addParam(param);
        return this;
    }

    public String getBody() {
        return body.toString();
    }
}
