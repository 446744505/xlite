package xlite.conf;

import xlite.coder.XCoder;
import xlite.coder.XField;
import xlite.type.XType;

public class ConfBeanField extends XField {
    private String fromCol;
    private String endPoint;
    private String foreignCheck;
    private String uniqCheck;
    private String mustCheck;
    private String index;

    public ConfBeanField(String name, XType type, XCoder parent) {
        super(name, type, parent);
    }

    public String getFromCol() {
        return fromCol;
    }

    public void setFromCol(String fromCol) {
        this.fromCol = fromCol;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getForeignCheck() {
        return foreignCheck;
    }

    public void setForeignCheck(String foreignCheck) {
        this.foreignCheck = foreignCheck;
    }

    public String getUniqCheck() {
        return uniqCheck;
    }

    public void setUniqCheck(String uniqCheck) {
        this.uniqCheck = uniqCheck;
    }

    public String getMustCheck() {
        return mustCheck;
    }

    public void setMustCheck(String mustCheck) {
        this.mustCheck = mustCheck;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
