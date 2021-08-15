package org.rizosdb.utl.or_le_inventario;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;


public class MainActivity extends AppCompatActivity {
    ImageButton
            btnProductos,btnClientes,
            btnProveedores,btnCompras,
            btnVentas,btnVendedores;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=openOrCreateDatabase("InventarioDB", Context.MODE_PRIVATE, null);
        initButtons();

    }
    private void initButtons(){
        btnProveedores = (ImageButton) findViewById(R.id.btnProveedores);
        btnProductos = (ImageButton) findViewById(R.id.btnProductos);
        btnVendedores = findViewById(R.id.btnVendedores);
        btnClientes = findViewById(R.id.btnClientes);
        btnVentas = findViewById(R.id.btnVenta);

        btnProveedores.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerProveedor.class);
            startActivity(intent);
        });

        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerProducto.class);
            startActivity(intent);
        });

        btnVendedores.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerVendedor.class);
            startActivity(intent);
        });

        btnClientes.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerCliente.class);
            startActivity(intent);
        });

        btnVentas.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerVentas.class);
            startActivity(intent);
        });

    }
}