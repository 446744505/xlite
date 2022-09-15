package xlite.xml;

import lombok.Getter;
import lombok.Setter;

public class XmlContext {
    @Getter private final XXmlFactory factory;
    @Getter @Setter private boolean confLoadCode;

    public XmlContext(XXmlFactory factory) {
        this.factory = factory;
    }
}
