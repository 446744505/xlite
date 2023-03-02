package xlite.coder;

public abstract class AbsCoder implements XCoder {
    protected final XCoder parent;

    protected AbsCoder(XCoder parent) {
        this.parent = parent;
    }

    public XCoder getParent() {
        return parent;
    }
}
