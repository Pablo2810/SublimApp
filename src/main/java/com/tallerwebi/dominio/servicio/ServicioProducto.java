package com.tallerwebi.dominio.servicio;


import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.presentacion.dto.DatosProducto;

import javax.transaction.Transactional;

@Transactional
public interface ServicioProducto {
    //DatosProducto generarDatosProducto(Archivo archivo, Prenda prenda, Integer cantidad, Double precio, Talle talle, Tela tela);

    Producto registrarProducto(Integer cantidad, Archivo archivo, Prenda prenda, Talle talle, Tela tela);
    void eliminarProducto(Long idProducto);
    Producto buscarPorId(Long id);
}
