package org.rizosdb.utl.or_le_inventario;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import org.rizosdb.utl.or_le_inventario.controllers.ControllerCliente;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerCompras;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerGrafica;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerProducto;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerProveedor;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerReporte;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerVendedor;
import org.rizosdb.utl.or_le_inventario.controllers.ControllerVentas;


public class MainActivity extends AppCompatActivity {
    ImageButton
            btnProductos,btnClientes,
            btnProveedores,btnCompras,
            btnVentas,btnVendedores,
            btnGrafica,btnReportes;


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
        btnCompras = findViewById(R.id.btnCompra);

        btnGrafica = findViewById(R.id.btnGrafica);

        btnReportes = findViewById(R.id.btnReportes);


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

        btnCompras.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerCompras.class);
            startActivity(intent);
        });


        btnGrafica.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerGrafica.class);
            startActivity(intent);
        });
        btnReportes.setOnClickListener(v -> {
            Intent intent = new Intent(this, ControllerReporte.class);

            startActivity(intent);
        });


    }
}