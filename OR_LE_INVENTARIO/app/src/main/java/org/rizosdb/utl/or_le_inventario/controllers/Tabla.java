package org.rizosdb.utl.or_le_inventario.controllers;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Tabla {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla

    /**
     * Constructor de la tabla
     * @param actividad Actividad donde va a estar la tabla
     * @param tabla TableLayout donde se pintará la tabla
     */
    public Tabla(Activity actividad, TableLayout tabla)
    {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }


    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    public void agregarFilaTabla(ArrayList<String[]> elementos)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);

        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            String[] column = elementos.get(i);

            for(String str:column){
                TextView texto = new TextView(actividad);
                if(i%2 == 0 && i != 0)
                    texto.setBackgroundColor(Color.rgb(141,218,236));
                else if(i != 0)
                    texto.setBackgroundColor(Color.rgb(236,209,141));
                texto.setText(str);
                texto.setGravity(Gravity.CENTER_HORIZONTAL);
                layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
                texto.setLayoutParams(layoutCelda);

                fila.addView(texto);
            }
            tabla.addView(fila);
            fila = new TableRow(actividad);
        }


        filas.add(fila);

        FILAS++;
    }

    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }
}
