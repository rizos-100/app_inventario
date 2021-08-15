package org.rizosdb.utl.or_le_inventario.models;

public class Proveedor {
    private String idProveedor;
    private String nombre;
    private String calle;
    private String colonia;
    private String ciudad;
    private String rfc;
    private String telefono;
    private String email;
    private float saldo;

    public Proveedor() {
    }

    public Proveedor(String idProveedor, String nombre, String calle, String colonia,
                     String ciudad, String rfc, String telefono, String email, float saldo) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.rfc = rfc;
        this.telefono = telefono;
        this.email = email;
        this.saldo = saldo;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
}
