package com.example.paymenttracker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;


public class OverviewFragment extends Fragment {

    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_overview, container, false);

        pieChart = fragment_view.findViewById(R.id.piechart);
        return fragment_view;
    }

    public void updateChart (BigDecimal[] data){
        ArrayList<PieEntry> pieEntries = new ArrayList<>(9);
        BigDecimal totalCounter = BigDecimal.ZERO;

        for (int i = 0; i < data.length; i++){
            if (data[i].compareTo(BigDecimal.ZERO) != 0) {
                pieEntries.add(new PieEntry(Float.parseFloat(data[i].toPlainString()), getResources().getStringArray(R.array.categories)[i]));
                totalCounter = totalCounter.add(data[i]);
            }
        }

        final float totalAmount = Float.parseFloat(totalCounter.toPlainString());

        PieDataSet dataSet = new PieDataSet(pieEntries, "Transaction Breakdown");
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

        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value){
                return "$" + String.format(Locale.US,"%.2f", value) + " (" + Math.round((value/totalAmount)*100) + "%)";
            }
        });
        pieData.setValueTextSize(11f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        // pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
}
