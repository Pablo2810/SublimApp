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
}
