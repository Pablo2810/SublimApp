package com.tallerwebi.dominio.entidad;

import java.util.Date;

public class Pedido {
    private Long id;
    private Date fechaCreacion;
    private Estado estado;
    private Integer cantCopias;
    private Double metrosTotales;
    private Double costoServicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getCantCopias() {
        return cantCopias;
    }

    public void setCantCopias(Integer cantCopias) {
        this.cantCopias = cantCopias;
    }

    public Double getMetrosTotales() {
        return metrosTotales;
    }

    public void setMetrosTotales(Double metrosTotales) {
        this.metrosTotales = metrosTotales;
    }

    public Double getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(Double costoServicio) {
        this.costoServicio = costoServicio;
    }
}
