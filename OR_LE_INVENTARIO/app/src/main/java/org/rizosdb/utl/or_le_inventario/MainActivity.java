package org.rizosdb.utl.or_le_inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.iu_vendedor.VendedorActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnVendedores = findViewById(R.id.btnVendedor);

        btnVendedores.setOnClickListener(v -> {
            Intent intent = new Intent(this, VendedorActivity.class);
            startActivity(intent);
        });


    }
}