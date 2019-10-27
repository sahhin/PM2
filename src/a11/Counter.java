package a11;

public abstract class Counter {
    public abstract void inc();

    public abstract int start();

    public abstract int tally();

    public int numInc() {
        return tally() - start();
    }

    public abstract void reset();

    public String toString() {
        return getClass().getName() + " start:" + start() +  " tally:" + tally() + " num_inc:" + numInc();
    }
}
