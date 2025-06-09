package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.HashSet;

@Entity
public class ProductoBase extends Producto{
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
