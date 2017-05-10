package com.udacity.stockhawk.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class FetchStockHistoryTask extends AsyncTask<Void, Void, Stock> {
    private String mSymbol;
    private IOnFetchStockHistoryTaskCompleted mListener;

    public FetchStockHistoryTask(String symbol, IOnFetchStockHistoryTaskCompleted listener) {
        mSymbol = symbol;
        mListener = listener;
    }

    @Override
    protected Stock doInBackground(Void... voids) {
        Stock stock = null;

        try {
            stock = YahooFinance.get(mSymbol, true);
        } catch (IOException exception) {
            Timber.e(exception, "Error fetching stock quotes");
        }

        return stock;
    }

    @Override
    protected void onPostExecute(Stock stock) {
        mListener.onFetchStockHistoryTaskCompleted(stock);
    }

    public interface IOnFetchStockHistoryTaskCompleted {
        void onFetchStockHistoryTaskCompleted(Stock stock);
    }
}
