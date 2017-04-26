package com.udacity.stockhawk;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.stockhawk.data.Contract;

/**
 * Created by alexandre on 21/04/2017.
 */

public class StocksIntentService extends IntentService {
    private static final String TAG = "StocksIntentService";
    String [] cols = new String[]{Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE};

    private static final int INDEX_SYMBOL = 0;
    private static final int INDEX_PRICE = 1;

    public StocksIntentService(){
        super("StocksIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, StocksWidgetProvider.class));


        Cursor cursor = getContentResolver().query(Contract.Quote.URI, cols, null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            cursor.close();
            return;
        }
        String symbol = cursor.getString(INDEX_SYMBOL);
        double price = cursor.getDouble(INDEX_PRICE);


    }
}
