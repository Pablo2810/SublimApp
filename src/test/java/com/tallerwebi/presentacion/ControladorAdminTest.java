package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.controlador.ControladorAdmin;
import com.tallerwebi.presentacion.dto.DatosTela;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorAdminTest {

    private ControladorAdmin controladorAdmin;
    private ServicioTela servicioTela;
    private ServicioTalle servicioTalle;
    private ServicioPedido servicioPedido;
    private RedirectAttributes redirectAttributes;


    @BeforeEach
    void init() {
        servicioTela = mock(ServicioTela.class);
        redirectAttributes = mock(RedirectAttributes.class);
        controladorAdmin = new ControladorAdmin(servicioTela, servicioTalle, servicioPedido);
    }

    @Test
    void listarTelasCasoExito() {
        Tela tela1 = new Tela();
        tela1.setId(1L);
        tela1.setNombre("Algodon");

        when(servicioTela.obtenerTelas()).thenReturn(List.of(tela1));

        ModelAndView resultado = controladorAdmin.listarTelas();

        List<Tela> telas = (List<Tela>) resultado.getModel().get("telas");

        assertNotNull(resultado.getModel().get("telas"));
        assertEquals(1, telas.size());
        assertNull(resultado.getModel().get("mensajeSinTelas"));
    }

    @Test
    void listarTelasLanzaExceptionYNoTraeTelas() {
        when(servicioTela.obtenerTelas()).thenThrow(new RuntimeException());

        ModelAndView resultado = controladorAdmin.listarTelas();

        assertEquals("No hay telas de fabrica registradas", resultado.getModel().get("mensajeSinTelas"));
        assertNull(resultado.getModel().get("telas"));
    }

    @Test
    void listarTelasNoTraeResultados() {
        when(servicioTela.obtenerTelas()).thenReturn(Collections.emptyList());

        ModelAndView resultado = controladorAdmin.listarTelas();

        assertEquals("No hay telas de fabrica registradas", resultado.getModel().get("mensajeSinTelas"));
        assertNull(resultado.getModel().get("telas"));
    }

    @Test
    void crearTelaCasoExito() {
        DatosTela dto = new DatosTela();
        dto.setColor("Gris");
        dto.setMetros(100.0);
        dto.setPrecio(3200.0);
        MultipartFile archivoMock = mock(MultipartFile.class);

        doNothing().when(servicioTela).crearOActualizar(dto, archivoMock);

        controladorAdmin.editarOCrearTela(null, dto, archivoMock, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensaje"), contains("Se creo la tela con exito"));
    }

    @Test
    void editarTelaCasoExito() {
        DatosTela dto = new DatosTela();
        dto.setId(1L);
        dto.setColor("Gris");
        dto.setMetros(100.0);
        dto.setPrecio(3200.0);
        MultipartFile archivoMock = mock(MultipartFile.class);

        doNothing().when(servicioTela).crearOActualizar(dto, archivoMock);

        controladorAdmin.editarOCrearTela(1L, dto, archivoMock, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensaje"), contains("Se edito la tela"));
    }

    @Test
    void editarTelaNoGuardaLaTelaSiAlSubirImagenALaNubeLanzaExcepcion() {
        DatosTela dto = new DatosTela();
        dto.setId(1L);
        dto.setColor("Gris");
        dto.setMetros(100.0);
        dto.setPrecio(3200.0);
        MultipartFile archivoMock = mock(MultipartFile.class);

        doThrow(IllegalArgumentException.class).when(servicioTela).crearOActualizar(dto, archivoMock);

        controladorAdmin.editarOCrearTela(1L, dto, archivoMock, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensajeAdvertencia"), contains("No se pudo subir la imagen a la nube"));
    }

    @Test
    void editarTelaNoGuardaLaTelaSiSeLanzaExcepcionAlGuardarOActualizar() {
        DatosTela dto = new DatosTela();
        dto.setId(1L);
        dto.setColor("Gris");
        dto.setMetros(100.0);
        dto.setPrecio(3200.0);
        MultipartFile archivoMock = mock(MultipartFile.class);

        doThrow(RuntimeException.class).when(servicioTela).crearOActualizar(dto, archivoMock);

        controladorAdmin.editarOCrearTela(1L, dto, archivoMock, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensajeError"), contains("Ocurrio un error al crear o actualizar la tela"));
    }

}
