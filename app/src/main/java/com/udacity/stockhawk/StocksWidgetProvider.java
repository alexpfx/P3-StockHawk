package com.udacity.stockhawk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.common.net.InetAddresses;
import com.google.common.util.concurrent.Striped;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockAdapter;

/**
 * Created by alexandre on 21/04/2017.
 */

public class StocksWidgetProvider extends AppWidgetProvider{
    private static final String ACTION_LOAD_STOCKS = "com.udacity.stockhawk.LOAD_STOCKS";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {

            Intent itService = new Intent(context, StocksWidgetService.class);
            itService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stock_list);
            views.setRemoteAdapter(R.id.widget_stocks_listview, itService);
            views.setEmptyView(R.id.widget_stocks_listview, R.id.tv_loading);


            Intent iLoadStocks = new Intent(context, AppWidgetProvider.class);
            iLoadStocks.setAction(ACTION_LOAD_STOCKS);
            iLoadStocks.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent piLoadStocks = PendingIntent.getBroadcast(context, 0, iLoadStocks, 0);
//            views.setOnClickPendingIntent(R.id.btn_load_stocks, piLoadStocks);


            Intent iOpen = new Intent(context, MainActivity.class);
            PendingIntent piOpen = PendingIntent.getActivity(context, 0, iOpen, 0);
//            views.setOnClickPendingIntent(R.id.tv_open, piOpen);

            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent == null){
            return;
        }


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equalsIgnoreCase(ACTION_LOAD_STOCKS)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
                return;
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stocks_listview);
        }



    }




    /*
    private static final String TAG = "StocksWidgetProvider";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        context.startService(new Intent(context, StocksIntentService.class));

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MainActivity.ACTION_DATA_UPDATED.equals(intent.getAction())){
            context.startService(new Intent(context, StocksIntentService.class));
        }
    }*/
}
