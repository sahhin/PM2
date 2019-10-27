package a11;

public  class BaseCounter extends Counter{
    private int start;
    private int count;

    public BaseCounter(int start){
        this.count = start;
        this.start = start;
    }

    public BaseCounter(){
        this(30);
        this.count = start;
    }

    @Override
    public void inc() {
        this.count++;
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
    }

}

