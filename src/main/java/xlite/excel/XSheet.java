package xlite.excel;

import java.util.*;

public class XSheet {
    private final XExcel excel;
    private final String sheetName;
    private Header header;
    private final Map<String, Map<Object, XRow>> rows = new HashMap<>();//key1=宏 key2=row key

    public XSheet(XExcel excel, String sheetName) {
        this.excel = excel;
        this.sheetName = sheetName;
    }

    public void addRow(String def, XRow row, XExcelHook hook) {
        if (Objects.isNull(row)) {
            return;
        }
        if (Objects.isNull(def) || def.isEmpty()) {
            def = XReader.DEF_DEFAULT;
        }
        Map<Object, XRow> rs = rows.get(def);
        if (Objects.isNull(rs)) {
            rs = new LinkedHashMap<>();
            rows.put(def, rs);
        }

        if (hook.isParseKey()) {
            Object key = hook.key(row);
            if (Objects.nonNull(rs.put(key, row))) {
                throw new IllegalStateException(String.format("multi row key = %s @ %s", key, this));
            }
        }
    }

    public Collection<XRow> rows() {
        return rows(null);
    }

    public Collection<XRow> rows(String def) {
        Map<Object, XRow> defaults = rows.get(XReader.DEF_DEFAULT);
        if (Objects.isNull(defaults)) {
            defaults = new HashMap<>();
        }
        if (Objects.nonNull(def) && !def.equals(XReader.DEF_DEFAULT)) {
            Map<Object, XRow> defs = rows.get(def);
            if (Objects.nonNull(defs)) {
                defaults.putAll(defs);
            }
        }
        return defaults.values();
    }

    public boolean isCommentCol(int colIndex) {
        return header.isCommentCol(colIndex);
    }

    public XExcel getExcel() {
        return excel;
    }

    public String getSheetName() {
        return sheetName;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return excel + "/" + sheetName;
    }
}
