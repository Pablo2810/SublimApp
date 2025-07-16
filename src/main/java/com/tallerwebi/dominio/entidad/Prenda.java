package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @ManyToMany
    @JoinTable(name="prenda_tela")
    private List<Tela> telas = new ArrayList<>();

    @OneToMany(mappedBy = "prenda")
    private List<Talle> talles = new ArrayList<>();

    @OneToMany(mappedBy = "prenda")
    private List<Producto> productos = new ArrayList<>();

    private Double precioBase;

    private String imagenUrl;

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public void agregarTela(Tela tela) {
        this.telas.add(tela);
        tela.getPrendas().add(this);
    }

    public List<Tela> getTelas() {
        return telas;
    }

    public void agregarTalle(Talle talle) {
        this.talles.add(talle);
        talle.setPrenda(this);
    }

    public List<Talle> getTalles() {
        return talles;
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
        producto.setPrenda(this);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTelas(List<Tela> telas) {
        this.telas = telas;
    }

    public void setTalles(List<Talle> talles) {
        this.talles = talles;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}