package com.udacity.stockhawk.ui;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.sync.HistoricalData;
import com.udacity.stockhawk.sync.HistoryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PriceHistoryActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MY_PERMISSION_REQUEST_READ_FINE_LOCATION = 10;
    @BindView(R.id.chart)
    CandleStickChart lineChart;

    private String history;
    private Uri uri;
    private String stockSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_history);
        ButterKnife.bind(this);

        getLoaderManager().initLoader(1, null, this);

        Intent intent = getIntent();

        String symbol = intent.getStringExtra(Intent.EXTRA_TEXT);
        Timber.d(symbol);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contract.Quote.URI, Contract.Quote.QUOTE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst() && history == null) {
            history = cursor.getString(Contract.Quote.POSITION_HISTORY);
            stockSymbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
        }
        if (history.isEmpty()){
            return;
        }
        updateChart(history);
    }

    private void updateChart(String history) {
        CandleDataSet set = new CandleDataSet(getEntries(history), stockSymbol);
        setupCandleSet(set);
        lineChart.setData(new CandleData(set));
    }

    private void setupCandleSet(final CandleDataSet set) {
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set.setShadowWidth(1.0f);
        set.setShadowColorSameAsCandle(true);

        set.setDecreasingColor(Color.RED);
        set.setDecreasingPaintStyle(Paint.Style.FILL);

        set.setIncreasingColor(Color.BLUE);
        set.setIncreasingPaintStyle(Paint.Style.FILL);

        set.setHighlightLineWidth(1.0f);
        set.setHighLightColor(Color.CYAN);

        set.setDrawValues(false);
        set.setVisible(true);

        set.setColor(Color.CYAN);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public List<CandleEntry> getEntries(String history) {
        HistoryBuilder builder = new HistoryBuilder(history);
        List<HistoricalData> historicalDataList = builder.parse();
        List<CandleEntry> entries = new ArrayList<>();
        int i = 0;
        for (HistoricalData historicalData : historicalDataList) {
            CandleEntry entry = new CandleEntry(i++, historicalData.getHigh(), historicalData.getLow(), historicalData.getOpen(), historicalData.getClose());
            entries.add(entry);
        }
        return entries;
    }

}