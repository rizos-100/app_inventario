package org.rizosdb.utl.or_le_inventario.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_PERSONA);
        db.execSQL(Utilidades.CREAR_TABLA_VENDEDOR);
        db.execSQL(Utilidades.CREAR_TABLA_CLIENTE);
        db.execSQL(Utilidades.CREAR_TABLA_COMPRA);
        db.execSQL(Utilidades.CREAR_TABLA_DETALLE_COMPRA);
        db.execSQL(Utilidades.CREAR_TABLA_VENTAS);
        db.execSQL(Utilidades.CREAR_TABLA_DETALLE_VENTAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionVieja, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_VENDEDORES);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_PERSONAS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_DETALLE_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_VENTAS);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_DETALLE_VENTAS);
        onCreate(db);
    }
}
