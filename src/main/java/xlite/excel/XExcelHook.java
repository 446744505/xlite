package xlite.excel;

public interface XExcelHook {
    boolean isLoadExcel(String fileName);
    void checkHeader(int colIndex, String title, XSheet sheet);
    Object key(XRow row);
    boolean isParseKey();
}
