package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorTela;
import com.tallerwebi.presentacion.dto.MisTelas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        String vista = controladorTela.mostrarCatalogoTelas(modelMock);

        verify(modelMock).addAttribute("telas", telasCatalogo);
        assertEquals("catalogo-telas", vista);
    }

    @Test
    public void queMuestreElFormularioDeCarga() {
        String vista = controladorTela.mostrarFormularioCarga(modelMock);

        verify(modelMock).addAttribute(eq("telasUsuario"), anyList());
        verify(modelMock).addAttribute(eq("tiposTela"), eq(TipoTela.values()));
        assertEquals("cargar-tela", vista);
    }

    @Test
    public void queCargueUnaTelaCorrectamente() {
        String resultado = controladorTela.cargarTela("lino", "verde", "http://imagen.com/tela.jpg");

        assertEquals("redirect:/mis-telas/cargar-tela", resultado);
    }

    @Test
    public void queDevuelvaErrorSiTipoTelaEsInvalido() {
        String resultado = controladorTela.cargarTela("tipoInvalido", "rojo", "img.jpg");

        assertEquals("redirect:/mis-telas/cargar?error=tipoInvalido", resultado);
    }

    @Test
    public void queSePuedaRegistrarUnaTelaDelCatalogo() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Azul", 50.0, "img.jpg");

        List<MisTelas> listaFabrica = List.of(tela);
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(listaFabrica);

        String vista = controladorTela.registrarTelaDesdeCatalogo(10L);

        assertEquals("redirect:/mis-telas/cargar-tela", vista);
    }

    @Test
    public void queNoAgregueTelaSiYaEstaComprada() {
        MisTelas tela = new MisTelas(10L, TipoTela.ALGODON, "Blanco", 0.0, "img.jpg");
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(List.of(tela));

        // Forzar que ya esté agregada manualmente
        controladorTela.cargarTela("algodon", "Blanco", "img.jpg");

        // Intentar registrarla desde catálogo
        String vista = controladorTela.registrarTelaDesdeCatalogo(10L);

        assertEquals("redirect:/mis-telas/cargar-tela", vista);
    }

    @Test
    public void queNoAgregueTelaSiElIdNoExisteEnElCatalogo() {
        when(servicioTelaMock.obtenerTelasDeFabrica()).thenReturn(new ArrayList<>()); // catálogo vacío

        String vista = controladorTela.registrarTelaDesdeCatalogo(999L);

        assertEquals("redirect:/mis-telas/cargar-tela", vista);
    }

    @Test
    public void queNoCargueTelaSiElTipoEsInvalido() {
        String vista = controladorTela.cargarTela("plastico", "Verde", "http://imagen.jpg");

        assertEquals("redirect:/mis-telas/cargar?error=tipoInvalido", vista);
    }
}

