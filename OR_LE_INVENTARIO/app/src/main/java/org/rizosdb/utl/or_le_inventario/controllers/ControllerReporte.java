package org.rizosdb.utl.or_le_inventario.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.rizosdb.utl.or_le_inventario.R;
import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;
import org.rizosdb.utl.or_le_inventario.models.DetalleCompra;

import java.util.ArrayList;
import java.util.Calendar;

public class ControllerReporte extends AppCompatActivity {

    SQLiteDatabase db;

    private EditText txtDiaI,txtMesI,txtAnioI,txtDiaF,txtMesF,txtAnioF;
    private Button btnReporte, btnReporteD;
    private Spinner spnProductos;
    private TableLayout tblReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        iniciarComponentes();
        verReporteDia();

    }

    private void iniciarComponentes(){
        txtDiaI=(EditText)findViewById(R.id.txtDiaI);
        txtDiaF=(EditText)findViewById(R.id.txtDiaF);
        txtMesI=(EditText)findViewById(R.id.txtMesI);
        txtMesF=(EditText)findViewById(R.id.txtMesF);
        txtAnioI=(EditText)findViewById(R.id.txtAnioI);
        txtAnioF=(EditText)findViewById(R.id.txtAnioF);

        btnReporte=(Button) findViewById(R.id.btnReporte);
        btnReporteD=(Button) findViewById(R.id.btnReporteD);

        spnProductos=(Spinner)findViewById(R.id.spnProducts);

        tblReporte=(TableLayout)findViewById(R.id.tblReporte);

        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);

        btnReporteD.setOnClickListener(v -> {
            verReporteDia();
        });

        btnReporte.setOnClickListener(v -> {
            verReporteFecha();
        });

        Calendar c = Calendar.getInstance();
        int anioActual=c.get(Calendar.YEAR);

        txtAnioF.setText(String.valueOf(anioActual));
        txtAnioI.setText(String.valueOf(anioActual));
        txtAnioF.setEnabled(false);
        txtAnioI.setEnabled(false);

        ArrayList<String> listaProductos = new ArrayList<String>();
        Cursor c2=db.rawQuery("SELECT * FROM producto", null);
        listaProductos.add("Selecciona un producto");

        while(c2.moveToNext())
        {
            listaProductos.add(c2.getString(0)+" "+c2.getString(1));
            System.out.println("Producto: "+c2.getString(1));
        }

        ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaProductos);
        adapterProd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProductos.setAdapter(adapterProd);
    }

    private void verReporteDia(){

        destruirTabla();
        Calendar c = Calendar.getInstance();
        int diaActual=c.get(Calendar.DATE);
        int mesActual=c.get(Calendar.MONTH)+1;
        int anioActual=c.get(Calendar.YEAR);
        int contF=1;
        float subtotal=0;
        float iva=0;
        float total=0;
        String nombreCliente="";

        TableRow fila = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        fila.setLayoutParams(lp);

        fila.setBackgroundResource(R.drawable.border_table);

        String[] titulos = {"ID Venta ", "Cliente", "Unidades ", "Subtotal ", "Iva ", "Total "};
        for (int x = 0; x < titulos.length; x++) {
            TextView tvTitulo = new TextView(this);
            tvTitulo.setTypeface(null, Typeface.BOLD);
            tvTitulo.setText(titulos[x]);
            fila.addView(tvTitulo);
        }
        // Finalmente agregar la fila en la primera posición
        tblReporte.addView(fila, 0);
        String fechaActual=anioActual+"-0"+mesActual+"-"+diaActual;

        Cursor cb=db.rawQuery("SELECT * FROM ventas WHERE fechaVenta='"+fechaActual+"'", null);
        if(cb.getCount()==0)
        {
            showMessage("INFORMACION!", "No se encontraron registros de hoy "+fechaActual);
            return;
        }
        while(cb.moveToNext()) {
            TableRow filaDatos = new TableRow(this);
            filaDatos.setLayoutParams(lp);
            filaDatos.setBackgroundResource(R.drawable.border_table);
            subtotal = (float) (Float.parseFloat(cb.getString(5)));
            iva = (float) (subtotal * 0.16);
            total = subtotal + iva;

            String sql = "SELECT * FROM persona p INNER JOIN cliente c on p.id = c.idPersona" +
                    " WHERE c.id = '" + cb.getString(1) + "'";

            Cursor cn=db.rawQuery(sql, null);
            if(cn.moveToFirst())
            {
                nombreCliente= cn.getString(1);
            }


            String[] datos = {cb.getString(0)+" |",nombreCliente+" |",cb.getString(4)+" |",
            cb.getString(5)+" |",iva+" |",total+" |"};

            for (int x = 0; x < datos.length; x++) {
                TextView tvTitulo = new TextView(this);
                tvTitulo.setText(datos[x]);
                filaDatos.addView(tvTitulo);
            }
            tblReporte.addView(filaDatos, contF);
            contF++;

        }


    }

    private void verReporteFecha(){
        if(txtDiaI.getText().toString().trim().length() == 0 ||
                txtDiaF.getText().toString().trim().length() == 0||
                txtMesI.getText().toString().trim().length() == 0 ||
                txtMesF.getText().toString().trim().length() == 0 ||
                txtAnioF.getText().toString().trim().length() == 0 ||
                txtAnioI.getText().toString().trim().length() == 0 ||
                spnProductos.getSelectedItemPosition()==0
        ){
            showMessage("Error","Favor de llenar todos los campos");
        }else{
            destruirTabla();
            int diaI=Integer.parseInt(txtDiaI.getText().toString().trim());
            int diaF=Integer.parseInt(txtDiaF.getText().toString().trim());
            int mesI=Integer.parseInt(txtMesI.getText().toString().trim());
            int mesF=Integer.parseInt(txtMesF.getText().toString().trim());
            int anioI=Integer.parseInt(txtAnioI.getText().toString().trim());
            int anioF=Integer.parseInt(txtAnioF.getText().toString().trim());
            String fecha="";

            int contF=1;
            float iva=0;
            float subtotal=0;
            float total=0;

            TableRow filaI = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

            filaI.setLayoutParams(lp);

            filaI.setBackgroundResource(R.drawable.border_table);

            String[] titulos = {"ID Venta ","Fecha ", "Producto ", "Cantidad ", "Subtotal ", "Iva ", "Total "};
            for (int x = 0; x < titulos.length; x++) {
                TextView tvTitulo = new TextView(this);
                tvTitulo.setTypeface(null, Typeface.BOLD);
                tvTitulo.setText(titulos[x]);
                filaI.addView(tvTitulo);
            }
            // Finalmente agregar la fila en la primera posición
            tblReporte.addView(filaI, 0);

            String sql = "SELECT * FROM detalleVentas " +
                    " WHERE idProducto = '" + spnProductos.getSelectedItem().toString().substring(0,2)+ "'";

            Cursor cp=db.rawQuery(sql, null);
            while(cp.moveToNext())
            {
                Cursor cv=db.rawQuery("SELECT * FROM ventas WHERE idVenta='"+cp.getString(1)+"'",
                        null);
                if(cv.moveToFirst())
                {
                    fecha=cv.getString(3);
                }
                int dia=Integer.parseInt(fecha.substring(8));
                int mes=Integer.parseInt(fecha.substring(5,7));
                int anio=Integer.parseInt(fecha.substring(0,4));


                if(mes > mesI && mes < mesF){
                    TableRow fila = new TableRow(this);
                    fila.setLayoutParams(lp);

                    fila.setBackgroundResource(R.drawable.border_table);
                    subtotal=Float.parseFloat(cp.getString(4));
                    iva=(float)(subtotal*0.16);
                    total=iva+subtotal;
                    String[] datos={cp.getString(1)+" |"+fecha+" |"+spnProductos.getSelectedItem().toString()+" |",
                    cp.getString(3)+" |",cp.getString(4)+" |",iva+" |",total+" |"};
                    for (int x = 0; x < datos.length; x++) {
                        TextView tvTitulo = new TextView(this);
                        tvTitulo.setTypeface(null, Typeface.NORMAL);
                        tvTitulo.setText(datos[x]);
                        fila.addView(tvTitulo);
                    }
                    // Finalmente agregar la fila en la primera posición
                    tblReporte.addView(fila, contF);
                    contF++;

                }
                if (mes == mesI){
                   if(dia >= diaI){
                       TableRow fila = new TableRow(this);
                       fila.setLayoutParams(lp);

                       subtotal=Float.parseFloat(cp.getString(4));
                       iva=(float)(subtotal*0.16);
                       total=iva+subtotal;
                       String[] datos={cp.getString(1)+" |"+fecha+" |"+spnProductos.getSelectedItem().toString()+" |",
                               cp.getString(3)+" |",cp.getString(4)+" |",iva+" |",total+" |"};
                       for (int x = 0; x < datos.length; x++) {
                           TextView tvTitulo = new TextView(this);
                           tvTitulo.setTypeface(null, Typeface.NORMAL);
                           tvTitulo.setText(datos[x]);
                           fila.addView(tvTitulo);
                       }
                       // Finalmente agregar la fila en la primera posición
                       tblReporte.addView(fila, contF);
                       contF++;
                  }
                }
                if(mes == mesF){
                    if(dia <= diaF){
                        TableRow fila = new TableRow(this);
                        fila.setLayoutParams(lp);

                        subtotal=Float.parseFloat(cp.getString(4));
                        iva=(float)(subtotal*0.16);
                        total=iva+subtotal;
                        String[] datos={cp.getString(1)+" |"+fecha+" |"+spnProductos.getSelectedItem().toString()+" |",
                                cp.getString(3)+" |",cp.getString(4)+" |",iva+" |",total+" |"};
                        for (int x = 0; x < datos.length; x++) {
                            TextView tvTitulo = new TextView(this);
                            tvTitulo.setTypeface(null, Typeface.NORMAL);
                            tvTitulo.setText(datos[x]);
                            fila.addView(tvTitulo);
                        }
                        // Finalmente agregar la fila en la primera posición
                        tblReporte.addView(fila, contF);
                        contF++;
                    }
                }



            }



        }
    }
    private void destruirTabla(){
        int count = tblReporte.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tblReporte.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
