package com.udacity.stockhawk.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.StocksWidgetDataProvider;

public class StocksWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StocksWidgetDataProvider(this, intent);
    }
}