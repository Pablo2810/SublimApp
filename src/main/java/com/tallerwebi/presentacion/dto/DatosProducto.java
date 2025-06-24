package com.tallerwebi.presentacion.dto;

import org.springframework.web.multipart.MultipartFile;

public class DatosProducto {
    private Long prendaId;
    private Long telaId;
    private Long talleId;
    private Integer cantidad;
    private MultipartFile archivo;
    private Double precio;

    public Long getPrendaId() { return prendaId; }

    public void setPrendaId(Long prendaId) { this.prendaId = prendaId; }

    public Long getTelaId() { return telaId; }

    public void setTelaId(Long telaId) { this.telaId = telaId; }

    public Long getTalleId() { return talleId; }

    public void setTalleId(Long talleId) { this.talleId = talleId; }

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public MultipartFile getArchivo() { return archivo; }

    public void setArchivo(MultipartFile archivo) { this.archivo = archivo; }

    public Double getPrecio() { return precio; }

    public void setPrecio(Double precio) { this.precio = precio; }

    /*public Archivo getArchivo() {
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
    }*/
}
