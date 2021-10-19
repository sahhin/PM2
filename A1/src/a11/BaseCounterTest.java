package a11;

import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class BaseCounterTest {

    @org.junit.jupiter.api.Test
    void inc() {
        BaseCounter bc = new BaseCounter(4);
        Assert.assertEquals(4, bc.tally());
        Assert.assertNotEquals(0,bc.tally());

        bc.inc();bc.inc();bc.inc();

        Assert.assertEquals(7, bc.tally());
        Assert.assertNotEquals(8,bc.tally());

    }

    @org.junit.jupiter.api.Test
    void start() {
        BaseCounter bc = new BaseCounter(4);
        Assert.assertEquals(4, bc.start());
        Assert.assertNotEquals(3, bc.start());
    }

    @org.junit.jupiter.api.Test
    void tally() {
        BaseCounter bc = new BaseCounter(0);
        Assert.assertEquals(0, bc.tally());
        Assert.assertNotEquals(1, bc.tally());
    }

    @org.junit.jupiter.api.Test
    void reset() {
        BaseCounter bc = new BaseCounter(5);
        bc.reset();
        Assert.assertEquals(bc.tally(), bc.start());
        Assert.assertNotEquals(0, bc.start());

    }
}