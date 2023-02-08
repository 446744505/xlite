package xlite.excel;

import lombok.Getter;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class XExcel implements Iterable<XSheet> {
    @Getter private final String fileName;
    private final File file;
    @Getter private final XExcelHook hook;
    private final Map<String, XSheet> sheets = new LinkedHashMap<>();

    public XExcel(String fileName, File file, XExcelHook hook) {
        this.fileName = fileName;
        this.file = file;
        this.hook = hook;
    }

    public void addSheet(XSheet sheet) {
        if (Objects.isNull(sheet)) {
            return;
        }
        sheets.put(sheet.getSheetName(), sheet);
    }

    @Override
    public Iterator<XSheet> iterator() {
        return sheets.values().iterator();
    }

    @Override
    public String toString() {
        return fileName;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
}
