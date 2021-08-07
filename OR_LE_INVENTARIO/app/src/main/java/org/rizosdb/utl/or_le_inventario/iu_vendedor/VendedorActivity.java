package org.rizosdb.utl.or_le_inventario.iu_vendedor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.rizosdb.utl.or_le_inventario.R;
import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.db.Utilidades;

public class VendedorActivity extends AppCompatActivity {
    ConexionSQLiteHelper sqLiteHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        sqLiteHelper = new ConexionSQLiteHelper(this,Utilidades.NOMBRE_DB,null, Utilidades.VERSION_DB);

        Button btnAgregar = findViewById(R.id.btnAgregar);
        EditText txt1 = findViewById(R.id.txt1);
        EditText txt2 = findViewById(R.id.txt2);
        btnAgregar.setOnClickListener(v -> {
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
        });
    }
}