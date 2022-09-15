package xlite.xml.attr;

public interface XAttr {
    String ATTR_NAME = "name";
    String ATTR_TYPE = "type";
    String ATTR_KEY = "key";
    String ATTR_VALUE = "value";
    String ATTR_PARENT = "parent";

    //conf
    String ATTR_EXCEL = "excel";
    String ATTR_COLFROM = "from";
    String ATTR_ENDPOINT = "to";
    String ATTR_COMMENT = "comment";

    String getName();
    String getValue();
}
