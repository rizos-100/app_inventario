package org.rizosdb.utl.or_le_inventario.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import org.rizosdb.utl.or_le_inventario.models.Compra;
import org.rizosdb.utl.or_le_inventario.models.DetalleCompra;
import org.rizosdb.utl.or_le_inventario.models.Producto;

import java.util.ArrayList;
import java.util.Calendar;

public class ControllerCompras extends AppCompatActivity {

    ConexionSQLiteHelper sqLiteHelper ;
    SQLiteDatabase db;

    private EditText txtNum,txtFecha,txtCantidad;
    private TextView txtSubtotal,txtTotal,txtTotalPares,txtIva;
    private Button btnAgregar,btnEliminar,btnConsultar,btnModifcar,btnLista,btnAñadirDetalle,btnUpdateDet;
    private Spinner spnProveedores,spnProductos;
    private TableLayout tblProductos;
    private ArrayList<DetalleCompra> detalles=new ArrayList<>();
    private int contFilas=1;
    private int contDet=0;
    private float subtotal=0,total=0,iva=0,totalPrs=0;

    String idDetalleActual="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        iniciarComponentes();
        iniciarListas();
        iniciarTabla();
    }
    //@SuppressLint("CutPasteId")
    private void iniciarComponentes(){

        txtNum=(EditText)findViewById(R.id.txtIdCompra);
        txtFecha=(EditText)findViewById(R.id.txtFecha);
        txtCantidad=(EditText)findViewById(R.id.txtCantidad);
        txtSubtotal=(TextView)findViewById(R.id.txtSubtotal);
        txtTotal=(TextView) findViewById(R.id.txtTotal);
        txtTotalPares=(TextView) findViewById(R.id.txtTotalPares);
        txtIva=(TextView) findViewById(R.id.txtIva);
        spnProductos=(Spinner)findViewById(R.id.spnProductos);
        spnProveedores=(Spinner)findViewById(R.id.spnProveedores);

        tblProductos=(TableLayout)findViewById(R.id.tblDetalleCompra);

        btnAgregar=(Button)findViewById(R.id.btnAgregarVenta);
        btnEliminar=(Button)findViewById(R.id.btnEliminarVenta);
        btnConsultar=(Button)findViewById(R.id.btnConsultarVenta);
        btnModifcar=(Button)findViewById(R.id.btnModificarVenta);
        btnLista=(Button)findViewById(R.id.btnListaVenta);
        btnAñadirDetalle=(Button)findViewById(R.id.btnAgregarProd);
        btnUpdateDet=(Button)findViewById(R.id.btnUpdateDet);

        obtenerFecha();

        btnAñadirDetalle.setOnClickListener(v -> {
            añadirDetalle();
        });
        btnAgregar.setOnClickListener(v -> {
            agregarCompra();
        });
        btnLista.setOnClickListener(v -> {
            verTodos();
        });
        btnConsultar.setOnClickListener(v -> {
            consultar();
        });
        btnEliminar.setOnClickListener(v -> {
            eliminar();
        });
        btnModifcar.setOnClickListener(v -> {
            actualizar();
        });

        btnUpdateDet.setOnClickListener(v -> {
            actualizarDetalle(idDetalleActual);
        });


        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Compra(idCompra TEXT PRIMARY KEY," +
                "idProveedor TEXT,"+
                "fecha TEXT,totalPago FLOAT, totalPares FLOAT,"+
                "CONSTRAINT fk_proveedor_compra" +
                " FOREIGN KEY (idProveedor) REFERENCES proveedor(numero))");

        db.execSQL("CREATE TABLE IF NOT EXISTS detalleCompra(idDetalleCompra TEXT PRIMARY KEY, " +
                "idCompra TEXT, idProducto TEXT,"+
                "cantidad FLOAT,costo FLOAT, importe FLOAT,"+
                        "CONSTRAINT fk_detalle_compra" +
                " FOREIGN KEY (idCompra) REFERENCES compra(idCompra)," +
                "CONSTRAINT fk_detalle_producto" +
                " FOREIGN KEY (idProducto) REFERENCES producto(numero))");



    }

    private void agregarCompra(){
        Compra compra= new Compra();
        if(spnProveedores.getSelectedItemPosition()==0 || detalles.size()==0){
            showMessage("Error","Debes elegir un proveedor válido y agregar poductos a la compra");
        }else{
            compra.setIdCompra(asignarClaveC());
            compra.setFecha(txtFecha.getText().toString());
            compra.setTotalPago(total);
            compra.setTotalPares(totalPrs);
            compra.setDetallesCompra(detalles);
            String idProveedor = spnProveedores.getSelectedItem().toString().substring(0,2);

            //db.beginTransaction();
            try{
                db.execSQL("INSERT INTO compra VALUES('" + compra.getIdCompra() + "','"
                        + idProveedor + "','"
                        + compra.getFecha() + "',"
                        + compra.getTotalPago() + ","
                        + compra.getTotalPares() + ");");

                for(int i=0; i< detalles.size();i++){
                    db.execSQL("INSERT INTO detalleCompra VALUES('" + asignarClaveDC() + "','"
                            + compra.getIdCompra() + "','"
                            + detalles.get(i).getProducto().getIdProducto() + "',"
                            + detalles.get(i).getCantidad()+ ","
                            + detalles.get(i).getCostoUnitario()+ ","
                            + detalles.get(i).getImporte() + ");");

                    Cursor c=db.rawQuery("SELECT existencia FROM producto WHERE numero ='"+detalles.get(i).getProducto().getIdProducto()+"'", null);
                    float existencias= (float) 0.0;
                    if(c.moveToFirst())
                    {
                        existencias=c.getFloat(0)+detalles.get(i).getCantidad();
                    }
                    db.execSQL("UPDATE producto SET existencia="+existencias+
                            " WHERE numero ='"+detalles.get(i).getProducto().getIdProducto()+"'");
                }

            }catch(Exception e){
                e.printStackTrace();
                showMessage("Error!", e.toString());

            }finally {
                //db.endTransaction();
                //db.close();
                showMessage("Exito!", "Registro agregado");
                clearText();

                detalles=new ArrayList<>();
                contFilas=1;
                contDet=0;
                subtotal=0;
                total=0;
                iva=0;
                totalPrs=0;

                destruirTabla();
            }

        }
    }

    private void destruirTabla(){
        int count = tblProductos.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tblProductos.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }

    private void verTodos(){
        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM compra", null);
        if(c.getCount()==0)
        {
            showMessage("Error!", "No se encontraron registros");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
            String idCompra=c.getString(0);
            buffer.append("Numero.: " + c.getString(0)+"\n");
            buffer.append("idProveedor.: " + c.getString(1)+"\n");
            buffer.append("Fecha.: " + c.getString(2)+"\n");
            buffer.append("Total pagado: $ " + c.getString(3)+"\n");
            buffer.append("Total Pares.: " + c.getString(4)+"\n");
            buffer.append("DETALLE COMPRA..................: \n");
            Cursor c2=db.rawQuery("SELECT * FROM detalleCompra WHERE idCompra="+"idCompra", null);
            while(c2.moveToNext())
            {
                buffer.append("Clave Producto: "+c2.getString(2)+"\n");
                buffer.append("Cantidad: "+c2.getString(3)+"\n");
                buffer.append("Costo Unitario $: "+c2.getString(4)+"\n");
                buffer.append("_____________________ \n");
            }
            buffer.append("\n");

        }
        showMessage("Registros", buffer.toString());
    }

    private void consultar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }else{
            Cursor c=db.rawQuery("SELECT * FROM compra WHERE idCompra='"+txtNum.getText().toString().trim()+"'", null);
            if(c.moveToFirst())
            {
                txtFecha.setText(c.getString(2));
                spnProveedores.setSelection(Integer.parseInt(c.getString(1).substring(1)));
                total=Float.parseFloat(c.getString(3));
                iva=(total/116)*16;
                subtotal=total-iva;
                txtSubtotal.setText("Subtotal $"+subtotal);
                txtIva.setText("IVA: $"+iva);
                txtTotal.setText("Total: $"+c.getString(3));
                txtTotalPares.setText("Total prs: "+c.getString(4));
                Cursor c2=db.rawQuery("SELECT * FROM detalleCompra WHERE idCompra='"+txtNum.getText().toString().trim()+"'", null);
                detalles=new ArrayList<>();
                while(c2.moveToNext())
                {
                    DetalleCompra dc = new DetalleCompra();
                    dc.setIdDetalleCompra(c2.getString(0));
                    dc.setCantidad(Float.parseFloat(c2.getString(3)));
                    dc.setCostoUnitario(Float.parseFloat(c2.getString(4)));
                    dc.setImporte(Float.parseFloat(c2.getString(5)));

                    Producto p= new Producto();
                    Cursor cp=db.rawQuery("SELECT * FROM producto WHERE numero ='"+c2.getString(2)+"'", null);
                    if(cp.moveToFirst()) {
                        p.setIdProducto(cp.getString(0));
                        p.setNombre(cp.getString(1));
                        p.setLinea(cp.getString(2));
                        p.setExistencia(Float.parseFloat(cp.getString(3)));
                        p.setPrecioCosto(Float.parseFloat(cp.getString(4)));
                        p.setPrecioPromedio(Float.parseFloat(cp.getString(5)));
                        p.setPrecioVenta1(Float.parseFloat(cp.getString(6)));
                        p.setPrecioVenta2(Float.parseFloat(cp.getString(7)));
                    }
                    dc.setProducto(p);
                    detalles.add(dc);

                }
            }

            agregarDetallesTabla(detalles);
        }

    }

    private void eliminar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }else{
         db.execSQL("DELETE FROM Compra WHERE idCompra='"+txtNum.getText()+"';");
         int pos=1;
         for(int i=0; i<detalles.size();i++){
            eliminarDetalle(detalles.get(i),pos);
            pos++;
         }
         clearText();
         showMessage("Exito!", "Registro eliminado");
        }
    }

    private void actualizar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }else{
            db.execSQL("UPDATE compra SET "
                    +"idProveedor= '"+ spnProveedores.getSelectedItem().toString().substring(0,2) + "'"
                    +" WHERE idCompra='"+txtNum.getText()+"';");
            showMessage("Exito!", "Registro actualizado");
        }

    }

    private void eliminarDetalle(DetalleCompra dc,int pos){
        View child = tblProductos.getChildAt(pos);
        if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        db.execSQL("DELETE FROM detalleCompra WHERE idDetalleCompra='"+dc.getIdDetalleCompra()+"';");
        Cursor c=db.rawQuery("SELECT * FROM producto WHERE numero ='"+dc.getProducto().getIdProducto()+"'", null);
        Producto p = new Producto();
        if(c.moveToFirst())
        {
            p.setIdProducto(c.getString(0));
            p.setNombre(c.getString(1));
            p.setLinea(c.getString(2));
            p.setExistencia(c.getFloat(3));
            p.setPrecioCosto(c.getFloat(4));
            p.setPrecioPromedio(c.getFloat(5));
            p.setPrecioVenta1(c.getFloat(6));
            p.setPrecioVenta2(c.getFloat(7));
        }
        float existencias= p.getExistencia()-dc.getCantidad();
        db.execSQL("UPDATE producto SET "
                +"existencia= "+ existencias + " "
                +" WHERE numero='"+p.getIdProducto()+"';");

       // showMessage("Exito!", "Registro eliminado");
    }


    private void iniciarTabla(){
        TableRow fila = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        fila.setLayoutParams(lp);

        fila.setBackgroundResource(R.drawable.border_table);

        String[] titulos = {"Clave ", "Descripcion", "Unidad ", "Linea ", "Cantidad ", "Costo ", "Importe "};
        for (int x = 0; x < titulos.length; x++) {
            TextView tvTitulo = new TextView(this);
            tvTitulo.setTypeface(null, Typeface.BOLD);
            tvTitulo.setText(titulos[x]);
            fila.addView(tvTitulo);
        }
        // Finalmente agregar la fila en la primera posición
        tblProductos.addView(fila, 0);

    }

    private void  agregarDetallesTabla(ArrayList<DetalleCompra> detalles){
        destruirTabla();
        TableRow fila = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        fila.setLayoutParams(lp);

        fila.setBackgroundResource(R.drawable.border_table);

        String[] titulos = {"Editar","Eliminar","Clave ", "Descripcion", "Unidad ", "Linea ", "Cantidad ", "Costo ", "Importe "};
        for (int x = 0; x < titulos.length; x++) {
            TextView tvTitulo = new TextView(this);
            tvTitulo.setTypeface(null, Typeface.BOLD);
            tvTitulo.setText(titulos[x]);
            fila.addView(tvTitulo);
        }
        // Finalmente agregar la fila en la primera posición
        tblProductos.addView(fila, 0);

        int contF=1;

        for (int i=0; i<detalles.size();i++){
            TableRow filaDatos = new TableRow(this);
            filaDatos.setLayoutParams(lp);
            // Borde
            filaDatos.setBackgroundResource(R.drawable.border_table);
            String idDetalle=detalles.get(i).getIdDetalleCompra();
            DetalleCompra dc = detalles.get(i);
            int pos=contF;
            String[] datos = {"Editar |","Eliminar |",detalles.get(i).getIdDetalleCompra()+" |",detalles.get(i).getProducto().getNombre(),
                    " |  Par    | ",""+detalles.get(i).getProducto().getLinea()+
                    " |  "+detalles.get(i).getCantidad(), "  | $"+detalles.get(i).getCostoUnitario()," | $"+detalles.get(i).getImporte()};
            for (int x = 0; x < datos.length; x++) {
                TextView tvTitulo = new TextView(this);
                if(x == 0 ){
                    tvTitulo.setBackgroundColor(Color.parseColor("#0D6EFD"));
                    tvTitulo.setOnClickListener(v -> {
                        mostrarDetalle(dc,pos);
                    });
                }
                if(x == 1 ){
                    tvTitulo.setBackgroundColor(Color.parseColor("#DC3545"));
                    tvTitulo.setOnClickListener(v -> {
                        eliminarDetalle(dc,pos);
                    });
                }

                tvTitulo.setText(datos[x]);
                filaDatos.addView(tvTitulo);
            }
            // Finalmente agregar la fila en la primera posición
            tblProductos.addView(filaDatos, contF);
            contF++;

        }
    }

    private void añadirDetalle(){

        if(spnProductos.getSelectedItemPosition()==0 || txtCantidad.getText().toString().trim().length()==0){
            showMessage("Error","Debes elegir un producto válido o especificar una cantidad mayor a 0");
        }else{


                //Obtener el producto de la base de datos
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        String idProducto=spnProductos.getSelectedItem().toString().substring(0,2);
        Cursor c=db.rawQuery("SELECT * FROM producto WHERE numero ='"+idProducto+"'", null);
        Producto p = new Producto();
        if(c.moveToFirst())
        {
            p.setIdProducto(c.getString(0));
            p.setNombre(c.getString(1));
            p.setLinea(c.getString(2));
            p.setExistencia(c.getFloat(3));
            p.setPrecioCosto(c.getFloat(4));
            p.setPrecioPromedio(c.getFloat(5));
            p.setPrecioVenta1(c.getFloat(6));
            p.setPrecioVenta2(c.getFloat(7));
        }
        //agregar datos del detalle de compra
        DetalleCompra dc=new DetalleCompra();
        dc.setIdDetalleCompra(asignarClaveDC());
        dc.setProducto(p);
        dc.setCantidad(Float.parseFloat(txtCantidad.getText().toString().trim()));
        dc.setCostoUnitario(p.getPrecioCosto());
        dc.setImporte(dc.getCostoUnitario()*dc.getCantidad());
        detalles.add(dc);

        //Agregar la fila a la tabla
        TableRow filaDatos = new TableRow(this);
        filaDatos.setLayoutParams(lp);
        // Borde
        filaDatos.setBackgroundResource(R.drawable.border_table);

        String[] datos = {""+detalles.get(contDet).getIdDetalleCompra()+" |",detalles.get(contDet).getProducto().getNombre(),
                " |  Par    | ",""+detalles.get(contDet).getProducto().getLinea()+
                " |  "+detalles.get(contDet).getCantidad(), "  | $"+detalles.get(contDet).getCostoUnitario()," | $"+detalles.get(contDet).getImporte()};
        for (int x = 0; x < datos.length; x++) {
            TextView tvTitulo = new TextView(this);
            tvTitulo.setText(datos[x]);

            filaDatos.addView(tvTitulo);
        }
        // Finalmente agregar la fila en la primera posición
        tblProductos.addView(filaDatos, contFilas);

        contDet++;
        contFilas++;

        //sumamos las variables
            totalPrs+=dc.getCantidad();
            subtotal+=dc.getImporte();
            iva= (float) (subtotal*0.16);
            total=subtotal+iva;

            txtTotalPares.setText("Total prs: "+totalPrs);
            txtIva.setText("IVA: $"+iva);
            txtSubtotal.setText("Subtotal: $"+subtotal);
            txtTotal.setText("Total: $"+total);

            clearText();

        }
    }

    private void iniciarListas(){
        ArrayList<String> listaProveedores = new ArrayList<String>();
        ArrayList<String> listaProductos = new ArrayList<String>();

        Cursor c,c2,c3=null;
        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        c=db.rawQuery("SELECT * FROM proveedor", null);

        listaProveedores.add("Selecciona un proveedor");

        while(c.moveToNext())
        {
            listaProveedores.add(c.getString(0)+" "+c.getString(1));
            System.out.println("Proveedor: "+c.getString(1));
        }

        c2=db.rawQuery("SELECT * FROM producto", null);
        listaProductos.add("Selecciona un producto");

        while(c2.moveToNext())
        {
            listaProductos.add(c2.getString(0)+" "+c2.getString(1));
            System.out.println("Producto: "+c2.getString(1));
        }

        ArrayAdapter<String> adapterProv = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaProveedores);
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProveedores.setAdapter(adapterProv);

        ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaProductos);
        adapterProd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProductos.setAdapter(adapterProd);

    }


    private void mostrarDetalle(DetalleCompra dc,int pos){
        //showMessage("Info:","Vas a editar el detalle "+dc.getIdDetalleCompra()+" de la posicion "+pos);
        idDetalleActual=dc.getIdDetalleCompra();
        txtCantidad.setText(String.valueOf(dc.getCantidad()));
        spnProductos.setSelection(Integer.parseInt(dc.getProducto().getIdProducto().substring(1)));
        View child = tblProductos.getChildAt(pos);
        if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
    }

    private void actualizarDetalle(String idDetalle){

        if(spnProductos.getSelectedItemPosition()==0 || txtCantidad.getText().toString().trim().length()==0){
            showMessage("Error","Debes elegir un producto válido o especificar una cantidad mayor a 0");
        }else{
            Cursor c2=db.rawQuery("SELECT * FROM detalleCompra WHERE idCompra='"+txtNum.getText().toString().trim()+"'", null);
            detalles=new ArrayList<>();
            float cant1=0;
            float cant2=0;
            cant2=Float.parseFloat(txtCantidad.getText().toString());
            if(c2.moveToFirst())
            {
                DetalleCompra dc = new DetalleCompra();
                dc.setIdDetalleCompra(c2.getString(0));
                dc.setCantidad(Float.parseFloat(c2.getString(3)));
                dc.setCostoUnitario(Float.parseFloat(c2.getString(4)));
                dc.setImporte(Float.parseFloat(c2.getString(5)));
                cant1=dc.getCantidad();
                }

            String idProducto=spnProductos.getSelectedItem().toString().substring(0,2);
            Cursor c=db.rawQuery("SELECT * FROM producto WHERE numero ='"+idProducto+"'", null);
            Producto p = new Producto();
            if(c.moveToFirst())
            {
                p.setIdProducto(c.getString(0));
                p.setNombre(c.getString(1));
                p.setLinea(c.getString(2));
                p.setExistencia(c.getFloat(3));
                p.setPrecioCosto(c.getFloat(4));
                p.setPrecioPromedio(c.getFloat(5));
                p.setPrecioVenta1(c.getFloat(6));
                p.setPrecioVenta2(c.getFloat(7));
            }
            float costo=Float.parseFloat(String.valueOf(txtCantidad.getText()))*p.getPrecioCosto();
            db.execSQL("UPDATE detalleCompra SET "
                    +"idProducto= '"+idProducto+ "',"
                    +"cantidad= "+txtCantidad.getText()+ ","
                    +"costo= "+ p.getPrecioCosto()+ ","
                    +"importe= "+ costo + " "
                    +" WHERE idDetalleCompra='"+idDetalle+"';");

            float dif=cant2-cant1;
            float existencias=p.getExistencia()+dif;
            db.execSQL("UPDATE producto SET "
                    +"existencia= "+ existencias + " "
                    +" WHERE numero='"+p.getIdProducto()+"';");

            actualizarTotalesCompras();
            consultar();

        }

    }

    private void actualizarTotalesCompras(){
        Cursor c2=db.rawQuery("SELECT * FROM detalleCompra WHERE idCompra='"+txtNum.getText().toString().trim()+"'", null);
        detalles=new ArrayList<>();
        float totalCompra=0;
        float totalPares=0;
        while(c2.moveToNext())
        {
            DetalleCompra dc = new DetalleCompra();
            dc.setIdDetalleCompra(c2.getString(0));
            dc.setCantidad(Float.parseFloat(c2.getString(3)));
            dc.setCostoUnitario(Float.parseFloat(c2.getString(4)));
            dc.setImporte(Float.parseFloat(c2.getString(5)));

            Producto p= new Producto();
            Cursor cp=db.rawQuery("SELECT * FROM producto WHERE numero ='"+c2.getString(2)+"'", null);
            if(cp.moveToFirst()) {
                p.setIdProducto(cp.getString(0));
                p.setNombre(cp.getString(1));
                p.setLinea(cp.getString(2));
                p.setExistencia(Float.parseFloat(cp.getString(3)));
                p.setPrecioCosto(Float.parseFloat(cp.getString(4)));
                p.setPrecioPromedio(Float.parseFloat(cp.getString(5)));
                p.setPrecioVenta1(Float.parseFloat(cp.getString(6)));
                p.setPrecioVenta2(Float.parseFloat(cp.getString(7)));
            }
            dc.setProducto(p);
            detalles.add(dc);
        }

        for(int i=0;i<detalles.size();i++){
            totalCompra+=detalles.get(i).getImporte();
            totalPares+=detalles.get(i).getCantidad();
        }
        totalCompra=(float)(totalCompra*1.16);
        db.execSQL("UPDATE compra SET "
                +"totalPago= '"+totalCompra+ "',"
                +"totalPares= "+totalPares+ " "
                +" WHERE idCompra='"+txtNum.getText().toString().trim()+"';");

    }

    private void obtenerFecha(){
        Calendar c = Calendar.getInstance();
        String fecha=c.get(Calendar.DATE)+"-0"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);
        txtFecha.setText(fecha);
        txtFecha.setEnabled(false);

    }

    private String asignarClaveC(){
        String clave="";
        String inicial="C";
        Cursor c = db.rawQuery(" SELECT idCompra FROM Compra", null);
        int codigo=1;
        if(c.getCount()> 0){
            c.moveToLast();
            codigo=Integer.parseInt(c.getString(0).substring(1))+1;
        }
        clave=inicial+codigo;
        return clave;
    }

    private String asignarClaveDC(){
        String clave="";
        String inicial="DC";
        Cursor c = db.rawQuery(" SELECT idDetalleCompra FROM detalleCompra", null);
        int codigo=1;
        if(c.getCount()> 0){
            c.moveToLast();
            codigo=Integer.parseInt(c.getString(0).substring(2))+1;
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

    private void clearText(){
        txtNum.setText("");
        txtCantidad.setText("");
    }
}
