package com.udacity.stockhawk.ui;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.Common;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

public class StockHistory extends AppCompatActivity {
    private String mSymbol;
    private String mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        mSymbol = getIntent().getStringExtra(Common.STOCK_SYMBOL);
        mHistory = getIntent().getStringExtra(Common.STOCK_HISTORY);
        loadStockHistoryData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(Common.STOCK_SYMBOL, mSymbol);
        outState.putString(Common.STOCK_HISTORY, mHistory);
    }

    protected void loadStockHistoryData() {
        if (mHistory != null) {
            List<Entry> entries = new ArrayList<Entry>();

            String[] lines = mHistory.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String[] pcs = lines[i].split(",");
                float value = Float.valueOf(pcs[1].trim());
                entries.add(new Entry(i, value));
            }

            LineChart chart = (LineChart) findViewById(R.id.chart_stock_history);
            Description description = new Description();
            description.setText(mSymbol + " " + getString(R.string.stock_history_description));
            description.setTextColor(R.color.colorAccent);
            chart.setDescription(description);
            LineDataSet dataSet = new LineDataSet(entries, mSymbol);
            dataSet.setValueTextColor(R.color.colorAccent);
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate();
        }
    }
}
