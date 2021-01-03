package com.example.paymenttracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class OverviewFragment extends Fragment {

    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_overview, container, false);

        pieChart = fragment_view.findViewById(R.id.piechart);
        return fragment_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateChart();
    }

    public void updateChart () {
        ArrayList<PieEntry> data = new ArrayList<>(9);
        float categoryTotals[] = new float[9];

        for (int i = 0; i < MainActivity.transactions.size(); i++) {
            int type = MainActivity.transactions.get(i).getType();
            if (type == TransactionType.SPEND) {
                categoryTotals[MainActivity.transactions.get(i).getCategory()] += MainActivity.transactions.get(i).getAmount();
            }
        }
        for (int j = 0; j < categoryTotals.length; j++) {
            if (categoryTotals[j] != 0) {
                data.add(new PieEntry(categoryTotals[j], getResources().getStringArray(R.array.categories)[j]));
            }
        }

        PieDataSet dataSet = new PieDataSet(data, "Transaction Breakdown");
        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);

        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(11f);
        // pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
    }
}
