package a11;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitedCounterTest {

    @Test
    void inc() {
        LimitedCounter lc = new LimitedCounter(0,2);
        Assert.assertEquals(0, lc.tally());
        Assert.assertNotEquals(5,lc.tally());
        lc.inc();lc.inc();lc.inc();
        Assert.assertEquals(3, lc.tally());
        Assert.assertNotEquals(8,lc.tally());
    }

    @Test
    void start() {
        LimitedCounter lc = new LimitedCounter(0,2);
        Assert.assertEquals(0, lc.tally());
        Assert.assertNotEquals(2,lc.tally());
        lc.inc();lc.inc();lc.inc();
        Assert.assertEquals(3, lc.tally());
        Assert.assertNotEquals(8,lc.tally());
    }

    @org.junit.jupiter.api.Test
    void tally() {
        BaseCounter bc = new BaseCounter(0);
        Assert.assertEquals(0, bc.tally());
        Assert.assertNotEquals(1, bc.tally());
    }

    @Test
    void reset() {
        LimitedCounter lc = new LimitedCounter(0,2);
        Assert.assertEquals(0, lc.tally());
        Assert.assertNotEquals(2,lc.tally());
        lc.inc();lc.inc();lc.inc();
        Assert.assertEquals(3, lc.tally());
        Assert.assertNotEquals(8,lc.tally());
    }

    @Test
    void numInc() {
        LimitedCounter lc = new LimitedCounter(0,2);
        Assert.assertEquals(0, lc.tally());
        Assert.assertNotEquals(3,lc.tally());
        lc.inc();lc.inc();lc.inc();
        Assert.assertEquals(3, lc.tally());
        Assert.assertNotEquals(8,lc.tally());
    }
}