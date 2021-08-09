package org.rizosdb.utl.or_le_inventario;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import org.rizosdb.utl.or_le_inventario.db.ConexionSQLiteHelper;
import org.rizosdb.utl.or_le_inventario.iu_vendedor.VendedorActivity;
=======
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
>>>>>>> Stashed changes

public class MainActivity extends AppCompatActivity {
    ImageButton btncProveedores,
            btnProductos,btnClientes,
            btnProveedores,btnCompras,
            btnVentas;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< Updated upstream
        ImageButton btnVendedores = findViewById(R.id.btnVendedor);

        btnVendedores.setOnClickListener(v -> {
            Intent intent = new Intent(this, VendedorActivity.class);
            startActivity(intent);
        });


=======


        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        initButtons();

    }
    private void initButtons(){
        btnProveedores = (ImageButton) findViewById(R.id.btnProveedores);

        btnProveedores.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerProveedor.class);
            startActivity(intent);
        });
>>>>>>> Stashed changes
    }
}