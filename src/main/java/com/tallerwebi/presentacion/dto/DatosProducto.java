package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.*;

import java.util.UUID;

public class DatosProducto {
    private Archivo archivo;
    private Prenda prenda;
    private Integer cantidad;
    private Double precio;
    private Talle talle;
    private Tela tela;

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public Prenda getPrenda() {
        return prenda;
    }

    public void setPrenda(Prenda prenda) {
        this.prenda = prenda;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void calcularPrecio() {
        this.precio = prenda.getPrecioBase() + (talle.getMetrosTotales() * tela.getPrecio());
    }

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
    }

    public Tela getTela() {
        return tela;
    }

    public void setTela(Tela tela) {
        this.tela = tela;
    }

    public Producto generarProducto(Pedido pedido) {
        Producto producto = new Producto();

        producto.setCantidad(cantidad);
        producto.setArchivo(archivo);
        producto.setPrenda(prenda);
        producto.setTalle(talle);
        producto.setTela(tela);
        producto.agregarPedido(pedido);

        return producto;
    }

    public String generarNombre() {
        return UUID.randomUUID() + "-" + prenda.getDescripcion() + "-" + talle.getDescripcion();
    }
}
