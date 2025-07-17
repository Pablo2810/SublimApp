package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.servicio.ServicioCotizacionDolar;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.presentacion.controlador.ControladorPedido;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PedidoControllerTest {

 /*   private ControladorPedido controladorPedido;
    private ServicioPedido servicioPedido;
    private ServicioCotizacionDolar servicioCotizacionDolar;

    private Model model;
    private RedirectAttributes redirectAttributes;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        servicioPedido = mock(ServicioPedido.class);
        servicioCotizacionDolar = mock(ServicioCotizacionDolar.class);

        controladorPedido = new ControladorPedido(servicioPedido, null, null, null, null, servicioCotizacionDolar, null);
        model = new ExtendedModelMap();
        redirectAttributes = new RedirectAttributesModelMap();

        MockitoAnnotations.openMocks(this);

        // Agregamos ViewResolver para evitar ciclo en las vistas
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");  // o .jsp según tu setup real

        mockMvc = MockMvcBuilders.standaloneSetup(controladorPedido)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void mostrarFormularioPagoProducto_devuelveVistaYModeloCorrecto() {
        Long idPedido = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(idPedido);
        pedido.setMontoTotal(1000.0);

        Producto producto = new Producto();
        producto.setCantidad(2);
        pedido.setProductos(Set.of(producto));

        when(servicioPedido.obtenerPedido(idPedido)).thenReturn(pedido);
        when(servicioCotizacionDolar.obtenerCotizacionDolar()).thenReturn(1300.0);

        // Flash attributes simulados (pueden estar vacíos o tener valores de prueba)
        String mensajeError = "";
        String opcionEnvioFlash = "LOCAL";
        String direccionEnvioFlash = "";
        String metodoPagoFlash = "debito";
        String monedaFlash = "PESOS";

        String vista = controladorPedido.mostrarFormularioPagoProducto(
                idPedido,
                model,
                mensajeError,
                opcionEnvioFlash,
                direccionEnvioFlash,
                metodoPagoFlash,
                monedaFlash
        );

        assertEquals("metodo-pago-pedido", vista);
        assertEquals(pedido, model.getAttribute("pedido"));
        assertEquals(2, model.getAttribute("cantidadTotal"));
        assertEquals(500.0, model.getAttribute("precioUnitario"));
        assertEquals(1300.0, model.getAttribute("cotizacionDolar"));
    }

    @Test
    void confirmarPagoProductos_conParametrosValidos_redirigeABoleta() {
        Long idPedido = 1L;
        Pedido pedidoMock = new Pedido();
        pedidoMock.setId(idPedido);

        when(servicioPedido.procesarPagoPedidoProductos(
                eq(idPedido),
                eq(false),
                eq("debito"),
                eq("LOCAL"),
                eq(""),
                eq("1111222233334444"),
                eq("123"),
                eq("Juan Perez"),
                eq("2025-12"),
                eq(1)
        )).thenReturn(pedidoMock);

        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        String resultado = controladorPedido.confirmarPagoProductos(
                idPedido,
                false,
                "debito",
                1,
                "LOCAL",
                "",
                "1111222233334444",
                "123",
                "Juan Perez",
                "2025-12",
                redirectAttributes
        );

        assertEquals("redirect:/boleta-pedido", resultado);
        assertEquals(pedidoMock, redirectAttributes.getFlashAttributes().get("pedido"));
    }

    @Test
    void confirmarPagoProductos_conParametrosInvalidos_redirigeConError() {
        Long idPedido = 1L;

        // número de tarjeta inválido: menos de 16 dígitos
        String resultado = controladorPedido.confirmarPagoProductos(
                idPedido,
                false,
                "debito",
                1,
                "LOCAL",
                "",
                "1111",  // inválido
                "123",
                "Juan Perez",
                "2025-12",
                redirectAttributes
        );

        assertTrue(resultado.startsWith("redirect:/metodo-pago-pedido"));
        assertNotNull(redirectAttributes.getFlashAttributes().get("mensajeError"));
    }

    // Test pedido inexistente redirige a historial
    @Test
    void mostrarFormularioPagoProducto_pedidoInexistente_redirigeHistorial() throws Exception {
        when(servicioPedido.obtenerPedido(anyLong())).thenReturn(null);

        mockMvc.perform(get("/metodo-pago-pedido").param("idPedido", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/historial-pedidos**"));
    }

    // Test cotización dólar falla y usa valor por defecto
    @Test
    void mostrarFormularioPagoProducto_cotizacionDolarException_usaValorPorDefecto() throws Exception {
        when(servicioPedido.obtenerPedido(anyLong())).thenReturn(new Pedido());
        when(servicioCotizacionDolar.obtenerCotizacionDolar()).thenThrow(new RuntimeException("Fallo cotización"));

        mockMvc.perform(get("/metodo-pago-pedido").param("idPedido", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cotizacionDolar", 1270.0));
    }

    // Test confirmacion pago con tarjeta inválida
    @Test
    void confirmarPagoProductos_tarjetaInvalida_redirigeConError() throws Exception {
        mockMvc.perform(post("/confirmar-pago-productos")
                        .param("idPedido", "1")
                        .param("pagoEnDolares", "false")
                        .param("metodoPago", "debito")
                        .param("cuotas", "1")
                        .param("opcionEnvio", "LOCAL")
                        .param("numeroTarjeta", "123") // inválido
                        .param("cvv", "123")
                        .param("nombreTitular", "Juan")
                        .param("vencimiento", "2025-12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/metodo-pago-pedido**"))
                .andExpect(flash().attributeExists("mensajeError"));
    }

    // Test pago válido redirige a boleta
    @Test
    void confirmarPagoProductos_valido_redirigeBoleta() throws Exception {
        // Mock del pedido que debe retornar el servicio
        Pedido pedidoMock = new Pedido();
        pedidoMock.setId(1L);

        // Configuramos el mock para que devuelva pedidoMock al llamar al servicio
        when(servicioPedido.procesarPagoPedidoProductos(
                anyLong(),
                anyBoolean(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyInt()
        )).thenReturn(pedidoMock);

        mockMvc.perform(post("/confirmar-pago-productos")
                        .param("idPedido", "1")
                        .param("pagoEnDolares", "false") // String que debe mapear a Boolean false
                        .param("metodoPago", "debito")
                        .param("cuotas", "1")
                        .param("opcionEnvio", "LOCAL")
                        .param("numeroTarjeta", "1111222233334444")
                        .param("cvv", "123")
                        .param("nombreTitular", "Juan Perez")
                        .param("vencimiento", "2025-12"))
                .andDo(print()) // Imprime la respuesta para debug
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boleta-pedido"))
                .andExpect(flash().attributeExists("pedido"));

        // Verificamos que el servicio fue llamado exactamente una vez
        verify(servicioPedido, times(1)).procesarPagoPedidoProductos(
                anyLong(),
                anyBoolean(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyInt()
        );
    }
*/
}


