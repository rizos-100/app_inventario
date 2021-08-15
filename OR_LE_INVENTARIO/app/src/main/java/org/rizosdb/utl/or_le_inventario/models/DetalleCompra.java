package org.rizosdb.utl.or_le_inventario.models;

public class DetalleCompra {
    private String idDetalleCompra;
    private Producto producto;
    private float cantidad;
    private float costoUnitario;
    private float importe;

    public DetalleCompra() {
    }

    public DetalleCompra(String idDetalleCompra, Producto producto, float cantidad,
                         float costoUnitario, float importe) {
        this.idDetalleCompra = idDetalleCompra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.importe = importe;
    }

    public String getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(String idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(float costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }
}
