package org.rizosdb.utl.or_le_inventario;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ControllerProducto extends AppCompatActivity {

    private EditText txtNum,txtNombre,txtLinea,txtExistencia,
            txtPrecioCosto,txtPrecioPromedio,txtPrecioVenta1,txtPrecioVenta2;
    private Button btnAgregar,btnEliminar,btnConsultar,btnModifcar,btnLista;
    private Spinner spnLinea;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        iniciarComponentes();
    }

    private void iniciarComponentes(){
        txtNum=(EditText)findViewById(R.id.txtID);
        txtNombre=(EditText)findViewById(R.id.txtNombre);
        spnLinea=(Spinner)findViewById(R.id.spnLinea);
        txtExistencia=(EditText)findViewById(R.id.txtExistencia);
        txtPrecioCosto=(EditText)findViewById(R.id.txtPrecioCosto);
        txtPrecioPromedio=(EditText)findViewById(R.id.txtPrecioPromedio);
        txtPrecioVenta1=(EditText)findViewById(R.id.txtPrecioVenta1);
        txtPrecioVenta2=(EditText)findViewById(R.id.txtPrecioVenta2);
        //txtNum.setEnabled(false);
        txtPrecioVenta1.setEnabled(false);
        txtPrecioVenta2.setEnabled(false);

        ArrayList<String> listaLinea = new ArrayList<String>();
        listaLinea.add("Hombre 25-30");
        listaLinea.add("Joven 22-25");
        listaLinea.add("Niño 18-21");
        listaLinea.add("Niño 15-17");
        listaLinea.add("Niño 12-14");
        listaLinea.add("Dama 22-25");
        listaLinea.add("Niña 18-21");
        listaLinea.add("Niña 15-17");
        listaLinea.add("Niña 12-14");
        listaLinea.add("BEBE 10-12");

        ArrayAdapter<String> adapterV = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaLinea);
        adapterV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLinea.setAdapter(adapterV);



        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);
        btnConsultar=(Button)findViewById(R.id.btnConsultar);
        btnModifcar=(Button)findViewById(R.id.btnModificar);
        btnLista=(Button)findViewById(R.id.btnLista);

        btnAgregar.setOnClickListener(v -> {
            agregarProducto();
        });

        btnLista.setOnClickListener(v -> {
            verTodos();
        });

        btnConsultar.setOnClickListener(v -> {
            consultar();
        });

        btnModifcar.setOnClickListener(v -> {
            modificar();
        });

        btnEliminar.setOnClickListener(v -> {
            eliminar();
        });

        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS producto(numero VARCHAR,nombre VARCHAR,linea VARCHAR," +
                "existencia FLOAT,precioCosto FLOAT,precioPromedio FLOAT,precioVenta1 FLOAT,precioVenta2 FLOAT);");


    }

    private void agregarProducto(){
        if(txtNombre.getText().toString().trim().length()==0||
                txtExistencia.getText().toString().trim().length()==0||
                txtPrecioCosto.getText().toString().trim().length()==0 ||
                txtPrecioPromedio.getText().toString().trim().length()==0
        )
        {
            showMessage("Error!", "Porfavor introduce todos los valores");
            return;
        }
        float precioCosto=Float.parseFloat(txtPrecioCosto.getText().toString().trim());
        float precioventa1= (float) (precioCosto*1.4);
        float precioventa2= (float) (precioCosto*1.28);

        db.execSQL("INSERT INTO producto VALUES('" + asignarClave() + "','"
                + txtNombre.getText().toString().trim() + "','"
                + spnLinea.getSelectedItem().toString() + "','"
                + txtExistencia.getText().toString().trim() + "','"
                + precioCosto + "','"
                + Float.parseFloat(txtPrecioPromedio.getText().toString().trim()) + "',"
                + precioventa1 + ","
                + precioventa2+");");
        showMessage("Exito!", "Registro agregado");
        clearText();

    }

    private void verTodos(){
        Cursor c=db.rawQuery("SELECT * FROM producto", null);
        if(c.getCount()==0)
        {
            showMessage("Error!", "No se encontraron registros");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            buffer.append("Numero.: " + c.getString(0)+"\n");
            buffer.append("Nombre.: " + c.getString(1)+"\n");
            buffer.append("Linea.: " + c.getString(2)+"\n");
            buffer.append("Existencia.: " + c.getFloat(3)+"\n");
            buffer.append("Precio Costo.: " + c.getFloat(4)+"\n");
            buffer.append("Precio Promedio.: " + c.getFloat(5)+"\n");
            buffer.append("Precio Venta 1.: " + c.getFloat(6)+"\n");
            buffer.append("Precio Venta 2.: " + c.getFloat(7)+"\n\n");
        }
        showMessage("Registros", buffer.toString());
    }

    private void consultar(){
        txtPrecioVenta1.setEnabled(true);
        txtPrecioVenta2.setEnabled(true);
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }
        Cursor      c=db.rawQuery("SELECT * FROM producto WHERE numero ='"+txtNum.getText().toString().trim()+"'", null);
        if(c.moveToFirst())
        {
            String linea=c.getString(2);
            switch (linea){
                case"Hombre 25-30":
                    spnLinea.setSelection(0);
                    break;
                case"Joven 22-25":
                    spnLinea.setSelection(1);
                    break;
                case"Niño 18-21":
                    spnLinea.setSelection(2);
                    break;
                case"Niño 15-17":
                    spnLinea.setSelection(3);
                    break;
                case"Niño 12-14":
                    spnLinea.setSelection(4);
                    break;
                case"Dama 22-25":
                    spnLinea.setSelection(5);
                    break;
                case"Niña 18-21":
                    spnLinea.setSelection(6);
                    break;
                case"Niña 15-17":
                    spnLinea.setSelection(7);
                    break;
                case"Niña 12-14":
                    spnLinea.setSelection(8);
                    break;
                case"BEBE 10-12":
                    spnLinea.setSelection(9);
                    break;

            }
            txtNombre.setText(c.getString(1));
            txtExistencia.setText(c.getString(3));
            txtPrecioCosto.setText(c.getString(4));
            txtPrecioPromedio.setText(c.getString(5));
            txtPrecioVenta1.setText(c.getString(6));
            txtPrecioVenta2.setText(c.getString(7));

            txtNum.setEnabled(false);
            txtPrecioVenta1.setEnabled(false);
            txtPrecioVenta2.setEnabled(false);
        }
        else
        {
            showMessage("Error!", "Clave no valida");
            clearText();
        }
    }

    private void modificar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }
        float precioCosto=Float.parseFloat(txtPrecioCosto.getText().toString().trim());
        float precioventa1= (float) (precioCosto*1.4);
        float precioventa2= (float) (precioCosto*1.28);

        db.execSQL("UPDATE producto SET "
                +"nombre= '"+ txtNombre.getText().toString().trim() + "',"
                +"linea= '"+ spnLinea.getSelectedItem().toString()  + "',"
                +"existencia= "+ txtExistencia.getText().toString().trim() + ","
                +"precioCosto= "+ precioCosto + ","
                +"precioPromedio= "+ txtPrecioPromedio.getText().toString().trim() + ","
                +"precioVenta1= "+ precioventa1 + ","
                +"precioVenta2= "+ precioventa2 +""
                +" WHERE numero='"+txtNum.getText()+"';");

        showMessage("Exito!", "Registro actualizado");
        txtNum.setEnabled(true);
        clearText();
    }

    private void eliminar()
    {
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }
        db.execSQL("DELETE FROM producto WHERE numero='"+txtNum.getText().toString().trim()+"';");
        showMessage("Exito!", "Registro eliminado");
        txtNum.setEnabled(true);
        clearText();
    }



    private String asignarClave(){
        String clave="";
        String inicial="";
        int pos = spnLinea.getSelectedItemPosition();
        switch(pos){
            case 0:
                inicial="A";
                break;
            case 1:
                inicial="B";
                break;
            case 2:
                inicial="C";
                break;
            case 3:
                inicial="D";
                break;
            case 4:
                inicial="E";
                break;
            case 5:
                inicial="R";
                break;
            case 6:
                inicial="S";
                break;
            case 7:
                inicial="T";
                break;
            case 8:
                inicial="U";
                break;
            case 9:
                inicial="X";
                break;

        }
        Cursor c = db.rawQuery(" SELECT numero FROM producto WHERE numero like '"+inicial+"%' ", null);
        int codigo=1;
        if(c.getCount()> 0){
            c.moveToLast();
            codigo=Integer.parseInt(c.getString(0).substring(1))+1;
        }
        clave=inicial+codigo;
        return clave;
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        txtNum.setText("");
        txtNombre.setText("");
        txtExistencia.setText("");
        txtPrecioCosto.setText("");
        txtPrecioPromedio.setText("");
        txtPrecioVenta1.setText("");
        txtPrecioVenta2.setText("");
    }

}
