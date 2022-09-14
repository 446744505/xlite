package xlite.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Header {
    private final XSheet sheet;
    private final Map<Integer, String> col2Title = new HashMap<>();
    private final Map<String, Integer> title2Col = new HashMap<>();

    public Header(XSheet sheet) {
        this.sheet = sheet;
    }

    public void setTitle(int colIndex, String title) {
        col2Title.put(colIndex, title);
        if (!Objects.isNull(title2Col.put(title, colIndex))) {
            throw new IllegalStateException(String.format("multi column %s @ %s", title, sheet));
        }
    }

    public Integer getColIndex(String title) {
        return title2Col.get(title);
    }

    public String getTitle(int colIndex) {
        return col2Title.get(colIndex);
    }

    public boolean isCommentCol(int colIndex) {
        return Objects.isNull(getTitle(colIndex));
    }
}
