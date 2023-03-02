package xlite.coder;

import java.util.ArrayList;
import java.util.List;

public class XConstructor extends AbsCoder {
    protected final List<XField> params = new ArrayList<>();

    protected XConstructor(XCoder parent) {
        super(parent);
    }

    public XConstructor addParam(XField param) {
        params.add(param);
        return this;
    }

    public List<XField> getParams() {
        return params;
    }
}
