package xlite.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class XSheet {
    @Getter private final XExcel excel;
    @Getter private final String sheetName;
    @Setter @Getter private Header header;
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

    @Override
    public String toString() {
        return excel + "/" + sheetName;
    }
}
