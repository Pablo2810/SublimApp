package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.controlador.ControladorLogin;
import com.tallerwebi.presentacion.controlador.ControladorProducto;
import com.tallerwebi.presentacion.dto.DatosLogin;
import com.tallerwebi.presentacion.dto.DatosProducto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControladorProductoTest {
    private ControladorProducto controladorProducto;
    private ServicioPrenda servicioPrendaMock;
    private ServicioTalle servicioTalleMock;
    private ServicioTela servicioTelaMock;
    private ServicioArchivo servicioArchivoMock;
    private ServicioProducto servicioProductoMock;
    private ServicioPedido servicioPedidoMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private RedirectAttributes redirectAttrs;


    @BeforeEach
    public void init() {
        servicioPrendaMock = mock(ServicioPrenda.class);
        servicioTalleMock = mock(ServicioTalle.class);
        servicioTelaMock = mock(ServicioTela.class);
        servicioArchivoMock = mock(ServicioArchivo.class);
        servicioProductoMock = mock(ServicioProducto.class);
        servicioPedidoMock = mock(ServicioPedido.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        redirectAttrs = mock(RedirectAttributes.class);
        controladorProducto = new ControladorProducto(servicioProductoMock, servicioTalleMock, servicioPrendaMock, servicioTelaMock, servicioArchivoMock, servicioPedidoMock);
    }

    @Test
    void queSeMuestreElFormularioDeProducto() throws Exception {
        Prenda prenda = new Prenda();
        prenda.setId(1L);
        List<Prenda> prendas = List.of(prenda);

        when(servicioPrendaMock.obtenerTodas()).thenReturn(prendas);

        ModelAndView modelAndView = controladorProducto.mostrarFormularioDeProducto(redirectAttrs);

        assertEquals("nuevo-pedido", modelAndView.getViewName());
        assertEquals(prendas, modelAndView.getModel().get("prendas"));
    }

    @Test
    void queNoPuedaMostrarElFormularioSiNoHayPrendas() throws Exception {
        when(servicioPrendaMock.obtenerTodas()).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = controladorProducto.mostrarFormularioDeProducto(redirectAttrs);

        assertEquals("redirect:/home", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Por el momento no hay prendas en stock");
    }

    @Test
    void queNoPuedaMostrarElFormularioSiObtenerTodasLasPrendasLanzaExcepcion() throws Exception {
        when(servicioPrendaMock.obtenerTodas()).thenThrow(new TelaNoEncontrada());

        ModelAndView modelAndView = controladorProducto.mostrarFormularioDeProducto(redirectAttrs);

        assertEquals("redirect:/home", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Por el momento no hay prendas en stock");
    }

    @Test
    void queSePuedaRegistrarProductoCorrectamente() throws Exception {
        Prenda prenda = new Prenda();
        Talle talle = new Talle();
        Usuario usuario = new Usuario();
        Tela tela = new Tela();
        Archivo archivo = new Archivo();
        Producto producto = new Producto();
        Pedido pedido = new Pedido();

        DatosProducto datosProducto = new DatosProducto();
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(MultipartFile.class));

        usuario.setId(8L);
        prenda.setId(1L);
        talle.setId(2L);
        tela.setId(3L);
        archivo.setId(4L);
        producto.setId(10L);
        pedido.setProductos(new HashSet<>());


        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioPrendaMock.buscarPrendaPorId(1L)).thenReturn(prenda);
        when(servicioTalleMock.obtenerTalle(2L)).thenReturn(talle);
        when(servicioTelaMock.obtenerTela(3L)).thenReturn(tela);
        when(servicioArchivoMock.registrarArchivo(datosProducto.getArchivo())).thenReturn(archivo);
        when(servicioProductoMock.registrarProducto(5, archivo, prenda, talle, tela)).thenReturn(producto);
        when(servicioPedidoMock.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("detalle-pedido", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("pedido"));
    }

    @Test
    void queNoSePuedaRegistrarProductoPorQueNoSeEncontroLaPrendaElegida() throws Exception {
        Usuario usuario = new Usuario();

        DatosProducto datosProducto = new DatosProducto();
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(MultipartFile.class));

        usuario.setId(8L);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioPrendaMock.buscarPrendaPorId(anyLong())).thenReturn(null);

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("redirect:/nuevo-pedido", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Ocurrio un error al traer materiales para el pedido");
    }

    @Test
    void queNoSePuedaRegistrarProductoPorQueElArchivoSubidoEsInvalido() throws Exception {
        Usuario usuario = new Usuario();

        DatosProducto datosProducto = new DatosProducto();
        usuario.setId(8L);
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(MultipartFile.class));

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);

        when(servicioPrendaMock.buscarPrendaPorId(anyLong())).thenReturn(new Prenda());
        when(servicioTalleMock.obtenerTalle(anyLong())).thenReturn(new Talle());
        when(servicioTelaMock.obtenerTela(anyLong())).thenReturn(new Tela());
        when(servicioArchivoMock.registrarArchivo(any(MultipartFile.class))).thenThrow(new ArchivoNoValido());

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("redirect:/nuevo-pedido", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Ingrese un archivo valido");
    }



}
