package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.controlador.ControladorLogin;
import com.tallerwebi.presentacion.controlador.ControladorProducto;
import com.tallerwebi.presentacion.dto.DatosLogin;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorProductoTest {
    private ControladorProducto controladorProducto;
    private ServicioPrenda servicioPrendaMock;
    private  ServicioTalle servicioTalleMock;
    private ServicioTela servicioTelaMock;
    private ServicioArchivo servicioArchivoMock;
    private ServicioProducto servicioProductoMock;
    private ServicioPedido servicioPedidoMock;


    @BeforeEach
    public void init(){
        servicioPrendaMock = mock(ServicioPrenda.class);
        servicioTalleMock = mock(ServicioTalle.class);
        servicioTelaMock = mock(ServicioTela.class);
        servicioArchivoMock = mock(ServicioArchivo.class);
        servicioProductoMock = mock(ServicioProducto.class);
        servicioPedidoMock = mock(ServicioPedido.class);
        controladorProducto = new ControladorProducto(servicioProductoMock, servicioTalleMock, servicioPrendaMock, servicioTelaMock, servicioArchivoMock, servicioPedidoMock);
    }

    @Test
    void queSePuedaRegistrarProductoCorrectamente() throws Exception {
        DatosProducto datosProducto = new DatosProducto();
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(org.springframework.web.multipart.MultipartFile.class));

        Prenda prenda = new Prenda(); prenda.setId(1L);
        Talle talle = new Talle(); talle.setId(2L);
        Tela tela = new Tela(); tela.setId(3L);
        Archivo archivo = new Archivo(); archivo.setId(4L);
        Producto producto = new Producto(); producto.setId(10L);
        Pedido pedido = new Pedido(); pedido.setProductos(new HashSet<>());


        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        Usuario usuario = new Usuario(); usuario.setId(8L);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioPrendaMock.buscarPrendaPorId(1L)).thenReturn(prenda);
        when(servicioTalleMock.obtenerTalle(2L)).thenReturn(talle);
        when(servicioTelaMock.obtenerTela(3L)).thenReturn(tela);
        when(servicioArchivoMock.registrarArchivo(datosProducto.getArchivo())).thenReturn(archivo);
        when(servicioProductoMock.registrarProducto(5, archivo, prenda, talle, tela)).thenReturn(producto);
        when(servicioPedidoMock.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock);

        assertEquals("detalle-pedido", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("pedido"));

    }

}
