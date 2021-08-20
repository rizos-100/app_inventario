package org.rizosdb.utl.or_le_inventario.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.rizosdb.utl.or_le_inventario.R;
import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;
import org.rizosdb.utl.or_le_inventario.models.Producto;

import java.util.ArrayList;

public class ControllerGrafica extends AppCompatActivity implements OnChartValueSelectedListener{
    private PieChart chart;
    private BarChart chartC;
    private Typeface tf;
    private TextView txtClienteSlt;


    ConexionSQLiteHelper sqLiteHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);
        sqLiteHelper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_DB,null, Utilidades.VERSION_DB);

        generarGraficaProductos();

        chartC = findViewById(R.id.chartC);
        txtClienteSlt = findViewById(R.id.txtClienteSlt);
        chartC.getDescription().setEnabled(false);
        chartC.setMaxVisibleValueCount(60);
        chartC.setPinchZoom(true);
        chartC.setDrawBarShadow(false);
        chartC.setDrawGridBackground(false);
        chartC.getAxisLeft().setDrawGridLines(false);
        chartC.animateY(2000);
        chartC.getLegend().setEnabled(false);

        ArrayList<Producto> productos = obtenerClienteMasCompra();
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < productos.size(); i++) {

            values.add(new BarEntry(i,
                    productos.get(i).getExistencia(),
                    productos.get(i).getNombre())
            );
            chartC.setX(8);
        }

        BarDataSet set1;

        if (chartC.getData() != null &&
                chartC.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chartC.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chartC.getData().notifyDataChanged();
            chartC.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chartC.setData(data);
            chartC.setFitBars(true);
        }

        chartC.invalidate();

        chartC.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.e("info","E-> "+e.getData()+" H-> "+h.toString());

                txtClienteSlt.setText("Cliente: "+e.getData());
            }

            @Override
            public void onNothingSelected() {
                txtClienteSlt.setText("Seleccione una barra tocandola");
            }
        });

    }

    public ArrayList<Producto> obtenerProductoMasVendido(){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            String sql = "SELECT Sum(cantidadPar) as cantidad, p.nombre FROM detalleVentas dv " +
                    " INNER JOIN producto p on p.numero = dv.idProducto " +
                    " GROUP BY  idProducto ORDER BY cantidad";
            Cursor c = db.rawQuery(sql, null);
            if (!c.moveToFirst()) {
                showMessage("Error!", "No se encontraron registros");
                return null;
            }

            while (c.moveToNext()) {
                Producto objP = new Producto();
                objP.setExistencia(c.getInt(0));
                objP.setNombre(c.getString(1));
                productos.add(objP);
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {

            db.close();
        }

        return productos;
    }

    public ArrayList<Producto> obtenerClienteMasCompra(){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            String sql = " SELECT  SUM(totalPares),p.nombre FROM ventas v " +
                    " INNER JOIN cliente c on c.id = v.idCliente " +
                    " INNER JOIN persona p on c.idPersona = p.id " +
                    " GROUP BY idCliente;";
            Cursor c = db.rawQuery(sql, null);
            if (!c.moveToFirst()) {
                showMessage("Error!", "No se encontraron registros");
                return null;
            }

            while (c.moveToNext()) {
                Producto objP = new Producto();
                objP.setExistencia(c.getInt(0));
                objP.setNombre(c.getString(1));
                productos.add(objP);
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {

            db.close();
        }

        return productos;
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Sistema de Inventario\nProducto mas vendido");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 21, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 21, s.length() -1 , 0);


        return s;
    }

    private void setDataProductos() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Producto> productos = obtenerProductoMasVendido();

        for (int i = 0; i < productos.size(); i++) {
            PieEntry pieEntry = new PieEntry(
                    productos.get(i).getExistencia(),
                    productos.get(i).getNombre());
            entries.add(pieEntry);

        }
        PieDataSet dataSet = new PieDataSet(entries, "Productos mas Vendidos");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ColorTemplate.rgb("2C87C9"));
        colors.add(ColorTemplate.rgb("C92C2C"));
        colors.add(ColorTemplate.rgb("FC7421"));
        colors.add(ColorTemplate.rgb("9821FC"));
        colors.add(ColorTemplate.rgb("0D9756"));

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();

    }

    private void generarGraficaProductos(){
        chart = findViewById(R.id.chartP);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterText(generateCenterSpannableText());

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        setDataProductos();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.e("info","E-> "+e.toString()+" H->"+h.toString());
    }

    @Override
    public void onNothingSelected() {

    }
}