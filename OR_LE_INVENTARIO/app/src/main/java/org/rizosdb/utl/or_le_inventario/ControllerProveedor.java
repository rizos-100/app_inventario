package org.rizosdb.utl.or_le_inventario;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ControllerProveedor extends AppCompatActivity {
    private EditText txtNum,txtNombre,txtCalle,txtColonia,
            txtCiudad,txtRFC,txtTel,txtEmail,txtSaldo;
    private Button btnAgregar,btnEliminar,btnConsultar,btnModifcar,btnLista;

     SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);
        iniciarComponentes();
    }

    private void iniciarComponentes(){

        txtNum=(EditText)findViewById(R.id.txtID);
        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtCalle=(EditText)findViewById(R.id.txtCalle);
        txtColonia=(EditText)findViewById(R.id.txtColonia);
        txtCiudad=(EditText)findViewById(R.id.txtTelefono);
        txtRFC=(EditText)findViewById(R.id.txtCorreo);
        txtTel=(EditText)findViewById(R.id.txtSaldo_cliente);
        txtEmail=(EditText)findViewById(R.id.txtEmail_);
        txtSaldo=(EditText)findViewById(R.id.txtSaldo);


        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);
        btnConsultar=(Button)findViewById(R.id.btnConsultar);
        btnModifcar=(Button)findViewById(R.id.btnModificar);
        btnLista=(Button)findViewById(R.id.btnLista);

        //txtNum.setEnabled(false);

        btnAgregar.setOnClickListener(v -> {
            agregarProveedor();
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
            modificar();
        });


        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS proveedor(numero VARCHAR,nombre VARCHAR,calle VARCHAR," +
                "colonia VARCHAR, ciudad VARCHAR,RFC VARCHAR,telefono VARCHAR,email VARCHAR,saldo FLOAT);");


    }

    private void agregarProveedor(){
        if(
                txtNombre.getText().toString().trim().length()==0||
                txtCalle.getText().toString().trim().length()==0||
                txtColonia.getText().toString().trim().length()==0||
                txtCiudad.getText().toString().trim().length()==0 ||
                txtRFC.getText().toString().trim().length()==0 ||
                txtTel.getText().toString().trim().length()==0 ||
                txtEmail.getText().toString().trim().length()==0
        )
        {
            showMessage("Error!", "Porfavor introduce todos los valores");
            return;
        }
            db.execSQL("INSERT INTO proveedor VALUES('" + asignarClave() + "','"
                    + txtNombre.getText().toString().trim() + "','"
                    + txtCalle.getText().toString().trim() + "','"
                    + txtColonia.getText().toString().trim() + "','"
                    + txtCiudad.getText().toString().trim() + "','"
                    + txtRFC.getText().toString().trim() + "','"
                    + txtTel.getText().toString().trim() + "','"
                    + txtEmail.getText().toString().trim() + "',"
                    + 0.0+");");
            showMessage("Exito!", "Registro agregado");
            clearText();

    }

    private void verTodos(){
        Cursor c=db.rawQuery("SELECT * FROM proveedor", null);
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
            buffer.append("Calle.: " + c.getString(2)+"\n");
            buffer.append("Colonia.: " + c.getString(3)+"\n");
            buffer.append("Ciudad.: " + c.getString(4)+"\n");
            buffer.append("RFC.: " + c.getString(5)+"\n");
            buffer.append("Telefono.: " + c.getString(6)+"\n");
            buffer.append("Email.: " + c.getString(7)+"\n");
            buffer.append("Saldo.: " + c.getString(8)+"\n\n");
        }
        showMessage("Registros", buffer.toString());
    }

    public void consultar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }
        Cursor c=db.rawQuery("SELECT * FROM proveedor WHERE numero ='"+txtNum.getText().toString().trim()+"'", null);
        if(c.moveToFirst())
        {
            txtNombre.setText(c.getString(1));
            txtCalle.setText(c.getString(2));
            txtColonia.setText(c.getString(3));
            txtCiudad.setText(c.getString(4));
            txtRFC.setText(c.getString(5));
            txtTel.setText(c.getString(6));
            txtEmail.setText(c.getString(7));
            txtSaldo.setText(c.getString(8));

            txtNum.setEnabled(false);
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
        db.execSQL("UPDATE proveedor SET "
                +"nombre= '"+ txtNombre.getText().toString().trim() + "',"
                +"calle= '"+ txtCalle.getText().toString().trim() + "',"
                +"colonia= '"+ txtColonia.getText().toString().trim() + "',"
                +"ciudad= '"+ txtCiudad.getText().toString().trim() + "',"
                +"RFC= '"+ txtRFC.getText().toString().trim() + "',"
                +"telefono= '"+ txtTel.getText().toString().trim() + "',"
                +"email= '"+ txtEmail.getText().toString().trim() + "',"
                +"saldo= "+ Float.parseFloat(txtSaldo.getText().toString().trim()) +""
                +" WHERE numero='"+txtNum.getText()+"';");
        showMessage("Exito!", "Registro actualizado");
        txtNum.setEnabled(true);
        clearText();
    }

    private void eliminar(){
        if(txtNum.getText().toString().trim().length()==0)
        {
            showMessage("Error!", "Introduce Clave");
            return;
        }
        db.execSQL("DELETE FROM proveedor WHERE numero='"+txtNum.getText().toString().trim()+"';");
        showMessage("Exito!", "Registro eliminado");
        txtNum.setEnabled(true);
        clearText();
    }

    private String asignarClave(){
        String clave="";
        String inicial="P";
        Cursor c = db.rawQuery(" SELECT numero FROM proveedor", null);
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
        txtCalle.setText("");
        txtColonia.setText("");
        txtCiudad.setText("");
        txtRFC.setText("");
        txtTel.setText("");
        txtEmail.setText("");
        txtSaldo.setText("");
    }
}
