package a11;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitedCounterTest {

    @Test
    void inc() {
        LimitedCounter lc = new LimitedCounter(0,2);
        Assert.assertEquals(2, lc.tally());
        Assert.assertNotEquals(0,lc.tally());
        lc.inc();lc.inc();lc.inc();
        Assert.assertEquals(3, lc.tally());
        Assert.assertNotEquals(8,lc.tally());
    }

    @Test
    void start() {
    }

    @Test
    void reset() {
    }

    @Test
    void numInc() {
    }
}