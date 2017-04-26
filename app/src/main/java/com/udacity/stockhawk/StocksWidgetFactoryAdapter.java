package com.udacity.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexandre on 22/04/2017.
 */

public class StocksWidgetFactoryAdapter implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<StockItem> mItemList = new ArrayList<>();

    private static final String TAG = "StocksWidgetFactoryAdap";

    public StocksWidgetFactoryAdapter(Context context,  Intent intent) {
        mContext = context;
    }

    private static final DecimalFormat dolarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    public void onCreate() {
        Cursor cursor = mContext.getContentResolver()
                .query(Contract.Quote.URI,
                       new String[]{Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE},
                       null,
                       null, null);

        if (cursor == null){
            return;
        }

        mItemList.clear();
        while (cursor.moveToNext()){
            String symbol = cursor.getString(0);
            double price = cursor.getDouble(1);
            mItemList.add(new StockItem(symbol, dolarFormat.format(price)));
        }
        cursor.close();


    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mItemList.clear();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_stock_list_item);
        views.setTextViewText(R.id.tv_widget_stock_symbol, mItemList.get(position).getSymbol());
        views.setTextViewText(R.id.tv_widget_stock_price, String.valueOf(mItemList.get(position).getPrice()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setItemList(List<StockItem> itemList) {
        mItemList = itemList;
    }
}
