package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Entity
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;

    private String descripcion;

    private Integer cantidadUsuariosVisto;

    @ManyToMany(mappedBy = "promocionesAceptadas")
    private HashSet<Usuario> usuariosAceptaron = new HashSet<>();

    private Double descuento;

    @OneToMany(mappedBy = "promocion")
    private List<Pedido> pedidos = new ArrayList<>();

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadUsuariosVisto() {
        return cantidadUsuariosVisto;
    }

    public void setCantidadUsuariosVisto(Integer cantidadUsuariosVisto) {
        this.cantidadUsuariosVisto = cantidadUsuariosVisto;
    }

    public void agregarUsuarioAceptado(Usuario usuario) {
        this.usuariosAceptaron.add(usuario);
        usuario.getPromocionesAceptadas().add(this);
    }

    public HashSet<Usuario> getUsuariosAceptaron() {
        return usuariosAceptaron;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setPromocionAplicada(this);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Promocion promocion = (Promocion) o;
        return Objects.equals(idPromocion, promocion.idPromocion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPromocion);
    }
}
