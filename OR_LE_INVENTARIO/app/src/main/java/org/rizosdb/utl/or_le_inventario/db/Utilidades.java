package org.rizosdb.utl.or_le_inventario.db;

public class Utilidades {

    public static final int VERSION_DB = 1;
    public static final String NOMBRE_DB = "InventarioDB";

    public static final String TABLA_VENDEDORES = "vendedor";
    public static final String TABLA_CLIENTES = "cliente";
    public static final String TABLA_PERSONAS = "persona";
    public static final String TABLA_COMPRAS = "compra";
    public static final String TABLA_DETALLE_COMPRAS = "detalleCompra";


    public static final String TABLA_PERSONA_ID = "id";
    public static final String TABLA_PERSONA_NOMBRE = "nombre";
    public static final String TABLA_PERSONA_CALLE = "calle";
    public static final String TABLA_PERSONA_COLONIA = "colonia";
    public static final String TABLA_PERSONA_TELEFONO = "telefono";
    public static final String TABLA_PERSONA_EMAIL = "correo";

    public static final String TABLA_VENDEDOR_ID = "id";
    public static final String TABLA_VENDEDOR_ID_PERSONA = "idPersona";
    public static final String TABLA_VENDEDOR_COMISION = "comision";



    public static final String TABLA_CLIENTES_ID = "id";
    public static final String TABLA_CLIENTES_ID_PERSONA = "idPersona";
    public static final String TABLA_CLIENTES_CIUDAD = "ciudad";
    public static final String TABLA_CLIENTES_RFC = "rfc";
    public static final String TABLA_CLIENTES_SALDO = "saldo";

    public static final String TABLA_COMPRA_ID = "idCompra";
    public static final String TABLA_COMPRA_IDPROVEEDOR = "idProveedor";
    public static final String TABLA_COMPRA_FECHA = "idCompra";
    public static final String TABLA_COMPRA_TOTALPAGO = "totalPago";
    public static final String TABLA_COMPRA_TOTALPARES = "totalPares";

    public static final String TABLA_DETALLE_COMPRA_ID = "idDetalleCompra";
    public static final String TABLA_DETALLE_IDCOMPRA = "idCompra";
    public static final String TABLA_DETALLE_IDPRODUCTO = "idProducto";
    public static final String TABLA_DETALLE_CANTIDAD = "cantidad";
    public static final String TABLA_DETALLE_COSTO = "costoUnitario";
    public static final String TABLA_DETALLE_IMPORTE = "importe";






    public static final String  CREAR_TABLA_PERSONA="CREATE TABLE "+TABLA_PERSONAS
            +" ("+TABLA_PERSONA_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLA_PERSONA_NOMBRE+" TEXT, "
            +TABLA_PERSONA_CALLE+" TEXT, "
            +TABLA_PERSONA_COLONIA+" TEXT, "
            +TABLA_PERSONA_TELEFONO+" TEXT, "
            +TABLA_PERSONA_EMAIL+" TEXT)";

    public static final String  CREAR_TABLA_VENDEDOR = "CREATE TABLE "+TABLA_VENDEDORES
            +" ("+TABLA_VENDEDOR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLA_VENDEDOR_ID_PERSONA+" INTEGER, "
            +TABLA_VENDEDOR_COMISION+" REAL, "+
            "CONSTRAINT fk_vendedor_persona " +
            "    FOREIGN KEY ("+TABLA_VENDEDOR_ID_PERSONA+") REFERENCES "+TABLA_PERSONAS+"("+TABLA_PERSONA_ID+")"+
            ")";

    public static final String  CREAR_TABLA_CLIENTE="CREATE TABLE "+TABLA_CLIENTES
            +" ("+TABLA_CLIENTES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TABLA_CLIENTES_ID_PERSONA+" INTEGER, "
            +TABLA_CLIENTES_CIUDAD+" TEXT, "
            +TABLA_CLIENTES_RFC+" TEXT, "
            +TABLA_CLIENTES_SALDO+" REAL, "+
            "CONSTRAINT fk_cliente_persona " +
            "    FOREIGN KEY ("+TABLA_CLIENTES_ID_PERSONA+") REFERENCES "+TABLA_PERSONAS+"("+TABLA_PERSONA_ID+")"+
            ")";

    public static final String CREAR_TABLA_COMPRA="CREATE TABLE "+TABLA_COMPRAS
            +" ("+TABLA_COMPRA_ID+" TEXT PRIMARY KEY, "
            +TABLA_COMPRA_IDPROVEEDOR+" TEXT, "
            +TABLA_COMPRA_FECHA+" TEXT, "
            +TABLA_COMPRA_TOTALPAGO+" REAL, "
            +TABLA_COMPRA_TOTALPARES+" REAL, "
            +"CONSTRAINT fk_compra_proveedor " +
            "    FOREIGN KEY ("+TABLA_COMPRA_IDPROVEEDOR+") REFERENCES proveedor(numero)"+
            ")";

    public static final String CREAR_TABLA_DETALLE_COMPRA="CREATE TABLE "+TABLA_DETALLE_COMPRAS
            +" ("+TABLA_DETALLE_COMPRA_ID+" TEXT PRIMARY KEY, "
            +TABLA_DETALLE_IDCOMPRA+" TEXT, "
            +TABLA_DETALLE_IDPRODUCTO+" TEXT, "
            +TABLA_DETALLE_CANTIDAD+" REAL, "
            +TABLA_DETALLE_COSTO+" REAL, "
            +TABLA_DETALLE_IMPORTE+" REAL, "
            +"CONSTRAINT fk_detalle_compra " +
            "    FOREIGN KEY ("+TABLA_DETALLE_IDCOMPRA+") REFERENCES compra(idCompra),"+
            "CONSTRAINT fk_detalle_producto " +
            "    FOREIGN KEY ("+TABLA_DETALLE_IDPRODUCTO+") REFERENCES producto(numero)"+
            ")";

}
