package org.rizosdb.utl.or_le_inventario.models;

public class Producto {
    private String idProducto;
    private String nombre;
    private String linea;
    private float existencia;
    private float precioCosto;
    private float precioPromedio;
    private float precioVenta1;
    private float precioVenta2;

    public Producto() {
    }

    public Producto(String idProducto, String nombre, String linea, float existencia,
                    float precioCosto, float precioPromedio, float precioVenta1, float precioVenta2) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.linea = linea;
        this.existencia = existencia;
        this.precioCosto = precioCosto;
        this.precioPromedio = precioPromedio;
        this.precioVenta1 = precioVenta1;
        this.precioVenta2 = precioVenta2;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public float getExistencia() {
        return existencia;
    }

    public void setExistencia(float existencia) {
        this.existencia = existencia;
    }

    public float getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(float precioCosto) {
        this.precioCosto = precioCosto;
    }

    public float getPrecioPromedio() {
        return precioPromedio;
    }

    public void setPrecioPromedio(float precioPromedio) {
        this.precioPromedio = precioPromedio;
    }

    public float getPrecioVenta1() {
        return precioVenta1;
    }

    public void setPrecioVenta1(float precioVenta1) {
        this.precioVenta1 = precioVenta1;
    }

    public float getPrecioVenta2() {
        return precioVenta2;
    }

    public void setPrecioVenta2(float precioVenta2) {
        this.precioVenta2 = precioVenta2;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto='" + idProducto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", linea='" + linea + '\'' +
                ", existencia=" + existencia +
                ", precioCosto=" + precioCosto +
                ", precioPromedio=" + precioPromedio +
                ", precioVenta1=" + precioVenta1 +
                ", precioVenta2=" + precioVenta2 +
                '}';
    }
}
