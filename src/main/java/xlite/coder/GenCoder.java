package xlite.coder;

import xlite.gen.GenContext;

public interface GenCoder extends XCoder {
    void before(GenContext context);
    void gen(GenContext context);
}
