package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoPedido; // Se puede generar un código único para cada pedido

    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Double montoTotal;

    private Double montoFinal;

    @Enumerated(EnumType.STRING)
    private Moneda monedaDePago;

    private LocalDate fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "promocion", nullable = true)
    private Promocion promocionAplicada;

    @ManyToOne
    private Usuario usuarioPedido;

    @ManyToMany
    private Set<Producto> productos = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    private String direccionEnvio;  // si aplica

    private String metodoPago; // crédito, débito...

    private Integer cuotas;

    private boolean pagoEnDolares;

    private Double cotizacionDolar;

    private Double costoEnvio;

    private String descripcionEnvio;

    private String tiempoEntrega;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getUsuarioPedido() {
        return usuarioPedido;
    }

    public void setUsuarioPedido(Usuario usuarioPedido) {
        this.usuarioPedido = usuarioPedido;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
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

    public Moneda getMonedaDePago() {
        return monedaDePago;
    }

    public void setMonedaDePago(Moneda monedaDePago) {
        this.monedaDePago = monedaDePago;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Promocion getPromocionAplicada() {
        return promocionAplicada;
    }

    public void setPromocionAplicada(Promocion promocionAplicada) {
        this.promocionAplicada = promocionAplicada;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) { this.productos = productos; }

    public TipoEnvio getTipoEnvio() { return tipoEnvio; }
    public void setTipoEnvio(TipoEnvio tipoEnvio) { this.tipoEnvio = tipoEnvio; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getCuotas() { return cuotas; }
    public void setCuotas(Integer cuotas) { this.cuotas = cuotas; }

    public boolean isPagoEnDolares() { return pagoEnDolares; }
    public void setPagoEnDolares(boolean pagoEnDolares) { this.pagoEnDolares = pagoEnDolares; }

    public Double getCotizacionDolar() { return cotizacionDolar; }
    public void setCotizacionDolar(Double cotizacionDolar) { this.cotizacionDolar = cotizacionDolar; }

    public Double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }

    public String getDescripcionEnvio() { return descripcionEnvio; }
    public void setDescripcionEnvio(String descripcionEnvio) { this.descripcionEnvio = descripcionEnvio; }

    public String getTiempoEntrega() { return tiempoEntrega; }
    public void setTiempoEntrega(String tiempoEntrega) { this.tiempoEntrega = tiempoEntrega; }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id) && Objects.equals(codigoPedido, pedido.codigoPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigoPedido);
    }
}