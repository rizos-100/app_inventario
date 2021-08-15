package org.rizosdb.utl.or_le_inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;
import org.rizosdb.utl.or_le_inventario.model.Cliente;

import java.util.ArrayList;

public class ControllerVentas extends AppCompatActivity {
    TextView txtCliente;
    ArrayList<Cliente> arrayClientes;
    ConexionSQLiteHelper sqLiteHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        sqLiteHelper = new ConexionSQLiteHelper(this,Utilidades.NOMBRE_DB,null, Utilidades.VERSION_DB);

        txtCliente = findViewById(R.id.txtCliente);
        arrayClientes = consultarClientes();


        txtCliente.setOnClickListener(v -> {
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

                    dialog.dismiss();
                }
            });
        });

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
}