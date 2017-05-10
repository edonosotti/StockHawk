package com.udacity.stockhawk.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StocksWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    List<StockDataItem> mCollection = new ArrayList<>();
    Context mContext = null;
    Intent mIntent = null;

    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public StocksWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        StockDataItem stockDataItem = mCollection.get(position);

        String price = dollarFormat.format(stockDataItem.price);
        String changePercent = percentageFormat.format(stockDataItem.changePercent / 100);

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        view.setTextViewText(R.id.symbol, stockDataItem.symbol);
        view.setTextViewText(R.id.price, price);
        view.setTextViewText(R.id.change, changePercent);

        if (stockDataItem.changeAbsolute > 0) {
            view.setInt(R.id.change, "setBackgroundColor", Color.GREEN);
        } else {
            view.setInt(R.id.change, "setBackgroundColor", Color.RED);
        }

        return view;
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
        return true;
    }

    private void initData() {
        mCollection.clear();

        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Contract.Quote.COLUMN_SYMBOL,
                Contract.Quote.COLUMN_PRICE,
                Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
                Contract.Quote.COLUMN_PERCENTAGE_CHANGE
        };

        String sortOrder = Contract.Quote.COLUMN_SYMBOL;

        Cursor cursor = db.query(Contract.Quote.TABLE_NAME,
                projection, null, null, null, null, sortOrder
        );

        while (cursor.moveToNext()) {

            String symbol = cursor.getString(
                    cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_SYMBOL));
            float quote = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_PRICE));
            float changePercent = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));
            float changeAbsolute = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));

            StockDataItem stockDataItem = new StockDataItem();
            stockDataItem.symbol = symbol;
            stockDataItem.price = quote;
            stockDataItem.changePercent = changePercent;
            stockDataItem.changeAbsolute = changeAbsolute;

            mCollection.add(stockDataItem);
        }

        cursor.close();
    }
}