package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DatosPedido {
    private String codigoPedido; // Se puede generar un código único para cada pedido

    private LocalDate fechaCreacion;

    private Estado estado;

    private Double montoTotal;

    private Double montoFinal;

    private LocalDate fechaEntrega;

    private Promocion promocionAplicada;

    private Usuario usuarioPedido;

    private Set<Producto> productos = new HashSet<>();

    public DatosPedido() {  }

    public DatosPedido(Usuario usuarioPedido) {
        this.fechaCreacion = LocalDate.now();
        this.estado = Estado.EN_ESPERA;
        this.usuarioPedido = usuarioPedido;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(Double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public Promocion getPromocionAplicada() {
        return promocionAplicada;
    }

    public void setPromocionAplicada(Promocion promocionAplicada) {
        this.promocionAplicada = promocionAplicada;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Usuario getUsuarioPedido() {
        return usuarioPedido;
    }

    public void setUsuarioPedido(Usuario usuarioPedido) {
        this.usuarioPedido = usuarioPedido;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }
}
