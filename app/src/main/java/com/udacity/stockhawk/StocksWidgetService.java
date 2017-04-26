package com.udacity.stockhawk;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by alexandre on 22/04/2017.
 */

public class StocksWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new StocksWidgetFactoryAdapter(this, intent);
    }
}
