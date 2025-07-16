package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.dominio.servicio.ServicioProducto;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioProductoImpl implements ServicioProducto {

    private RepositorioProducto repositorioProducto;

    @Autowired
    public ServicioProductoImpl(RepositorioProducto repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    @Override
    public Producto registrarProducto(Integer cantidad, Archivo archivo, Prenda prenda, Talle talle, Tela tela) {
        Producto producto = new Producto();
        producto.setCantidad(cantidad);
        producto.setArchivo(archivo);
        producto.setPrenda(prenda);
        producto.setTalle(talle);
        producto.setTela(tela);
        Double precio = prenda.getPrecioBase() + (talle.getMetrosTotales() * tela.getPrecio());
        producto.setPrecio(precio);
        return repositorioProducto.guardarProducto(producto);
    }

    @Override
    public void eliminarProducto(Long idProducto) {
        repositorioProducto.eliminarProducto(idProducto);
    }

    @Override
    public void actualizarImagenProducto(Long id, Producto producto) {
        Producto productoEncontrado = repositorioProducto.buscar(id);
        productoEncontrado.setImagenUrl(producto.getImagenUrl());
        productoEncontrado.setImagenPrendaConDisenioUrl(producto.getImagenPrendaConDisenioUrl());
        repositorioProducto.actualizarProducto(productoEncontrado);
    }

    public Producto buscarPorId(Long id) {
        return repositorioProducto.buscar(id);
    }

}
