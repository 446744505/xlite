package xlite.coder;

import lombok.Getter;
import xlite.type.TypeBuilder;
import xlite.type.XType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XMethod extends XConstructor {
    @Getter private final String name;
    private XType returned;
    @Getter private boolean isStatic;
    @Getter private boolean isOverride;
    @Getter private Scope scope = Scope.PUBLIC;
    @Getter private List<String> exceptions = new ArrayList<>();
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

    public XMethod scope(Scope scope) {
        this.scope = scope;
        return this;
    }

    public XMethod addBody(String txt) {
        body.append(txt);
        return this;
    }

    public XMethod throwed(String ex) {
        if (!exceptions.contains(ex)) {
            exceptions.add(ex);
        }
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

    public XType getReturned() {
        if (Objects.isNull(returned)) {
            return TypeBuilder.VOID;
        }
        return returned;
    }
}
