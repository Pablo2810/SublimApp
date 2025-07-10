package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class Talle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prenda")
    private Prenda prenda;

    private String descripcion;
    private Double metrosTotales;

    private Double cinturaMIN;
    private Double cinturaMAX;

    private Double pechoMIN;
    private Double pechoMAX;

    private String pais;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prenda getPrenda() {
        return prenda;
    }

    public void setPrenda(Prenda prenda) {
        this.prenda = prenda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMetrosTotales() {
        return metrosTotales;
    }

    public void setMetrosTotales(Double metrosTotales) {
        this.metrosTotales = metrosTotales;
    }

    public Double getCinturaMIN() { return cinturaMIN; }

    public void setCinturaMIN(Double cinturaMIN) { this.cinturaMIN = cinturaMIN; }

    public Double getCinturaMAX() { return cinturaMAX; }

    public void setCinturaMAX(Double cinturaMAX) { this.cinturaMAX = cinturaMAX; }

    public Double getPechoMIN() { return pechoMIN; }

    public void setPechoMIN(Double pechoMIN) { this.pechoMIN = pechoMIN; }

    public Double getPechoMAX() { return pechoMAX; }

    public void setPechoMAX(Double pechoMAX) { this.pechoMAX = pechoMAX; }

    public String getPais() { return pais; }

    public void setPais(String pais) { this.pais = pais; }
}