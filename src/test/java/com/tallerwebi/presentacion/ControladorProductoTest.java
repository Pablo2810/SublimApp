package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoNoValido;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.controlador.ControladorLogin;
import com.tallerwebi.presentacion.controlador.ControladorProducto;
import com.tallerwebi.presentacion.dto.DatosLogin;
import com.tallerwebi.presentacion.dto.DatosPrenda;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorProductoTest {
    private ControladorProducto controladorProducto;
    private ServicioPrenda servicioPrendaMock;
    private ServicioTalle servicioTalleMock;
    private ServicioTela servicioTelaMock;
    private ServicioArchivo servicioArchivoMock;
    private ServicioProducto servicioProductoMock;
    private ServicioPedido servicioPedidoMock;
    private ServicioCotizacion servicioCotizacionMock;
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
        controladorProducto = new ControladorProducto(servicioProductoMock, servicioTalleMock, servicioPrendaMock,
                servicioTelaMock, servicioArchivoMock, servicioPedidoMock, servicioCotizacionMock);
    }

    private DatosProducto crearDatosProductoEjemplo() {
        DatosProducto datosProducto = new DatosProducto();
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(MultipartFile.class));
        return datosProducto;
    }

    @Test
    void queSeMuestreElFormularioDeProducto() throws Exception {
        Prenda prenda = new Prenda();
        prenda.setId(1L);
        prenda.setDescripcion("Remera");
        prenda.setPrecioBase(1000.0);

        List<Prenda> prendas = List.of(prenda);

        when(servicioPrendaMock.obtenerTodas()).thenReturn(prendas);

        ModelAndView modelAndView = controladorProducto.mostrarFormularioDeProducto(redirectAttrs);
        List<DatosPrenda> listaDatosPrenda = (List<DatosPrenda>) modelAndView.getModel().get("prendas");

        assertEquals("nuevo-pedido", modelAndView.getViewName());
        assertEquals(1, listaDatosPrenda.size());
        assertEquals("Remera", listaDatosPrenda.get(0).getDescripcion());
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

  /* @Test
    void queSePuedaRegistrarProductoCorrectamente() throws Exception {

        Prenda prenda = new Prenda();
        prenda.setId(1L);

        Talle talle = new Talle();
        talle.setId(2L);
        talle.setMetrosTotales(1.0);

        Tela tela = new Tela();
        tela.setId(3L);

        Archivo archivo = new Archivo();
        archivo.setId(4L);

        Producto producto = new Producto();
        producto.setId(10L);
        producto.setCantidad(5);

        Usuario usuario = new Usuario();
        usuario.setId(8L);

        Pedido pedido = new Pedido();
        pedido.setProductos(new HashSet<>());

        DatosProducto datosProducto = new DatosProducto();
        datosProducto.setPrendaId(1L);
        datosProducto.setTalleId(2L);
        datosProducto.setTelaId(3L);
        datosProducto.setCantidad(5);
        datosProducto.setArchivo(mock(MultipartFile.class)); // simula archivo subido

        // Mockear sesión y servicios
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioTalleMock.obtenerTalle(anyLong())).thenReturn(talle);
        when(servicioTelaMock.obtenerTela(anyLong())).thenReturn(tela);
        when(servicioPrendaMock.buscarPrendaPorId(anyLong())).thenReturn(prenda);
        when(servicioArchivoMock.registrarArchivo(any(MultipartFile.class))).thenReturn(archivo);
        when(servicioProductoMock.registrarProducto(5, archivo, prenda, talle, tela)).thenReturn(producto);
        when(servicioPedidoMock.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);

        doNothing().when(servicioPedidoMock).asociarProductoPedido(pedido);
        doNothing().when(servicioTelaMock).consumirTelaParaProducto(tela, talle.getMetrosTotales() * 5, usuario);

        assertNotNull(servicioPrendaMock.buscarPrendaPorId(1L), "Prenda es null");
        assertNotNull(servicioTalleMock.obtenerTalle(2L), "Talle es null");
        assertNotNull(servicioTelaMock.obtenerTela(3L), "Tela es null");

        // Llamar al método
        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        // Verificaciones
        assertEquals("detalle-pedido", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("pedido"));

        Pedido pedidoEnModelo = (Pedido) modelAndView.getModel().get("pedido");
        assertNotNull(pedidoEnModelo);
        assertTrue(pedidoEnModelo.getProductos().contains(producto));

        verify(servicioPedidoMock).asociarProductoPedido(pedido);
        verify(servicioTelaMock).consumirTelaParaProducto(tela, talle.getMetrosTotales() * 5, usuario);
    } */

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
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Materiales inválidos");

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
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Ingrese un archivo válido");

    }

    @Test
    void queRedirijaALoginSiNoHayUsuarioLogueado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(null);

        DatosProducto datosProducto = new DatosProducto();
        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    void queMuestreErrorYRedirijaSiNoHayStockSuficiente() throws Exception {
        DatosProducto datosProducto = crearDatosProductoEjemplo();
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioPrendaMock.buscarPrendaPorId(datosProducto.getPrendaId())).thenReturn(new Prenda());
        when(servicioTalleMock.obtenerTalle(datosProducto.getTalleId())).thenReturn(new Talle());
        when(servicioTelaMock.obtenerTela(datosProducto.getTelaId())).thenReturn(new Tela());
        when(servicioArchivoMock.registrarArchivo(any())).thenReturn(new Archivo());
        when(servicioProductoMock.registrarProducto(anyInt(), any(), any(), any(), any())).thenReturn(new Producto());
        when(servicioPedidoMock.buscarPedidoEstadoPendiente(usuario)).thenReturn(new Pedido());

        doThrow(new StockInsuficiente())
                .when(servicioTelaMock).consumirTelaParaProducto(any(Tela.class), anyDouble(), any(Usuario.class));

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("redirect:/nuevo-pedido", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Datos inválidos");

    }


    @Test
    void queMuestreErrorYRedirijaSiTelaNoEncontrada() throws Exception {
        DatosProducto datosProducto = crearDatosProductoEjemplo();
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioPrendaMock.buscarPrendaPorId(datosProducto.getPrendaId())).thenReturn(new Prenda());
        when(servicioTalleMock.obtenerTalle(datosProducto.getTalleId())).thenReturn(new Talle());
        when(servicioTelaMock.obtenerTela(datosProducto.getTelaId())).thenReturn(new Tela());
        when(servicioArchivoMock.registrarArchivo(any())).thenReturn(new Archivo());
        when(servicioProductoMock.registrarProducto(anyInt(), any(), any(), any(), any())).thenReturn(new Producto());
        when(servicioPedidoMock.buscarPedidoEstadoPendiente(usuario)).thenReturn(new Pedido());

        doThrow(new TelaNoEncontrada())
                .when(servicioTelaMock).consumirTelaParaProducto(any(Tela.class), anyDouble(), any(Usuario.class));

        ModelAndView modelAndView = controladorProducto.registrarProductoAlPedido(datosProducto, requestMock, redirectAttrs);

        assertEquals("redirect:/nuevo-pedido", modelAndView.getViewName());
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Datos inválidos");
    }

}
