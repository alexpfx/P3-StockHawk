package com.udacity.stockhawk;

/**
 * Created by alexandre on 22/04/2017.
 */

public class StockItem {
    private String symbol;
    private String price;

    public StockItem(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getSymbol() {
        return symbol;
    }
}
