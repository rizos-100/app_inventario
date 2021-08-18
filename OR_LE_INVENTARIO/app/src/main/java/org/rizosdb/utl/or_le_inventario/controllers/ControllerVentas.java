package org.rizosdb.utl.or_le_inventario.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.rizosdb.utl.or_le_inventario.R;
import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;
import org.rizosdb.utl.or_le_inventario.models.Cliente;
import org.rizosdb.utl.or_le_inventario.models.Producto;
import org.rizosdb.utl.or_le_inventario.models.Vendedor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ControllerVentas extends AppCompatActivity {
    TextView txtCliente,txtIdCliente,txtCalleVend,txtSpinVende,
            txtIdVend,txtComision,txtFechaVenta,txtSpinProd,txtCantPar,
            txtDescripTabla;
    EditText txtNumVenta;

    Button btnAgregarProdVen,btnAgregarVenta;

    ArrayList<Cliente> arrayClientes;
    ArrayList<Vendedor> arrayVendedores;
    ArrayList<Producto> arrayProductos, arrayProductosTable;
    Vendedor objVendedor;
    Cliente objCliente;
    Producto objProductoSlt;
    ArrayList<Integer> numPares;
    ConexionSQLiteHelper sqLiteHelper ;

    private TableLayout tbTabla;
    private String[] header = {"Clave ", "Descripción", "Unidad ", "Linea ","Cantidad ", "Precio ", "Importe "};
    private ArrayList<String[]> rows = new ArrayList<>();

    Tabla tabla;

    int paresTotal = 0;
    double suma = 0;
    double comision = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        sqLiteHelper = new ConexionSQLiteHelper(this,Utilidades.NOMBRE_DB,null, Utilidades.VERSION_DB);

        txtCliente = findViewById(R.id.txtCliente);
        txtIdCliente = findViewById(R.id.txtIdCliente);
        txtCalleVend = findViewById(R.id.txtCalleVend);
        txtSpinVende = findViewById(R.id.txtSpinVende);
        txtIdVend = findViewById(R.id.txtIdVend);
        txtComision = findViewById(R.id.txtComision);
        txtFechaVenta = findViewById(R.id.txtFechaVenta);
        txtNumVenta = findViewById(R.id.txtNumVenta);
        txtSpinProd = findViewById(R.id.txtSpinProd);
        btnAgregarProdVen = findViewById(R.id.btnAgregarProdVen);
        btnAgregarVenta = findViewById(R.id.btnAgregarVenta);
        txtCantPar = findViewById(R.id.txtCantPar);

        txtDescripTabla = findViewById(R.id.txtDescripTabla);

        tbTabla = findViewById(R.id.tbTabla);

        arrayClientes = consultarClientes();
        arrayVendedores = consultarVendedores();
        arrayProductos = consultarProductos();
        arrayProductosTable = new ArrayList<>();
        numPares = new ArrayList<>();

        Date objDate = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd '-' MMM '-' yyyy", new Locale("es", "MX"));
        txtFechaVenta.setText("Fecha: "+formateador.format(objDate));
        txtNumVenta.setText(""+consultarVentasTotal());


        txtCliente.setOnClickListener(v -> {
           iniciarSpinnerCliente();
        });
        txtSpinVende.setOnClickListener(v -> {
            iniciarSpinnerVendedor();
        });
        txtSpinProd.setOnClickListener(v -> {
            iniciarSpinnerProducto();
        });

        btnAgregarProdVen.setOnClickListener(v -> {
            agregarArrayTable();
        });

        btnAgregarVenta.setOnClickListener(v -> {
            agregarVenta();
        });

        tabla = new Tabla(this, tbTabla);


    }

    private ArrayList<Cliente> consultarClientes() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            String sql = "SELECT * FROM " + Utilidades.TABLA_PERSONAS+" p INNER JOIN "+Utilidades.TABLA_CLIENTES+
                    " c on p."+Utilidades.TABLA_PERSONA_ID+" = c."+Utilidades.TABLA_CLIENTES_ID_PERSONA;
            Cursor cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                Cliente objC = new Cliente();
                objC.setIdCliente(cursor.getInt(6));
                objC.setNombre(cursor.getString(1));
                objC.setDomicilio(cursor.getString(2));


                clientes.add(objC);
            }
            cursor.close();

        }catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(getApplication(),"Hubo un error o no se encontro la información",Toast.LENGTH_SHORT).show();
        }
        return clientes;
    }
    private ArrayList<String> llenarSpinnerCliente(ArrayList<Cliente> arrayCliente){
        ArrayList<String> arrayList = new ArrayList<>();

        for (Cliente c:arrayCliente) {

            arrayList.add(c.getNombre());
        }
        return  arrayList;
    }

    private ArrayList<Vendedor> consultarVendedores() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        ArrayList<Vendedor> vendedors = new ArrayList<>();

        try {
            String sql = "SELECT * FROM " + Utilidades.TABLA_PERSONAS+" p INNER JOIN "+Utilidades.TABLA_VENDEDORES+
                    " v on p."+Utilidades.TABLA_PERSONA_ID+" = v."+Utilidades.TABLA_VENDEDOR_ID_PERSONA;
            Cursor cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                Vendedor objV = new Vendedor();
                    objV.setIdVendedor(cursor.getInt(6));
                    objV.setNombre(cursor.getString(1));
                    objV.setComision(cursor.getDouble(8));

                vendedors.add(objV);
            }
            cursor.close();

        }catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(getApplication(),"Hubo un error o no se encontro la información",Toast.LENGTH_SHORT).show();
        }
        return vendedors;
    }
    private ArrayList<String> llenarSpinnerVendedor(ArrayList<Vendedor> arrayVendedores){
        ArrayList<String> arrayList = new ArrayList<>();

        for (Vendedor v:arrayVendedores) {

            arrayList.add(v.getNombre());
        }
        return  arrayList;
    }

    private ArrayList<Producto> consultarProductos() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            String sql = "SELECT * FROM producto" ;
            Cursor cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                Producto objP = new Producto();
                    objP.setIdProducto(cursor.getString(0));
                    objP.setNombre(cursor.getString(1));
                    objP.setLinea(cursor.getString(2));
                    objP.setExistencia(cursor.getFloat(3));
                    objP.setPrecioPromedio(cursor.getFloat(5));

                productos.add(objP);
            }
            cursor.close();

        }catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(getApplication(),"Hubo un error o no se encontro la información",Toast.LENGTH_SHORT).show();
        }
        return productos;
    }
    private ArrayList<String> llenarSpinnerProductos(ArrayList<Producto> arrayProductos){
        ArrayList<String> arrayList = new ArrayList<>();

        for (Producto p:arrayProductos) {

            arrayList.add((p.getNombre()+" - "+p.getLinea()));
        }
        return  arrayList;
    }

    private void iniciarSpinnerCliente(){
        Dialog dialog = new Dialog(ControllerVentas.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        dialog.getWindow().setLayout(950,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ControllerVentas.this,
                android.R.layout.simple_list_item_1,llenarSpinnerCliente(arrayClientes));
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtCliente.setText(arrayClientes.get(position).getNombre());
                txtIdCliente.setText("ID: "+arrayClientes.get(position).getIdCliente());
                txtCalleVend.setText("Calle: "+arrayClientes.get(position).getDomicilio());
                objCliente = arrayClientes.get(position);


                dialog.dismiss();
            }
        });
    }
    private void iniciarSpinnerVendedor(){
        Dialog dialog = new Dialog(ControllerVentas.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        dialog.getWindow().setLayout(950,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ControllerVentas.this,
                android.R.layout.simple_list_item_1,llenarSpinnerVendedor(arrayVendedores));
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtSpinVende.setText(arrayVendedores.get(position).getNombre());
                txtIdVend.setText("ID: "+arrayVendedores.get(position).getIdVendedor());
                txtComision.setText("Comisión: "+arrayVendedores.get(position).getComision()+"%");
                objVendedor = arrayVendedores.get(position);


                dialog.dismiss();
            }
        });
    }
    private void iniciarSpinnerProducto(){
        Dialog dialog = new Dialog(ControllerVentas.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        dialog.getWindow().setLayout(950,950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ControllerVentas.this,
                android.R.layout.simple_list_item_1,llenarSpinnerProductos(arrayProductos));
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtSpinProd.setText(arrayProductos.get(position).getNombre());
                objProductoSlt = arrayProductos.get(position);

                dialog.dismiss();
            }
        });
    }

    private int consultarVentasTotal() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
       int count = 0;

        try {
            String sql = "SELECT * FROM " + Utilidades.TABLA_VENTAS;
            Cursor cursor = db.rawQuery(sql,null);
            count = cursor.getCount()+1;

            cursor.close();

        }catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(getApplication(),"Hubo un error o no se encontro la información",Toast.LENGTH_SHORT).show();
        }
        return count;
    }

    private void agregarArrayTable(){
        arrayProductosTable.add(objProductoSlt);
        numPares.add(Integer.parseInt(txtCantPar.getText().toString()));

        try {
            tabla.agregarFilaTabla(getInfo(arrayProductosTable,numPares));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String[]> getInfo(ArrayList<Producto> datos,ArrayList<Integer> pares) throws ParseException {
        paresTotal = 0;
        suma = 0;
        comision = 0;

        tbTabla.removeAllViews();
        rows =  new ArrayList<>();
        rows.add(header);
        for (int i = 0; i < datos.size(); i++){
            float  importe = pares.get(i) * datos.get(i).getPrecioPromedio();
            String[] row = {
                    datos.get(i).getIdProducto(),
                    datos.get(i).getNombre(),
                    "Par",
                    datos.get(i).getLinea(),
                    String.valueOf(pares.get(i)),
                    String.valueOf(datos.get(i).getPrecioPromedio()),
                    String.valueOf(importe)
            };
            paresTotal +=pares.get(i);
            suma += importe;
            rows.add(row);
        }
        comision = (suma*1.16)*objVendedor.getComision()/100;
        txtDescripTabla.setText("Total de pares: "+paresTotal+"\n"+
                "Suma: "+suma+"\n"+
                "IVA: "+(suma*0.16)+"  "+
                "Total: "+(suma*1.16)+"\n"+
                "Comisión "+comision);
        return rows;
    }

    private void agregarVenta(){
        if(validarTexto()){
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(Utilidades.TABLA_VENTA_ID, txtNumVenta.getText().toString());
                values.put(Utilidades.TABLA_VENTA_ID_CLIENTE, objCliente.getIdCliente());
                values.put(Utilidades.TABLA_VENTA_ID_VENDEDOR,objVendedor.getIdVendedor());
                values.put(Utilidades.TABLA_VENTA_FECHA,txtFechaVenta.getText().toString());
                values.put(Utilidades.TABLA_VENTA_TOTAL_PARES, paresTotal);
                values.put(Utilidades.TABLA_VENTA_PRECIO_TOTAL, suma);
                values.put(Utilidades.TABLA_VENTA_COMISION_VEN, comision);

                Long idVenta = db.insert(Utilidades.TABLA_VENTAS, Utilidades.TABLA_VENTA_ID, values);

                /**/

                if(idVenta > 0) {
                    for (int i = 0; i < arrayProductosTable.size(); i++){

                        db.execSQL("UPDATE producto SET "
                                +"existencia= "+ (arrayProductosTable.get(i).getExistencia()-numPares.get(i)) + " "
                                +" WHERE numero='"+arrayProductosTable.get(i).getIdProducto()+"';");;

                        ContentValues valuesFore = new ContentValues();
                        valuesFore.put(Utilidades.TABLA_DETALLE_VENTA_ID_VENTA, idVenta);
                        valuesFore.put(Utilidades.TABLA_DETALLE_VENTA_ID_PRODUCTO, arrayProductosTable.get(i).getIdProducto());
                        valuesFore.put(Utilidades.TABLA_DETALLE_VENTA_CANTIDAD_PAR, numPares.get(i));
                        valuesFore.put(Utilidades.TABLA_DETALLE_VENTA_IMPORTE, (numPares.get(i) *
                                arrayProductosTable.get(i).getPrecioPromedio())
                        );
                        Long idDetalle = db.insert(Utilidades.TABLA_DETALLE_VENTAS, Utilidades.TABLA_DETALLE_VENTA_ID, valuesFore);
                    }

                    db.setTransactionSuccessful();
                    showMessage("Venta registrada con exito", "Se ingreso con el ID: " + idVenta);
                }else
                    showMessage("Falló la venta","Fallo el registro de la venta");

                clearText(this);
            }catch (Exception e){
                e.printStackTrace();

            }finally {
                db.endTransaction();
                db.close();
            }
        }else
            showMessage("Datos incompletos","Porfavor, verifica que todos los " +
                    "datos esten llenados correctamente");
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public boolean validarTexto(){
        if(objVendedor != null && objCliente != null && arrayProductosTable.size() > 0)
            return true;
        else
            return false;

    }

    public void clearText(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }
}