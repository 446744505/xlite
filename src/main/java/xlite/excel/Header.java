package xlite.excel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Header {
    private final Map<Integer, String> col2Title = new HashMap<>();
    private final Map<String, Integer> title2Col = new HashMap<>();

    public void setTitle(int colIndex, String title) {
        col2Title.put(colIndex, title);
        title2Col.put(title, colIndex);
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
