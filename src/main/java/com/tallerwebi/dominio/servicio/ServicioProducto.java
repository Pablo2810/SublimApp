package com.tallerwebi.dominio.servicio;


import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Prenda;
import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.presentacion.dto.DatosProducto;

import javax.transaction.Transactional;

@Transactional
public interface ServicioProducto {
    DatosProducto generarDatosProducto(Archivo archivo, Prenda prenda, Integer cantidad, Double precio, Talle talle, Tela tela);
}
