package xlite.xml;

public class XmlContext {
    private final XXmlFactory factory;
    private boolean exportCode;
    private String endPoint;

    public XmlContext(XXmlFactory factory) {
        this.factory = factory;
    }

    public XXmlFactory getFactory() {
        return factory;
    }

    public boolean isExportCode() {
        return exportCode;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setExportCode(boolean exportCode) {
        this.exportCode = exportCode;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
