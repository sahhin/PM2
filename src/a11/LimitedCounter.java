package a11;

public class LimitedCounter extends BaseCounter {
    private int limit;
    private int num_inc;
    private int start;

    public LimitedCounter(int start, int limit) {
        super(start);
    }

    private LimitedCounter() {
        this(0, 100);
    }


    @Override
    public int inc() { return  0;}

    @Override
    public int start() {
        return 0;
    }

    @Override
    public int tally() {
        return 0;
    }

    @Override
    public void reset() {

    }
}
