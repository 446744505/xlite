package xlite.conf;

import xlite.excel.XExcelHook;
import xlite.excel.XRow;
import xlite.excel.XSheet;
import xlite.excel.cell.XCell;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfExcelHook implements XExcelHook {
    public static final String COL_ID_TITLE = "ID";

    private final boolean onlyLoadEnum;
    private final Map<String, String> enumExcels = new HashMap<>();
    private final Map<String, Class> enumIdExcels = new HashMap<>();

    public ConfExcelHook(boolean onlyLoadEnum) {
        this.onlyLoadEnum = onlyLoadEnum;
    }

    public void registerEnumExcel(String fileName, String keyCol) {
        enumExcels.put(fileName, keyCol);
    }

    public void registerEnumIdExcel(String fileName, Class clazz) {
        enumIdExcels.put(fileName, clazz);
    }

    private boolean isEnumExcel(String fileName) {
        return enumExcels.containsKey(fileName);
    }

    @Override
    public boolean isLoadExcel(String fileName) {
        if (onlyLoadEnum) {
            return enumExcels.containsKey(fileName);
        }
        return true;
    }

    @Override
    public void checkHeader(int colIndex, String title, XSheet sheet) {
        String fileName = sheet.getExcel().getFileName();
        if (!isEnumExcel(fileName) && colIndex == 0 && !COL_ID_TITLE.equals(title)) {
            throw new IllegalStateException("conf excel cell(0, 0) must a ID cell @ " + sheet);
        }
    }

    @Override
    public boolean isParseKey() {
        return true;
    }

    @Override
    public Object key(XRow row) {
        String fileName = row.getSheet().getExcel().getFileName();
        if (isEnumExcel(fileName)) {
            return enumKey(row, fileName);
        }
        return beanKey(row);
    }

    private Object enumKey(XRow row, String fileName) {
        String keyCol = enumExcels.get(fileName);
        XCell cell = row.getCell(keyCol);
        if (Objects.isNull(cell)) {
            throw new IllegalStateException(String.format("there is no %s column @ %s", keyCol, row.getSheet()));
        }
        return cell.asString();
    }

    private Object beanKey(XRow row) {
        XCell idCell = row.getCell(COL_ID_TITLE);
        XSheet sheet = row.getSheet();
        if (Objects.isNull(idCell)) {
            throw new IllegalStateException(String.format("there is no id column @ %s", sheet));
        }
        String fileName = row.getSheet().getExcel().getFileName();
        Class enumClass = enumIdExcels.get(fileName);
        if (Objects.isNull(enumClass)) {
            return idCell.asInteger();
        }

        //id是enum
        String id = idCell.asString();
        try {
            Field f = enumClass.getField(id);
            return f.get(null);
        } catch (NoSuchFieldException e) {
            throw new NullPointerException(String.format("not id %s at enum %s", id, enumClass.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
