package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorTelaTest {

    private ControladorTela controladorTela;
    private ServicioTela servicioTelaMock;
    private Model modelMock;

    @BeforeEach
    public void init() {
        servicioTelaMock = mock(ServicioTela.class);
        controladorTela = new ControladorTela(servicioTelaMock);
        modelMock = mock(Model.class);
    }

    @Test
    public void queMuestreElCatalogoDeTelas() {
        List<MisTelas> telasCatalogo = new ArrayList<>();
        telasCatalogo.add(new MisTelas(1L, TipoTela.LINO, "Rojo", 100.0, "imagen.jpg"));

        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(telasCatalogo);

        // mostrarCatalogoTelas espera Model
        String vista = controladorTela.mostrarCatalogoTelas(modelMock);

        verify(modelMock).addAttribute("telas", telasCatalogo);
        assertEquals("catalogo-telas", vista);
    }

    @Test
    public void queMuestreElFormularioDeCarga() {
        // mostrarFormularioCarga espera Model
        String vista = controladorTela.mostrarFormularioCarga(modelMock);

        verify(modelMock).addAttribute(eq("telasUsuario"), anyList());
        verify(modelMock).addAttribute(eq("tiposTela"), eq(TipoTela.values()));
        assertEquals("cargar-tela", vista);
    }

    @Test
    public void queCargueUnaTelaCorrectamente() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // cargarTela espera RedirectAttributes
        String resultado = controladorTela.cargarTela("lino", "verde", "http://imagen.com/tela.jpg", redirectAttributes);

        assertEquals("redirect:/cargar-tela", resultado);
    }

    @Test
    public void queDevuelvaErrorSiTipoTelaEsInvalido() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String resultado = controladorTela.cargarTela("tipoInvalido", "rojo", "img.jpg", redirectAttributes);

        assertEquals("redirect:/cargar-tela", resultado);
        verify(redirectAttributes).addFlashAttribute("mensajeError", "Tipo de tela inválido");
    }

    @Test
    public void queSePuedaRegistrarUnaTelaDelCatalogo() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Azul", 50.0, "img.jpg");

        List<MisTelas> listaFabrica = List.of(tela);
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(listaFabrica);

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String vista = controladorTela.registrarTelaDesdeCatalogo(10L, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("mensaje", "Tela comprada con éxito");
        assertEquals("redirect:/catalogo-telas", vista);
    }

    @Test
    public void queNoAgregueTelaSiYaEstaComprada() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Blanco", 0.0, "img.jpg");
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(List.of(tela));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Se simula que ya fue comprada una vez
        controladorTela.registrarTelaDesdeCatalogo(10L, redirectAttributes);
        String vista = controladorTela.registrarTelaDesdeCatalogo(10L, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensajeAdvertencia"), eq("Ya has comprado esta tela"));
        assertEquals("redirect:/catalogo-telas", vista);
    }

    @Test
    public void queNoAgregueTelaSiElIdNoExisteEnElCatalogo() {
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(new ArrayList<>());

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String vista = controladorTela.registrarTelaDesdeCatalogo(999L, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensajeError"), eq("Tela no encontrada en el catálogo"));
        assertEquals("redirect:/catalogo-telas", vista);
    }

    @Test
    public void queAlCargarVariasTelasGenereIdsUnicos() {
        ServicioTela servicioMock = mock(ServicioTela.class);
        ControladorTela controlador = new ControladorTela(servicioMock);
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        controlador.cargarTela("lino", "rojo", "url1", redirectAttrs);
        controlador.cargarTela("algodon", "azul", "url2", redirectAttrs);
        controlador.cargarTela("lino", "verde", "url3", redirectAttrs);

        try {
            java.lang.reflect.Field field = controlador.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<?> telas = (List<?>) field.get(controlador);

            assertEquals(3, telas.size());

            long idPrevio = -1;
            for (Object obj : telas) {
                MisTelas tela = (MisTelas) obj;
                long idActual = tela.getId();
                assertTrue(idActual >= 1000);
                assertTrue(idActual > idPrevio);
                idPrevio = idActual;
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("No se pudo acceder a telasDelUsuario");
        }
    }

    @Test
    public void queCargueTelaConColorYUrlVaciosNoRompa() {
        ServicioTela servicioMock = mock(ServicioTela.class);
        ControladorTela controlador = new ControladorTela(servicioMock);
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controlador.cargarTela("lino", "", "", redirectAttrs);
        assertEquals("redirect:/cargar-tela", resultado);
    }

    @Test
    public void queMostrarFormularioCargaMuestraLaListaInterna() {
        ServicioTela servicioMock = mock(ServicioTela.class);
        ControladorTela controlador = new ControladorTela(servicioMock);
        Model modelMock = mock(Model.class);

        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);
        controlador.cargarTela("algodon", "rojo", "url", redirectAttrs);

        controlador.mostrarFormularioCarga(modelMock);

        try {
            java.lang.reflect.Field field = controlador.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<MisTelas> telasInternas = (List<MisTelas>) field.get(controlador);

            verify(modelMock).addAttribute("telasUsuario", telasInternas);
        } catch (Exception e) {
            fail("No se pudo acceder a telasDelUsuario");
        }
    }

    @Test
    public void queProcesarCompraAgregaTelaYRedirigeConMensaje() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controladorTela.procesarCompra(
                "azul",             // color
                20.0,               // precio
                3.5,                // metros
                "algodon",          // tipoTela (como String)
                "/img/tela_azul.jpg", // imagenUrl
                redirectAttrs
        );

        assertEquals("redirect:/cargar-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("mensaje", "Compra realizada con éxito");

        try {
            java.lang.reflect.Field field = controladorTela.getClass().getDeclaredField("telasDelUsuario");
            field.setAccessible(true);
            List<MisTelas> telas = (List<MisTelas>) field.get(controladorTela);

            assertFalse(telas.isEmpty());
            MisTelas ultimaTela = telas.get(telas.size() - 1);
            assertEquals(TipoTela.ALGODON, ultimaTela.getTipoTela());
            assertEquals("azul", ultimaTela.getColor());
            assertEquals(20.0, ultimaTela.getPrecio());
            assertEquals(3.5, ultimaTela.getMetros());
            assertEquals("/img/tela_azul.jpg", ultimaTela.getImagenUrl());
        } catch (Exception e) {
            fail("No se pudo acceder a telasDelUsuario");
        }
    }


    @Test
    public void queProcesarCompraConTipoTelaInvalidoRedirigeConError() {
        RedirectAttributes redirectAttrs = mock(RedirectAttributes.class);

        String resultado = controladorTela.procesarCompra(
                "rojo",               // color
                10.0,                 // precio
                1.0,                  // metros
                "tipoInvalido",       // tipoTela
                "/img/tela_roja.jpg", // imagenUrl
                redirectAttrs
        );

        assertEquals("redirect:/cargar-tela", resultado);
        verify(redirectAttrs).addFlashAttribute("error", "Tipo de tela inválido");
    }

}


