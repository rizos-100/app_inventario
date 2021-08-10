package org.rizosdb.utl.or_le_inventario.iu_vendedor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.rizosdb.utl.or_le_inventario.R;
import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;

public class VendedorActivity extends AppCompatActivity {
    ConexionSQLiteHelper sqLiteHelper ;
    EditText txtNombre,txtCalle,txtColonia,txtTelefono, txtCorreo ,txtComision,txtID,txtID_persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        sqLiteHelper = new ConexionSQLiteHelper(this,Utilidades.NOMBRE_DB,null, Utilidades.VERSION_DB);

        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnConsultarTodo = findViewById(R.id.btnLista);
        Button btnConsultar = findViewById(R.id.btnConsultar);
        Button btnModificar = findViewById(R.id.btnModificar);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        txtNombre = findViewById(R.id.txtNombre);
        txtCalle = findViewById(R.id.txtCalle);
        txtColonia = findViewById(R.id.txtColonia);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtComision = findViewById(R.id.txtComision);
        txtID = findViewById(R.id.txtID);
        txtID_persona= findViewById(R.id.txtID_persona);

        btnAgregar.setOnClickListener(v -> {
            agregarVendedor();
        });
        btnConsultarTodo.setOnClickListener(v -> {
            consultarTodo();
        });
        btnConsultar.setOnClickListener(v -> {
            consultarPorId(txtID.getText().toString());
        });
        btnModificar.setOnClickListener(v -> {
            modificarVendedor();
        });
        btnEliminar.setOnClickListener(v -> {
            eliminarVendedor();
        });
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void agregarVendedor(){
        if(validarTexto()){
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
            db.beginTransaction();
            try {


                ContentValues values = new ContentValues();
                values.put(Utilidades.TABLA_PERSONA_NOMBRE, txtNombre.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_CALLE, txtCalle.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_COLONIA, txtColonia.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_TELEFONO, txtTelefono.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_EMAIL, txtCorreo.getText().toString());

                Long idPersona = db.insert(Utilidades.TABLA_PERSONAS, Utilidades.TABLA_PERSONA_ID, values);

                ContentValues valuesFore = new ContentValues();
                valuesFore.put(Utilidades.TABLA_VENDEDOR_COMISION, txtComision.getText().toString());
                valuesFore.put(Utilidades.TABLA_VENDEDOR_ID_PERSONA, idPersona);
                Long idVendedor = db.insert(Utilidades.TABLA_VENDEDORES, Utilidades.TABLA_VENDEDOR_ID, valuesFore);
                db.setTransactionSuccessful();
                if(idVendedor > 0)
                    showMessage("Vendedor(a) registrodo(a) con exito","Se ingreso con el ID: "+idVendedor);
                else
                    showMessage("Vendedor(a) registrodo(a) falló","Fallo el registro del vendedor(as)");

                clearText();
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

    public void consultarTodo(){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            String sql = "SELECT * FROM " + Utilidades.TABLA_PERSONAS+" p INNER JOIN "+Utilidades.TABLA_VENDEDORES+
                    " v on p."+Utilidades.TABLA_PERSONA_ID+" = v."+Utilidades.TABLA_VENDEDOR_ID_PERSONA;
            Cursor c = db.rawQuery(sql, null);
            if (c.getCount() == 0) {
                showMessage("Error!", "No se encontraron registros");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("ID Vendedor: " + c.getString(6) + "\n");
                buffer.append("Nombre: " + c.getString(1) + "\n");
                buffer.append("Calle: " + c.getString(2) + "\n");
                buffer.append("Colonia: " + c.getString(3) + "\n");
                buffer.append("Telefono: " + c.getString(4) + "\n");
                buffer.append("Correo: " + c.getString(5) + "\n");
                buffer.append("Comisión: $" + c.getString(8) + "\n\n\n");

            }
            showMessage("Registros de Vendedores", buffer.toString());
        } catch (Exception e){
        e.printStackTrace();
        }finally {

            db.close();
        }
    }

    public void consultarPorId(String id){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            String sql = "SELECT * FROM " + Utilidades.TABLA_PERSONAS+" p INNER JOIN "+Utilidades.TABLA_VENDEDORES+
                    " v on p."+Utilidades.TABLA_PERSONA_ID+" = v."+Utilidades.TABLA_VENDEDOR_ID_PERSONA+
                    " WHERE v."+Utilidades.TABLA_VENDEDOR_ID+ " = "+id;
            Cursor c = db.rawQuery(sql, null);
            if (!c.moveToFirst()) {
                showMessage("Error!", "No se encontraron registros");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            if (c.moveToFirst()) {
                txtNombre.setText(c.getString(1));
                txtCalle.setText(c.getString(2));
                txtColonia.setText(c.getString(3));
                txtTelefono.setText(c.getString(4));
                txtCorreo.setText(c.getString(5));
                txtComision.setText(c.getString(8));
                txtID.setText(c.getString(0));
                txtID_persona.setText(c.getString(7));

            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {

            db.close();
        }
    }

    public void modificarVendedor(){
        if(validarTexto()){
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
            db.beginTransaction();
            try {

                ContentValues values = new ContentValues();
                values.put(Utilidades.TABLA_PERSONA_NOMBRE, txtNombre.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_CALLE, txtCalle.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_COLONIA, txtColonia.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_TELEFONO, txtTelefono.getText().toString());
                values.put(Utilidades.TABLA_PERSONA_EMAIL, txtCorreo.getText().toString());

                String[] args = new String[]{txtID_persona.getText().toString()};
                int idPersona = db.update(Utilidades.TABLA_PERSONAS,values, Utilidades.TABLA_PERSONA_ID+"=?", args);

                ContentValues valuesFore = new ContentValues();
                valuesFore.put(Utilidades.TABLA_VENDEDOR_COMISION, txtComision.getText().toString());
                String[] argsVend = new String[]{txtID.getText().toString()};
                int idVendedor = db.update(Utilidades.TABLA_VENDEDORES,valuesFore, Utilidades.TABLA_VENDEDOR_ID_PERSONA+"=?", argsVend);
                db.setTransactionSuccessful();
                if(idVendedor > 0 && idPersona > 0)
                    showMessage("Vendedor(a) actualizado(a) con exito","Se actualizo el vendedor(a): "+txtNombre.getText().toString());
                else
                    showMessage("Vendedor(a) actualizado(a) falló","Fallo la actualizació del vendedor(as)");

                clearText();
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

    public void eliminarVendedor(){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            String[] args = new String[]{txtID.getText().toString()};
            int idVendedor = db.delete(Utilidades.TABLA_VENDEDORES, Utilidades.TABLA_VENDEDOR_ID+"=?",args);

            if(idVendedor > 0) {
                String[] argsPer = new String[]{txtID_persona.getText().toString()};
                int idPersona = db.delete(Utilidades.TABLA_PERSONAS, Utilidades.TABLA_PERSONA_ID+"=?",argsPer);
                if(idPersona > 0) {
                    db.setTransactionSuccessful();
                    showMessage("Vendedor(a) eliminado(a) con exito", "Se elimino el vendedor(a): " + txtNombre.getText().toString());
                }
                }else
                showMessage("Vendedor(a) eliminado(a) falló","Fallo la eliminación del vendedor(a)");

            db.close();
            clearText();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("info",e.toString());

        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public void clearText()
    {
        txtNombre.setText("");
        txtCalle.setText("");
        txtColonia.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtComision.setText("");

    }

    public boolean validarTexto(){
        if(txtNombre.getText().toString().trim().length() > 0 &&
                txtCalle.getText().toString().trim().length() > 0 &&
                txtColonia.getText().toString().trim().length() > 0 &&
                txtTelefono.getText().toString().trim().length() > 0 &&
                txtCorreo.getText().toString().trim().length() > 0 &&
                txtCorreo.getText().toString().trim().length() > 0)
            return true;
        else
            return false;

    }
}