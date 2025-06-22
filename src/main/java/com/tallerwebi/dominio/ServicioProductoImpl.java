package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.dominio.servicio.ServicioProducto;
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
        /*producto.setPrecio(); //CALCULAR PRECIO EN BASE A LA PRENDA Y LA CANTIDAD*/
        producto.setArchivo(archivo);//ANTES SE DEBE VALIDAR EL ARCHIVO CORRECTAMENTE
        producto.setPrenda(prenda);
        producto.setTalle(talle);
        producto.setTela(tela);//ANTES DE ASIGNARLA VERIFICAR QUE LA TELA LA TENGA EL USUARIO LOGUEADO Y RESTAR LOS METROS EN BASE AL LARGO DE LA PRENDA POR CANTIDAD
        return repositorioProducto.guardarProducto(producto);
    }

    /*@Override
    public DatosProducto generarDatosProducto(Archivo archivo, Prenda prenda, Integer cantidad, Double precio, Talle talle, Tela tela) {
        DatosProducto dtoProducto = new DatosProducto();
        dtoProducto.setCantidad(cantidad);
        dtoProducto.setPrecio(precio);
        dtoProducto.setTalle(talle);
        dtoProducto.setTela(tela);
        dtoProducto.setArchivo(archivo);
        dtoProducto.setPrenda(prenda);

        return dtoProducto;
    }

    public void ingresarProducto(DatosProducto producto, Pedido pedido) {

    }*/
}
