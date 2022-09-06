package xlite.coder;

import lombok.Getter;

public abstract class AbsCoder implements XCoder {
    @Getter protected final XCoder parent;

    protected AbsCoder(XCoder parent) {
        this.parent = parent;
    }
}
