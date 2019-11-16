package utils;

import java.time.Year;
import java.util.*;

public class YearInterval implements Iterable<Year> {
    private Year start;
    private Year end;

    public YearInterval(Year start, Year end) {
        this.start = start;
        this.end = end;
    }

    public YearInterval(Year start) {
        this(start, start);
    }

    public static void main(String[] args) {
        System.out.printf("Hier");
    }

    public boolean contains(Year y) {
        return start.compareTo(y) <= 0 && y.compareTo(end) <= 0;
    }

    public Iterator<Year> iterator() {
        return new Iterator<Year>() {
            Year current = start.minusYears(1);

            @Override
            public boolean hasNext() {
                return current.plusYears(1).compareTo(end) <= 0;
            }

            @Override
            public Year next() {
                return current = current.plusYears(1);
            }
        };

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearInterval)) return false;
        YearInterval years = (YearInterval) o;
        return Objects.equals(start, years.start) &&
                Objects.equals(end, years.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        if (start.equals(end)) return start.toString();
        return String.format("(%d,%d)", start.getValue(), end.getValue());
    }

}