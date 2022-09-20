package xlite.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xlite.excel.cell.XCell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class XReader {
    static final String COMMENT_SYMBOL = "#";
    static final String DEF_DEFAULT = "def_default";
    static final String DEF_START = "#ifdef";
    static final String DEF_END = "#endif";

    private final File excelDir;
    private final XExcelHook hook;
    private final Map<String, Workbook> books = new HashMap<>();

    public XReader(File excelDir, XExcelHook hook) {
        this.excelDir = excelDir;
        this.hook = hook;
    }

    public Map<String, XExcel> read() throws IOException, InvalidFormatException {
        for (File f : excelDir.listFiles()) {
            filterExcels(f);
        }
        return readExcels();
    }

    private void filterExcels(File file) throws IOException, InvalidFormatException {
        String fileName = file.getName();
        if (file.isDirectory() && !fileName.startsWith(COMMENT_SYMBOL)) {
            for (File f : file.listFiles()) {
                filterExcels(f);
            }
            return;
        }

        if (fileName.startsWith(COMMENT_SYMBOL)) {
            return;
        }

        Workbook workbook = null;
        if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(file);
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }
        if (Objects.nonNull(workbook)) {
            if (Objects.nonNull(books.put(fileName, workbook))) {//不允许重名
                throw new UnsupportedOperationException(String.format("excel file name %s is exist", fileName));
            }
        }
    }

    private Map<String, XExcel> readExcels() {
        Map<String, XExcel> excels = new HashMap<>(books.size());
        books.forEach((fileName, book) -> {
            if (hook.isLoadExcel(fileName)) {
                XExcel excel = parseBook(fileName, book);
                excels.put(excel.getFileName(), excel);
            }
        });
        return excels;
    }

    private XExcel parseBook(String fileName, Workbook book) {
        XExcel excel = new XExcel(fileName, hook);
        book.sheetIterator().forEachRemaining(sheet -> excel.addSheet(parseSheet(excel, sheet)));
        return excel;
    }

    private XSheet parseSheet(XExcel excel, Sheet sheet) {
        String sheetName = sheet.getSheetName();
        if (sheetName.startsWith(COMMENT_SYMBOL)) {
            return null;
        }

        String def = null;
        XSheet st = new XSheet(excel, sheetName);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {//第一行一定是header
                st.setHeader(parseHeader(st, row));
            } else {
                XRow r = parseRow(st, row);
                if (Objects.isNull(r)) {
                    continue;
                }
                if (r == XRow.DefEndRow.INSTANCE) {
                    def = null;
                } else if (r instanceof XRow.DefStartRow) {
                    def = ((XRow.DefStartRow) r).getName();
                } else {
                    st.addRow(def, r, hook);
                }
            }
        }
        return st;
    }

    private Header parseHeader(XSheet sheet, Row row) {
        Header header = new Header(sheet);
        row.cellIterator().forEachRemaining(cell -> {
            CellType cellType = cell.getCellType();
            if (cellType != CellType.STRING) {
                throw new IllegalStateException("sheet header cell must be a string cell @ " + sheet);
            }
            String title = cell.getStringCellValue();
            int colIndex = cell.getColumnIndex();
            if (title.startsWith(XReader.COMMENT_SYMBOL)) {
                if (colIndex == 0) {
                    throw new IllegalStateException("cell (0, 0) can not be a comment cell @ " + sheet);
                }
            } else {
                hook.checkHeader(colIndex, title, sheet);
                header.setTitle(colIndex, title);
            }
        });
        return header;
    }

    private XRow parseRow(XSheet sheet, Row row) {
        XRow r = new XRow(sheet);
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            if (sheet.isCommentCol(colIndex)) {
                continue;
            }
            Cell cell = row.getCell(colIndex);
            if (colIndex == 0 && Objects.nonNull(cell) && cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue();
                if (val.startsWith(XReader.DEF_START)) {
                    String name = val.substring(XReader.DEF_START.length() + 1);
                    return new XRow.DefStartRow(name);
                } else if (XReader.DEF_END.equals(val)) {
                    return XRow.DefEndRow.INSTANCE;
                } else if (val.startsWith(XReader.COMMENT_SYMBOL)) {
                    return null;//注释行
                }
            }
            r.addCell(colIndex, XCell.createCell(cell));
        }
        return r;
    }
}
