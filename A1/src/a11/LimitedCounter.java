package a11;

public class LimitedCounter extends BaseCounter {
    private int limit;
    private int num_inc;

    public LimitedCounter(int start, int limit) {
        super(start);
        this.limit = limit;
        this.num_inc = 0;
    }

    private LimitedCounter() {
        this(0, 100);
    }


    @Override
    public Counter inc() {
        if (tally() + 1 > this.limit) {
            this.count = this.start();
        }
        this.num_inc++;
        return super.inc();
    }

    @Override
    public int start() {
        return super.start();
    }

    @Override
    public void reset() {
        super.reset();
        this.num_inc = 0;
    }

    @Override
    public int numInc() {
        return this.num_inc;
    }

    public static void main(String[] args) {
        LimitedCounter lm = new LimitedCounter(0, 2);
        lm.inc();
        lm.inc();
        lm.inc();
        System.out.println(lm);
        lm.reset();
        lm.inc();
        lm.inc();
        System.out.println(lm);
    }
}

