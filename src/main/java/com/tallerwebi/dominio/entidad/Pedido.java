package com.tallerwebi.dominio.entidad;

import java.time.LocalDate;
import java.util.Date;

public class Pedido {
    private Long id;
    private LocalDate fechaCreacion;
    private Estado estado;
    private Integer cantCopias;
    private Double ancho;
    private Double alto;
    private String metrosTotales;
    private Double costoServicio;
    private Archivo archivo;

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

    public Integer getCantCopias() {
        return cantCopias;
    }

    public void setCantCopias(Integer cantCopias) {
        this.cantCopias = cantCopias;
    }

    public Double getAncho() { return ancho; }

    public void setAncho(Double ancho) { this.ancho = ancho; }

    public Double getAlto() { return alto; }

    public void setAlto(Double alto) { this.alto = alto; }

    public String getMetrosTotales() {
        return metrosTotales;
    }

    public void setMetrosTotales(String metrosTotales) { this.metrosTotales = metrosTotales; }

    public Double getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(Double costoServicio) {
        this.costoServicio = costoServicio;
    }

    public Archivo getArchivo() { return archivo; }

    public void setArchivo(Archivo archivo) { this.archivo = archivo; }
}
