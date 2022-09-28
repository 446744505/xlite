package xlite.type.inner;

import lombok.Getter;
import xlite.util.Util;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateTime {
    @Getter private final String txt;
    private LocalDateTime localDateTime;

    public DateTime() {
        LocalDateTime now = LocalDateTime.now();
        txt = now.format(Util.formatter);
    }

    public DateTime(String txt) {
        this.txt = txt;
    }

    public LocalDateTime asLocalDateTime() {
        if (Objects.nonNull(localDateTime)) {
            return localDateTime;
        }
        localDateTime = Util.strToDate(txt);
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return Objects.equals(txt, dateTime.txt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txt);
    }

    @Override
    public String toString() {
        return txt;
    }
}
