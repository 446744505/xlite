package xlite.xml.attr;

public interface XAttr {
    String ATTR_NAME = "name";
    String ATTR_TYPE = "type";
    String ATTR_KEY = "key";
    String ATTR_VALUE = "value";
    String ATTR_PARENT = "parent";
    String ATTR_COMMENT = "comment";
    String ATTR_RANGE_CHECK = "range";

    //conf
    String ATTR_EXCEL = "excel";
    String ATTR_COLFROM = "from";
    String ATTR_ENDPOINT = "to";
    String ATTR_FOREIGN_CHECK = "foreign";
    String ATTR_UNIQ_CHECK = "uniq";

    String getName();
    String getValue();
}
