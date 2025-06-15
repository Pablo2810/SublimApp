package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorTelaTest {

    private ControladorTela controlador;
    private ServicioTela servicioMock;
    private Model modelMock;

    @BeforeEach
    public void setUp() {
        servicioMock = mock(ServicioTela.class);
        controlador = new ControladorTela(servicioMock);
        modelMock = mock(Model.class);
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
    public void mostrarFormularioCarga_DebeAgregarTelasDelUsuarioYTiposAlModelo_YRetornarVistaCarga() {
        String vista = controlador.mostrarFormularioCarga(modelMock);

        verify(modelMock).addAttribute(eq("telasUsuario"), anyList());
        verify(modelMock).addAttribute(eq("tiposTela"), eq(TipoTela.values()));
        assertEquals("cargar-tela", vista);
    }

    @Test
    public void cargarTelaConTipoValido_DebeAgregarTelaALaListaInterna_YAgregarMensajeExitoYRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.cargarTela("algodon", "azul", "url.jpg", redirectAttrs);

        assertEquals("redirect:/cargar-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Tela guardada con éxito");
        // Verificamos que la lista interna tenga una tela
        try {
            var field = controlador.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<MisTelas> lista = (List<MisTelas>) field.get(controlador);
            assertFalse(lista.isEmpty());
            assertEquals("azul", lista.get(0).getColor());
            assertEquals(TipoTela.ALGODON, lista.get(0).getTipoTela());
        } catch (Exception e) {
            fail("Error accediendo a telasDelUsuario");
        }
    }

    @Test
    public void cargarTelaConTipoInvalido_DebeAgregarMensajeError_YRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.cargarTela("tipoInvalido", "rojo", "img.jpg", redirectAttrs);

        assertEquals("redirect:/cargar-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Tipo de tela inválido");
    }

    @Test
    public void registrarTelaDesdeCatalogoConIdValido_DebeAgregarTelaALaListaDelUsuario_YAgregarMensajeExitoYRedirigir() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Azul", 50.0, "img.jpg");
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(List.of(tela));

        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        String vista = controlador.registrarTelaDesdeCatalogo(10L, redirectAttrs);

        assertEquals("redirect:/catalogo-telas", vista);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Tela comprada con éxito");
        // La lista interna debe contener la tela
        try {
            var field = controlador.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<MisTelas> lista = (List<MisTelas>) field.get(controlador);
            assertTrue(lista.stream().anyMatch(t -> t.getId().equals(10L)));
        } catch (Exception e) {
            fail("Error accediendo a telasDelUsuario");
        }
    }

    @Test
    public void registrarTelaDesdeCatalogoConTelaYaComprada_DebeAgregarMensajeAdvertencia_YNoDuplicarCompra() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Blanco", 0.0, "img.jpg");
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(List.of(tela));

        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        controlador.registrarTelaDesdeCatalogo(10L, redirectAttrs); // Primera compra
        controlador.registrarTelaDesdeCatalogo(10L, redirectAttrs); // Intento repetido

        verify(redirectAttrs).addFlashAttribute("mensajeAdvertencia", "Ya has comprado esta tela");
    }

    @Test
    public void registrarTelaDesdeCatalogoConIdInvalido_DebeAgregarMensajeError_YRedirigir() {
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(Collections.emptyList());

        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        String vista = controlador.registrarTelaDesdeCatalogo(999L, redirectAttrs);

        assertEquals("redirect:/catalogo-telas", vista);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Tela no encontrada en el catálogo");
    }

    @Test
    public void mostrarVistaPago_DebeRetornarNombreVistaMetodoPago() {
        String vista = controlador.mostrarVistaPago();
        assertEquals("metodo-pago-tela", vista);
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
    public void confirmarPagoConTipoTelaInvalido_DebeLanzarIllegalArgumentException() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.confirmarPago(
                    "credito", "1234567812345678", "Juan Perez", "2025-12", "123",
                    6, "azul", 10.0, 1.0, "tipoInvalido", "img.jpg",
                    redirectAttrs
            );
        });
    }

    @Test
    public void confirmarPagoConMetodoPagoInvalido_DebeAgregarMensajeError_YRedirigirAMetodoPago() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "paypal", "1234567812345678", "Ana Lopez", "2026-12", "456",
                3, "azul", 10.0, 1.0, "algodon", "img.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Método de pago inválido.");
    }

    @Test
    public void registrarTelaDesdeCatalogoConIdNoExistenteAgregaMensajeError() {
        when(servicioMock.obtenerTelasDeFabrica()).thenReturn(Collections.emptyList());

        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        String resultado = controlador.registrarTelaDesdeCatalogo(999L, redirectAttrs);

        assertEquals("redirect:/catalogo-telas", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Tela no encontrada en el catálogo");
    }

    @Test
    public void procesarCompraConMetodoPagoValido_DebeRedirigirAMetodoPagoTela_YPasarDatos() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.procesarCompra("rojo", 12.0, 2.0, "algodon", "img.jpg", redirectAttrs);

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("color", "rojo");
        verify(redirectAttrs).addFlashAttribute("precio", 12.0);
        verify(redirectAttrs).addFlashAttribute("metros", 2.0);
        verify(redirectAttrs).addFlashAttribute("tipoTela", "algodon");
        verify(redirectAttrs).addFlashAttribute("imagenUrl", "img.jpg");
    }

    @Test
    public void procesarCompraConPrecioNegativo_DebeAgregarMensajeError_YRedirigirMetodoPago() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        // No podemos setear método de pago simulado
        String resultado = controlador.procesarCompra("rojo", -10.0, 2.0, "algodon", "img.jpg", redirectAttrs);

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Precio inválido.");
    }

    @Test
    public void confirmarPagoConMetodoDebito_DebeIgnorarCuotas_YAgregarTelaYMensajeExito_YRedirigirCarga() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "debito", "1111222233334444", "Carlos Soto", "2028-10", "789",
                3, "azul", 15.0, 3.0, "lino", "url.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        try {
            Field field = controlador.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<MisTelas> telas = (List<MisTelas>) field.get(controlador);

            Optional<MisTelas> ultimaTela = telas.stream()
                    .filter(t -> t.getColor().equals("azul") && t.getPrecio() == 15.0 && t.getMetros() == 3.0)
                    .findFirst();

            assertTrue(ultimaTela.isPresent());
        } catch (Exception e) {
            fail("Error accediendo a telasDelUsuario: " + e.getMessage());
        }

        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
        verify(redirectAttrs).addFlashAttribute("total", 15.0 * 3.0);
    }

    @Test
    public void confirmarPagoConCreditoY3Cuotas_DebeCalcularTotalEInteresCorrectamente_YAgregarFlashAttributes() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "credito", "2222333344445555", "Laura Diaz", "2027-09", "321",
                3, "rojo", 20.0, 2.0, "algodon", "url.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/boleta-tela", resultado);

        double totalEsperado = 20.0 * 2.0 * 1.08;
        double cuotaEsperada = totalEsperado / 3;

        verify(redirectAttrs).addFlashAttribute(eq("total"), doubleThat(v -> Math.abs(v - totalEsperado) < 0.01));
        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"), doubleThat(v -> Math.abs(v - cuotaEsperada) < 0.01));
        verify(redirectAttrs).addFlashAttribute("cuotas", 3);
    }

    @Test
    public void confirmarPagoConCuotasInvalidas_DebeRedirigirAMetodoPagoYMostrarError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "credito", "9999888877776666", "Marta Sosa", "2030-01", "999",
                0, "negro", 30.0, 1.0, "lino", "url.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Debe seleccionar una cantidad de cuotas válida.");
    }

    @Test
    public void confirmarPagoCreditoCon3Cuotas_DebeAgregarValoresCorrectosConInteres() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        controlador.confirmarPago(
                "credito", "4444333322221111", "Santiago Gil", "2029-11", "654",
                3, "rojo", 20.0, 2.0, "algodon", "img.jpg",
                redirectAttrs
        );

        double totalEsperado = 20.0 * 2.0 * 1.08;
        double cuotaEsperada = totalEsperado / 3;

        verify(redirectAttrs).addFlashAttribute(eq("total"), doubleThat(v -> Math.abs(v - totalEsperado) < 0.01));
        verify(redirectAttrs).addFlashAttribute(eq("valorCuota"), doubleThat(v -> Math.abs(v - cuotaEsperada) < 0.01));
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
    }

    @Test
    public void confirmarPagoDebitoConCuotasMayoresA1_DebeForzarUnaCuota() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        controlador.confirmarPago(
                "debito", "1111222233334444", "Luis Ramos", "2025-08", "111",
                6, "verde", 10.0, 5.0, "lino", "url.jpg",
                redirectAttrs
        );

        double total = 10.0 * 5.0;

        verify(redirectAttrs).addFlashAttribute("cuotas", 1);
        verify(redirectAttrs).addFlashAttribute("valorCuota", total);
        verify(redirectAttrs).addFlashAttribute("total", total);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");
    }

    @Test
    public void mostrarBoletaTela_DebeRetornarVistaBoleta() {
        String vista = controlador.mostrarBoleta();
        assertEquals("boleta-tela", vista);
    }

    @Test
    public void confirmarPagoConNumeroTarjetaInvalido_DebeAgregarMensajeError_YRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "debito", "1234", "Nombre Apellido", "12/25", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Número de tarjeta inválido.");
    }

    @Test
    public void confirmarPagoConNombreTitularVacio_DebeAgregarMensajeError_YRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "credito", "1234567812345678", "   ", "12/25", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "El nombre del titular es obligatorio.");
    }

    @Test
    public void confirmarPagoConFechaVencimientoInvalida_DebeAgregarMensajeError_YRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "debito", "1234567812345678", "Maria Gomez", "1225", "123",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "Fecha de vencimiento inválida.");
    }


    @Test
    public void confirmarPagoConCodigoSeguridadInvalido_DebeAgregarMensajeError_YRedirigir() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.confirmarPago(
                "credito", "1234567812345678", "Carlos Perez", "12/25", "12",
                1, "azul", 10.0, 2.0, "algodon", "img.jpg",
                redirectAttrs
        );

        assertEquals("redirect:/metodo-pago-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensajeError", "CVV inválido.");
    }

}


