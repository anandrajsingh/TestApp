package in.android.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        RadarChart radarChart = findViewById(R.id.radarChart);

        ArrayList<RadarEntry> visitors = new ArrayList<>();
        visitors.add(new RadarEntry(435));
        visitors.add(new RadarEntry(445));
        visitors.add(new RadarEntry(455));
        visitors.add(new RadarEntry(475));
        visitors.add(new RadarEntry(535));
        visitors.add(new RadarEntry(485));
        visitors.add(new RadarEntry(515));

        RadarDataSet radarDataSet = new RadarDataSet(visitors, "Visitors");
        radarDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.BLACK);
        radarDataSet.setValueTextSize(16f);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        String[] labels = {"2015", "2016", "2017", "2018", "2019", "2020", "2021"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radarChart.setData(radarData);
    }
}