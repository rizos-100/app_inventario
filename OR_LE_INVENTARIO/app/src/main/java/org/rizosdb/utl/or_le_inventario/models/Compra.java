package org.rizosdb.utl.or_le_inventario.models;

import java.util.ArrayList;

public class Compra {
    private String idCompra;
    private Proveedor proveedor;
    private String fecha;
    private ArrayList<DetalleCompra> detallesCompra;
    private float totalPago;
    private float totalPares;

    public Compra() {
    }

    public Compra(String idCompra, Proveedor proveedor,
                  String fecha, ArrayList<DetalleCompra> detallesCompra,
                  float totalPago, float totalPares) {
        this.idCompra = idCompra;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.detallesCompra = detallesCompra;
        this.totalPago = totalPago;
        this.totalPares = totalPares;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<DetalleCompra> getDetallesCompra() {
        return detallesCompra;
    }

    public void setDetallesCompra(ArrayList<DetalleCompra> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }

    public float getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(float totalPago) {
        this.totalPago = totalPago;
    }

    public float getTotalPares() {
        return totalPares;
    }

    public void setTotalPares(float totalPares) {
        this.totalPares = totalPares;
    }
}
