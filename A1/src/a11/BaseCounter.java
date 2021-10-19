package a11;

public  class BaseCounter extends Counter{
    private int start;
    protected int count;

    public BaseCounter(int start){
        this.start = start;
        this.count = start;
    }

    public BaseCounter(){
        this(0);
    }

    @Override
    public Counter inc() {
        this.count++;
        return this;
    }

    @Override
    public int start() {
        return this.start;
    }

    @Override
    public int tally() {
        return this.count;
    }

    @Override
    public void reset(){
    this.count = this.start;
    }

    public static void main(String[] args) {
        BaseCounter bc = new BaseCounter(4);
        bc.inc();
        bc.inc();bc.inc();
        System.out.println(bc);
        bc.reset();
        bc.inc();bc.inc();
        System.out.println(bc);
    }

}

