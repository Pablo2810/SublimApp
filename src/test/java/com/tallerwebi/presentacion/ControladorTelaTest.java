package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.TipoEnvio;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioCompraTela;
import com.tallerwebi.dominio.servicio.ServicioCotizacionDolar;
import com.tallerwebi.dominio.servicio.ServicioEnvio;
import com.tallerwebi.dominio.servicio.ServicioPago;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorTelaTest {

    private ControladorTela controlador;
    private ServicioTela servicioMock;
    private ServicioEnvio servicioEnvioMock;
    private ServicioCotizacionDolar servicioCotizacionDolarMock;
    private ServicioPago servicioPagoMock;
    private RepositorioCompraTela repositorioCompraTelaMock;

    private Model modelMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void setUp() {
        servicioMock = mock(ServicioTela.class);
        servicioEnvioMock = mock(ServicioEnvio.class);
        servicioCotizacionDolarMock = mock(ServicioCotizacionDolar.class);
        servicioPagoMock = mock(ServicioPago.class);

        controlador = new ControladorTela(servicioMock, servicioEnvioMock, servicioCotizacionDolarMock, servicioPagoMock, repositorioCompraTelaMock);

        modelMock = mock(Model.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = new Usuario();

        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);
    }

    @Test
    public void mostrarCatalogoTelas_DebeAgregarListaDeTelasAlModelo_YRetornarVistaCatalogo() {
        List<MisTelas> telasCatalogo = List.of(new MisTelas(1L, TipoTela.LINO, "Rojo", 100.0, "img.jpg"));
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(telasCatalogo);

        String vista = controlador.mostrarCatalogoTelas(modelMock);

        verify(modelMock).addAttribute("telas", telasCatalogo);
        assertEquals("catalogo-telas", vista);
    }

    @Test
    public void mostrarCatalogoTelasConServicioNull_DebeAgregarMensajeError_YRetornarVistaCatalogo() {
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(null);

        String vista = controlador.mostrarCatalogoTelas(modelMock);

        verify(modelMock).addAttribute(eq("mensajeError"), anyString());
        assertEquals("catalogo-telas", vista);
    }

    @Test
    public void verDetalleTelaPorIdConIdNoExistente_DebeAgregarMensajeError_YRetornarVistaCatalogo() {
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(Collections.emptyList());

        String vista = controlador.verDetalleTelaPorId(999L, modelMock);

        verify(modelMock).addAttribute("mensajeError", "Tela no encontrada.");
        assertEquals("catalogo-telas", vista);
    }

    @Test
    public void mostrarBoletaTela_DebeRetornarVistaBoleta() {
        String vista = controlador.mostrarBoleta();
        assertEquals("boleta-tela", vista);
    }

    @Test
    public void procesarCompraConMetodoPagoValido_DebeRedirigirAMetodoPagoTela_YPasarDatos() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.procesarCompra(1L,"rojo", 12.0, 2.0, "algodon", "img.jpg", redirectAttrs);

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("idTela", 1L);
        verify(redirectAttrs).addFlashAttribute("color", "rojo");
        verify(redirectAttrs).addFlashAttribute("precio", 12.0);
        verify(redirectAttrs).addFlashAttribute("metros", 2.0);
        verify(redirectAttrs).addFlashAttribute("tipoTela", "algodon");
        verify(redirectAttrs).addFlashAttribute("imagenUrl", "img.jpg");
    }

    @Test
    public void procesarCompraConPrecioNegativo_DebeAgregarMensajeError_YRedirigirMetodoPago() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.procesarCompra(1L, "rojo", -10.0, 2.0, "algodon", "img.jpg", redirectAttrs);

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Precio inválido.");
    }

    @Test
    public void procesarCompraConMetrosNulos_DebeRedirigirConError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.procesarCompra(1L, "rojo", 50.0, null, "algodon", "img.jpg", redirectAttrs);

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Metros inválidos.");
    }

    @Test
    public void confirmarPagoEnPesosConMetodoDebito_DebeIgnorarCuotas_YAgregarMensajeExito() throws Exception{
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        Usuario usuario = new Usuario();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);

        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(TipoEnvio.LOCAL);

        // Mock cálculo total y cuota
        when(servicioPagoMock.calcularTotal(15.0, 3.0, TipoEnvio.LOCAL.getCosto(), 1)).thenReturn(45.0);
        when(servicioPagoMock.calcularValorCuota(45.0, 1)).thenReturn(45.0);

        String resultado = controlador.confirmarPago(
                "debito", "1111222233334444", "Carlos Soto", "2028-10", "789",
                3, "azul", 15.0, 3.0, "lino", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(redirectAttrs).addFlashAttribute("total", 45.0);
        verify(redirectAttrs).addFlashAttribute("cuotas", 1);
        verify(redirectAttrs).addFlashAttribute("valorCuota", 45.0);

        verify(servicioMock).comprarTelaDeFabrica(1L, 3.0, usuario);
    }

    @Test
    public void confirmarPagoEnPesosConCreditoY3Cuotas_DebeCalcularTotalEInteresCorrectamente() throws Exception {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        TipoEnvio envioLocal = mock(TipoEnvio.class);
        when(envioLocal.getCosto()).thenReturn(100.0);
        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(envioLocal);

        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        // Simular cálculos de pago
        when(servicioPagoMock.calcularTotal(20.0, 2.0, 100.0, 3)).thenReturn(43.2);
        when(servicioPagoMock.calcularValorCuota(43.2, 3)).thenReturn(14.4);

        controlador.confirmarPago(
                "credito", "2222333344445555", "Laura Diaz", "2027-09", "321",
                3, "rojo", 20.0, 2.0, "algodon", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        verify(redirectAttrs).addFlashAttribute(eq("total"), doubleThat(v -> Math.abs(v - 43.2) < 0.01));
        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"), doubleThat(v -> Math.abs(v - 14.4) < 0.01));
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");

        verify(servicioMock).comprarTelaDeFabrica(1L, 2.0, usuarioMock);
    }

    @Test
    public void confirmarPagoEnPesosconDebitoConCuotasMayoresA1_DebeForzarUnaCuota() throws Exception{
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(TipoEnvio.LOCAL);

        // Simular cálculos de pago con cuota 1
        when(servicioPagoMock.calcularTotal(10.0, 5.0, TipoEnvio.LOCAL.getCosto(), 1)).thenReturn(50.0);
        when(servicioPagoMock.calcularValorCuota(50.0, 1)).thenReturn(50.0);

        controlador.confirmarPago(
                "debito", "1111222233334444", "Luis Ramos", "2025-08", "111",
                6, "verde", 10.0, 5.0, "lino", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        verify(redirectAttrs).addFlashAttribute("cuotas", 1);
        verify(redirectAttrs).addFlashAttribute("valorCuota", 50.0);
        verify(redirectAttrs).addFlashAttribute("total", 50.0);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
    }

    @Test
    public void confirmarPagoEnPesosConMetodoPagoInvalido_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "paypal", "1234567812345678", "Ana Lopez", "2026-12", "456",
                3, "azul", 10.0, 1.0, "algodon", "img.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Método de pago inválido.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConCuotasInvalidas_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        String resultado = controlador.confirmarPago(
                "credito", "9999888877776666", "Marta Sosa", "2030-01", "999",
                0, "negro", 30.0, 1.0, "lino", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Debe seleccionar una cantidad de cuotas válida.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConNumeroTarjetaInvalido_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "debito", "1234", "Nombre Apellido", "2025-12", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Número de tarjeta inválido.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConNombreTitularVacio_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "credito", "1234567812345678", "   ", "2025-12", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("El nombre del titular es obligatorio.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConFechaVencimientoInvalida_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "debito", "1234567812345678", "Maria Gomez", "1225", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Fecha de vencimiento inválida.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConCodigoSeguridadInvalido_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "credito", "1234567812345678", "Carlos Perez", "2025-12", "12",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("CVV inválido.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConCreditoSinCuotas_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "credito", "1234567812345678", "Juan Pérez", "2026-10", "123",
                null, "azul", 40.0, 1.0, "algodon", "imagen.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Debe seleccionar una cantidad de cuotas válida.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnPesosConTipoEnvioInvalido_DebeMostrarError() throws Exception {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        // Simular tipo de envío inválido
        when(servicioEnvioMock.determinarTipoEnvio("volar")).thenReturn(null);
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        String resultado = controlador.confirmarPago(
                "debito", "1234567812345678", "Carlos Sosa", "2026-10", "123",
                1, "azul", 10.0, 1.0, "algodon", "img.jpg",
                1L, "volar", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Método de envío inválido.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnDolaresConDebito_DebeIgnorarCuotas_YAgregarMensajeExito() throws Exception {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        Usuario usuario = new Usuario();

        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);
        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(TipoEnvio.LOCAL);
        when(servicioCotizacionDolarMock.obtenerCotizacionDolar()).thenReturn(1270.0);

        // Simular cálculos de pago en dólares con cuota 1
        when(servicioPagoMock.calcularTotal(15.0, 3.0, TipoEnvio.LOCAL.getCosto(), 1)).thenReturn(45.0);
        when(servicioPagoMock.calcularTotalEnDolares(45.0, 1270.0)).thenReturn(45.0 / 1270.0);
        when(servicioPagoMock.calcularValorCuota(45.0 / 1270.0, 1)).thenReturn(45.0 / 1270.0);

        String resultado = controlador.confirmarPago(
                "debito", "1111222233334444", "Carlos Soto", "2028-10", "789",
                3, "azul", 15.0, 3.0, "lino", "url.jpg",
                1L, "LOCAL", true, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        verify(servicioMock).comprarTelaDeFabrica(1L, 3.0, usuario);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(redirectAttrs).addFlashAttribute("pagoEnDolares", true);
        verify(redirectAttrs).addFlashAttribute("cotizacionDolar", 1270.0);
        verify(redirectAttrs).addFlashAttribute("totalEquivalente", 45.0); // 15*3

        verify(redirectAttrs).addFlashAttribute("cuotas", 1);

        double totalUSD = 45.0 / 1270.0;
        verify(redirectAttrs).addFlashAttribute("valorCuota", totalUSD);
        verify(redirectAttrs).addFlashAttribute("total", totalUSD);
    }

    @Test
    public void confirmarPagoEnDolaresConCreditoY3Cuotas_DebeCalcularTotalEInteresCorrectamente() throws Exception {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);
        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(TipoEnvio.LOCAL);
        when(servicioCotizacionDolarMock.obtenerCotizacionDolar()).thenReturn(1270.0);

        // Simular cálculos de pago en dólares con 3 cuotas
        when(servicioPagoMock.calcularTotal(20.0, 2.0, TipoEnvio.LOCAL.getCosto(), 3)).thenReturn(43.2);
        when(servicioPagoMock.calcularTotalEnDolares(43.2, 1270.0)).thenReturn(43.2 / 1270.0);
        when(servicioPagoMock.calcularValorCuota(43.2 / 1270.0, 3)).thenReturn((43.2 / 1270.0) / 3);

        String resultado = controlador.confirmarPago(
                "credito", "2222333344445555", "Laura Diaz", "2027-09", "321",
                3, "rojo", 20.0, 2.0, "algodon", "url.jpg",
                1L, "LOCAL", true, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(redirectAttrs).addFlashAttribute("pagoEnDolares", true);
        verify(redirectAttrs).addFlashAttribute("cuotas", 3);
        verify(redirectAttrs).addFlashAttribute("cotizacionDolar", 1270.0);
        verify(redirectAttrs).addFlashAttribute("totalEquivalente", 43.2);
        verify(redirectAttrs).addFlashAttribute("total", 43.2 / 1270.0);
        verify(redirectAttrs).addFlashAttribute("valorCuota", (43.2 / 1270.0) / 3);

        verify(servicioMock).comprarTelaDeFabrica(1L, 2.0, usuarioMock);
    }

    @Test
    public void confirmarPagoEnPesosSinUsuarioLogueado_DebeRedirigirALoginConMensaje() throws Exception{
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(null);
        when(servicioEnvioMock.determinarTipoEnvio("LOCAL")).thenReturn(TipoEnvio.LOCAL);

        String resultado = controlador.confirmarPago(
                "debito", "1111222233334444", "Carlos Soto", "2028-10", "789",
                1, "azul", 15.0, 3.0, "lino", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/login", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Debés iniciar sesión para realizar la compra.");
    }

}



