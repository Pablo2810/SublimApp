package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CompraTela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double metros;
    private Double precioUnitario;
    private Double totalPagado;

    private String metodoPago;
    private Integer cuotas;
    private boolean pagoEnDolares;
    private Double cotizacionDolar;

    private String descripcionEnvio;
    private Double costoEnvio;
    private String tiempoEntrega;

    private LocalDateTime fechaCompra;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Tela tela;

    @PrePersist
    public void prePersist() {
        this.fechaCompra = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private EstadoTela estado = EstadoTela.EN_DEPOSITO;

    // Getters y Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Double getMetros() { return metros; }

    public void setMetros(Double metros) { this.metros = metros; }

    public Double getPrecioUnitario() { return precioUnitario; }

    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getTotalPagado() { return totalPagado; }

    public void setTotalPagado(Double totalPagado) { this.totalPagado = totalPagado; }

    public String getMetodoPago() { return metodoPago; }

    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getCuotas() { return cuotas; }

    public void setCuotas(Integer cuotas) { this.cuotas = cuotas; }

    public boolean isPagoEnDolares() { return pagoEnDolares; }

    public void setPagoEnDolares(boolean pagoEnDolares) { this.pagoEnDolares = pagoEnDolares; }

    public Double getCotizacionDolar() { return cotizacionDolar; }

    public void setCotizacionDolar(Double cotizacionDolar) { this.cotizacionDolar = cotizacionDolar; }

    public String getDescripcionEnvio() { return descripcionEnvio; }

    public void setDescripcionEnvio(String descripcionEnvio) { this.descripcionEnvio = descripcionEnvio; }

    public Double getCostoEnvio() { return costoEnvio; }

    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }

    public String getTiempoEntrega() { return tiempoEntrega; }

    public void setTiempoEntrega(String tiempoEntrega) { this.tiempoEntrega = tiempoEntrega; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }

    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Tela getTela() { return tela; }

    public void setTela(Tela tela) { this.tela = tela; }

    public EstadoTela getEstado() { return estado; }

    public void setEstado(EstadoTela estado) { this.estado = estado; }

}