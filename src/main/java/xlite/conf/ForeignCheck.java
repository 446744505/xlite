package xlite.conf;

import lombok.Getter;

public class ForeignCheck {
    @Getter private final Class owner;
    @Getter private final String ownerField;
    @Getter private final Class checker;
    @Getter private final String checkField;
    @Getter private final Object val;
    @Getter private final boolean checkChild;

    public ForeignCheck(Class owner, String ownerField, Class checker, String checkField, Object val, boolean checkChild) {
        this.owner = owner;
        this.ownerField = ownerField;
        this.checker = checker;
        this.checkField = checkField;
        this.val = val;
        this.checkChild = checkChild;
    }
}
