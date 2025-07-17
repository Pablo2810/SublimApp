package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "archivo")
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "prenda")
    private Prenda prenda;

    private Integer cantidad;

    @ManyToMany(mappedBy = "productos")
    private Set<Pedido> pedidos = new HashSet<>();

    private Double precio;

    @ManyToOne
    @JoinColumn(name = "tela")
    private Tela tela;

    @ManyToOne
    @JoinColumn(name = "talle")
    private Talle talle;

    private String imagenUrl;

    private String imagenPrendaConDisenioUrl;

    private Double precioConvertido;

    public Double getPrecioConvertido() { return precioConvertido; }

    public void setPrecioConvertido(Double precioConvertido) { this.precioConvertido = precioConvertido; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(HashSet<Pedido> pedidos) {
        this.pedidos = pedidos;
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

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getImagenPrendaConDisenioUrl() {
        return imagenPrendaConDisenioUrl;
    }

    public void setImagenPrendaConDisenioUrl(String imagenPrendaConDisenioUrl) {
        this.imagenPrendaConDisenioUrl = imagenPrendaConDisenioUrl;
    }

    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.getProductos().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        if (this.id == null || producto.id == null) {
            // Si alguno no tiene id, no son iguales (es instancia nueva)
            return false;
        }
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : System.identityHashCode(this);
    }

}