package xlite.coder;

import xlite.type.XType;

public class XField extends AbsCoder {
    private final String name;
    private boolean staticed;
    private boolean consted;
    private boolean isPublic;
    private XType type;
    private String defaultVal;
    private String comment;
    private String rangeCheck = "";

    public XField(String name, XType type, XCoder parent) {
        super(parent);
        this.name = name;
        this.type = type;
    }

    public XField staticed() {
        staticed = true;
        return this;
    }

    public XField consted() {
        consted = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStaticed() {
        return staticed;
    }

    public boolean isConsted() {
        return consted;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public XType getType() {
        return type;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public String getComment() {
        return comment;
    }

    public String getRangeCheck() {
        return rangeCheck;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setType(XType type) {
        this.type = type;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRangeCheck(String rangeCheck) {
        this.rangeCheck = rangeCheck;
    }
}
