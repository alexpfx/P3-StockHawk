package com.udacity.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.HistoricalData;
import com.udacity.stockhawk.sync.HistoryBuilder;
import com.udacity.stockhawk.sync.QuoteIntentService;
import com.udacity.stockhawk.sync.QuoteJobService;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PriceHistoryActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.chart)
    CandleStickChart lineChart;

    private String history;
    private Uri uri;

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
        }
        updateChart(history);
    }

    private void updateChart(String history) {
        CandleDataSet set = new CandleDataSet(getEntries(history), "xlabel");
        setupCandleSet(set);

        lineChart.setData(new CandleData(set));
    }

    private void setupCandleSet(final CandleDataSet set) {
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(2.0f);
        set.setDecreasingColor(Color.RED);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(Color.BLUE);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(1f);
        set.setHighLightColor(Color.YELLOW);
        set.setDrawValues(false);
        set.setVisible(true);
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