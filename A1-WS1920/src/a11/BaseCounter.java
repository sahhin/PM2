package a11;

public abstract class BaseCounter extends Counter{
    private  int start;
    private int count;
    public BaseCounter(int start){
        count = start;
    }

    public BaseCounter(){
        this(0);
    }
    @Override
    public int inc() {
        count += 1;
        return  count;
    }

    @Override
    public int start() {
        return start;
    }

    @Override
    public int tally() {
        return count;
    }

    @Override
    public int reset(){
    return count = start;
    }
}
