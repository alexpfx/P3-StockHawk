package com.udacity.stockhawk.sync;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

import timber.log.Timber;


public class QuoteIntentService extends IntentService {

    public QuoteIntentService() {
        super(QuoteIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(final Intent intent) throws RuntimeException{
        Timber.d("Intent handled");

        QuoteSyncJob.getQuotes(getApplicationContext());
    }
}
