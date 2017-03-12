package com.udacity.stockhawk.sync;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.StringTokenizer;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by alexandre on 11/03/2017.
 */

public class HistoryBuilder {
    private StringBuilder sb = new StringBuilder();
    private final String sep = "|";

    public HistoryBuilder() {

    }

    public HistoryBuilder (String history){
        sb.append(history);
    }

    public void add(HistoricalQuote historicalData) {
        sep(sb.append(historicalData.getDate().getTimeInMillis()));
        sep(sb.append(historicalData.getOpen()));
        sep(sb.append(historicalData.getLow()));
        sep(sb.append(historicalData.getHigh()));
        sb.append(historicalData.getClose());
        sb.append("\n");
    }

    private void sep(StringBuilder sb) {
        sb.append(sep);
    }

    @Override
    public String toString() {
        return sb.toString();
    }



    public List<HistoricalData> parse() {
        StringTokenizer stLine = new StringTokenizer(sb.toString(), "\n");
        List<HistoricalData> historicalDatas = new ArrayList<>();
        while (stLine.hasMoreTokens()) {
            String element = stLine.nextToken();
            StringTokenizer st = new StringTokenizer(element, sep);
            String date = nextToken(st);
            BigDecimal open = new BigDecimal(nextToken(st));
            BigDecimal low = new BigDecimal(nextToken(st));
            BigDecimal high = new BigDecimal(nextToken(st));
            BigDecimal close = new BigDecimal(nextToken(st));
            HistoricalData data = new HistoricalData(Long.valueOf(date), open, low, high, close);
            historicalDatas.add(data);
        }
        return historicalDatas;
    }


    String nextToken(StringTokenizer st) {
        return st.nextToken();
    }

}
