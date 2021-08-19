package org.rizosdb.utl.or_le_inventario.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.rizosdb.utl.or_le_inventario.R;

import java.util.ArrayList;

public class ControllerGrafica extends AppCompatActivity {
    private LineChart lineChart;
    private LineDataSet lineDataSet,lineDataSet2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        lineChart = findViewById(R.id.lineChart);

        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i<11; i++){
            float y = (int) (Math.random() * 8) + 1;
            lineEntries.add(new Entry((float) i,(float)y));
        }
        lineDataSet = new LineDataSet(lineEntries, "Producto 1");

        ArrayList<Entry> lineEntries2 = new ArrayList<>();
        for (int i = 0; i<11; i++){
            float y = (int) (Math.random() * 8) + 1;
            lineEntries2.add(new Entry((float) i,(float)y));
        }
        lineDataSet2 = new LineDataSet(lineEntries2, "Producto 2");
        lineDataSet2.setColor(Color.rgb(152,0,0));

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineData.addDataSet(lineDataSet2);
        lineChart.setData(lineData);
    }
}