package com.udacity.stockhawk.sync;

import java.math.BigDecimal;

public class HistoricalData {

    private long time;
    private float open;
    private float low;
    private float high;
    private float close;

    public HistoricalData(long time, BigDecimal open, BigDecimal low, BigDecimal high, BigDecimal close) {
        this.time = time;
        this.open = open.floatValue();
        this.low = low.floatValue();
        this.high = high.floatValue();
        this.close = close.floatValue();
    }


    private float toCents(BigDecimal value) {
        return value.floatValue();
    }

    public long getTime() {
        return time;
    }


    public float getOpen() {
        return open;
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }

    public float getClose() {
        return close;
    }
}
