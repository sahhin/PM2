package a11;

public class StepCounter extends BaseCounter {

    private int step;

    public StepCounter(int start, int step) {
        super(start);
        this.step = step;
    }

    public StepCounter() {
        this(0, 1);
    }

    @Override
    public Counter inc() {
        this.count += this.step;
        return this;
    }

    @Override
    public int numInc() {
        return super.numInc() / this.step;
    }

    @Override
    public int tally() {
        return this.count;
    }

    public static void main(String[] args) {
        StepCounter sc = new StepCounter(4, 2);
        sc.inc();
        sc.inc();
        sc.inc();
        System.out.println(sc);
    }
}

