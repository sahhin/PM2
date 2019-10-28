package a11;

public class StepCounter extends BaseCounter {

    private int start;
    private int count;
    private int step;

    public StepCounter(int start, int step) {
        super(start);
        this.start = start;
        this.count = start;
        this.step = step;
    }

    public StepCounter() {
        this(0, 1);
    }

    @Override
    public void inc() {
        count += this.step;
    }

    @Override
    public int numInc() {
        return super.numInc() / this.step;
    }

    public static void main(String[] args) {
        StepCounter sc = new StepCounter(4, 2);
        sc.inc();
        sc.inc();
        sc.inc();
        System.out.println(sc);
    }
}

