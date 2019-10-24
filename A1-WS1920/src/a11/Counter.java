package a11;

public abstract class Counter {
    public abstract int inc();

    public abstract int start();

    public abstract int tally();

    public int num_inc() {
        return tally() - start();
    }

    public abstract int reset();

    public String toString() {
        return getClass().getName() + "start:" + start() + "tally:" + tally() + "num_inc:" + num_inc();
    }
}
