package org.rizosdb.utl.or_le_inventario.db;

public class Utilidades {

    public static final int VERSION_DB = 1;
    public static final String NOMBRE_DB = "db-inventario";

    public static final String TABLA_VENDEDORES = "vendedor";
    public static final String TABLA_CLIENTES = "cliente";
    public static final String TABLA_PERSONAS = "persona";


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


}
