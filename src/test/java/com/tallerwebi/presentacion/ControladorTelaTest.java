package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;



public class ControladorTelaTest {

    private ControladorTela controlador;
    private ServicioTela servicioMock;
    private Model modelMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void setUp() {
        servicioMock = mock(ServicioTela.class);
        controlador = new ControladorTela(servicioMock);
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
    public void mostrarVistaPago_DebeRetornarNombreVistaMetodoPago() {
        String vista = controlador.mostrarVistaPago();
        assertEquals("metodo-pago-tela", vista);
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
        verify(redirectAttrs).addFlashAttribute(eq("metros"), isNull());
    }

    @Test
    public void confirmarPagoConMetodoDebito_DebeIgnorarCuotas_YAgregarMensajeExito() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        Usuario usuario = new Usuario();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);

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
    public void confirmarPagoConCreditoY3Cuotas_DebeCalcularTotalEInteresCorrectamente() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        controlador.confirmarPago(
                "credito", "2222333344445555", "Laura Diaz", "2027-09", "321",
                3, "rojo", 20.0, 2.0, "algodon", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        double totalEsperado = 20.0 * 2.0 * 1.08;
        double cuotaEsperada = totalEsperado / 3;

        verify(redirectAttrs).addFlashAttribute(eq("total"), doubleThat(v -> Math.abs(v - totalEsperado) < 0.01));
        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"), doubleThat(v -> Math.abs(v - cuotaEsperada) < 0.01));
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(servicioMock).comprarTelaDeFabrica(1L, 2.0, usuarioMock);
    }

    @Test
    public void confirmarPagoDebitoConCuotasMayoresA1_DebeForzarUnaCuota() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        controlador.confirmarPago(
                "debito", "1111222233334444", "Luis Ramos", "2025-08", "111",
                6, "verde", 10.0, 5.0, "lino", "url.jpg",
                1L, "LOCAL", false, sessionMock, model, redirectAttrs
        );

        double total = 10.0 * 5.0;

        verify(redirectAttrs).addFlashAttribute("cuotas", 1);
        verify(redirectAttrs).addFlashAttribute("valorCuota", total);
        verify(redirectAttrs).addFlashAttribute("total", total);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
    }

    @Test
    public void confirmarPagoConMetodoPagoInvalido_DebeMostrarError() {
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
    public void confirmarPagoConCuotasInvalidas_DebeMostrarError() {
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
    public void confirmarPagoConNumeroTarjetaInvalido_DebeMostrarError() {
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
    public void confirmarPagoConNombreTitularVacio_DebeMostrarError() {
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
    public void confirmarPagoConFechaVencimientoInvalida_DebeMostrarError() {
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
    public void confirmarPagoConCodigoSeguridadInvalido_DebeMostrarError() {
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
    public void confirmarPagoCreditoSinCuotas_DebeMostrarError() {
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
    public void confirmarPagoConTipoEnvioInvalido_DebeMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();

        String resultado = controlador.confirmarPago(
                "debito", "1234567812345678", "Carlos Sosa", "2026-10", "123",
                1, "azul", 10.0, 1.0, "algodon", "img.jpg",
                1L, "volar", false, sessionMock, model, redirectAttrs
        );

        assertEquals("metodo-pago-tela", resultado);
        assertEquals("Método de envío inválido.", model.asMap().get("mensajeError"));
    }

    @Test
    public void confirmarPagoEnDolaresConDebito_DebeIgnorarCuotas_YAgregarMensajeExito() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        Usuario usuario = new Usuario();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuario);

        // precio = 15.0, metros = 3.0 → total en pesos = 45.0 → / cotización (1230) ≈ 0.0366
        String resultado = controlador.confirmarPago(
                "debito", "1111222233334444", "Carlos Soto", "2028-10", "789",
                3, "azul", 15.0, 3.0, "lino", "url.jpg",
                1L, "LOCAL", true, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        verify(servicioMock).comprarTelaDeFabrica(1L, 3.0, usuario);

        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(redirectAttrs).addFlashAttribute("pagoEnDolares", true);
        verify(redirectAttrs).addFlashAttribute(eq("cotizacionDolar"), any(Double.class));
        verify(redirectAttrs).addFlashAttribute(eq("totalEquivalente"), eq(45.0));
        verify(redirectAttrs).addFlashAttribute("cuotas", 1);

        // Valor aproximado de cuota en dólares ≈ 0.0366
        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"), argThat(valor ->
                valor instanceof Double && ((Double) valor) > 0.036 && ((Double) valor) < 0.037
        ));

        verify(redirectAttrs).addFlashAttribute(eq("total"), argThat(valor ->
                valor instanceof Double && ((Double) valor) > 0.036 && ((Double) valor) < 0.037
        ));
    }

    @Test
    public void confirmarPagoEnDolaresConCreditoY3Cuotas_DebeCalcularTotalEInteresCorrectamente() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        Model model = new ExtendedModelMap();
        when(sessionMock.getAttribute("usuarioLogueado")).thenReturn(usuarioMock);

        String resultado = controlador.confirmarPago(
                "credito", "2222333344445555", "Laura Diaz", "2027-09", "321",
                3, "rojo", 20.0, 2.0, "algodon", "url.jpg",
                1L, "LOCAL", true, sessionMock, model, redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        double totalPesos = 20.0 * 2.0 * 1.08; // 43.2
        double cotizacionDolar = 1230.0;
        double totalUSD = totalPesos / cotizacionDolar;       // ≈ 0.0351
        double cuotaUSD = totalUSD / 3;                        // ≈ 0.0117

        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");

        verify(redirectAttrs).addFlashAttribute(eq("total"),
                argThat(v -> v instanceof Double && Math.abs((Double) v - totalUSD) < 0.0001));

        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"),
                argThat(v -> v instanceof Double && Math.abs((Double) v - cuotaUSD) < 0.0001));

        verify(redirectAttrs).addFlashAttribute("cuotas", 3);
        verify(redirectAttrs).addFlashAttribute("pagoEnDolares", true);
        verify(redirectAttrs).addFlashAttribute(eq("cotizacionDolar"), any(Double.class));
        verify(redirectAttrs).addFlashAttribute(eq("totalEquivalente"),
                argThat(v -> v instanceof Double && Math.abs((Double) v - totalPesos) < 0.0001));

        verify(servicioMock).comprarTelaDeFabrica(1L, 2.0, usuarioMock);
    }

}



