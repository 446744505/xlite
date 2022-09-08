package xlite.excel;

public interface XExcelHook {
    void checkHeader(int colIndex, String title, XSheet sheet);
    Object key(XRow row);
}
