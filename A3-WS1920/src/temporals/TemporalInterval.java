package temporals;

import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;
import java.util.Objects;

public class TemporalInterval<T extends Temporal & Comparable<? super T>> implements Iterable<T> {
    private T start;
    private T end;
    private TemporalUnit unit;

    public TemporalInterval(T start, T end, TemporalUnit unit) {
        this.start = start;
        this.end = end;
        this.unit = unit;
    }

    public TemporalInterval(T start,TemporalUnit unit) {
        this(start,start,unit);
    }

    public boolean contains(T y) {
        return start.compareTo(y) <= 0 && y.compareTo(end) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemporalInterval)) return false;
        TemporalInterval years = (TemporalInterval) o;
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
        return String.format("[%s..%s]",start,end);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            T current = (T)start.minus(1,unit);
            @Override
            public boolean hasNext() {
                return ((T)current.plus(1,unit)).compareTo(end) <= 0;
            }

            @Override
            public T next() {
                return current = (T)current.plus(1,unit);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        Year y = Year.of(2016);
        TemporalInterval<Year> ty = new TemporalInterval<>(Year.of(2015),Year.of(2019), ChronoUnit.YEARS);

        System.out.println(ty);
        for(Year ye : ty) {
            System.out.println(ye);
        }
    }
}