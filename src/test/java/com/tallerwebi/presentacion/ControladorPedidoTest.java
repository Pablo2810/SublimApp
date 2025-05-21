package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.presentacion.controlador.ControladorPedido;
import com.tallerwebi.presentacion.dto.DatosPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPedidoTest {
    private DatosPedido datosPedidoMock;
    private ControladorPedido controladorPedido;
    private ServicioPedido servicioPedidoMock;
    private ServicioArchivo servicioArchivoMock;
    private Pedido pedidoMock;
    private Archivo archivoMock;
    private MockMultipartFile fileMock;

    @BeforeEach
    public void init(){
        datosPedidoMock = new DatosPedido("CamisetasDibuMartinez",30);
        pedidoMock = mock(Pedido.class);
        archivoMock = mock(Archivo.class);
        servicioPedidoMock = mock(ServicioPedido.class);
        servicioArchivoMock = mock(ServicioArchivo.class);
        controladorPedido = new ControladorPedido(servicioArchivoMock, servicioPedidoMock);
        fileMock = new MockMultipartFile("file", "camiseta.jpg", "image/jpeg", "datos".getBytes());
    }

    @Test
    public void queTeDevuelvaUnaVistaCuandoPedidoComoArchivoSeaValido() throws IOException {
        when(servicioArchivoMock.registrarArchivo(
                                            eq(datosPedidoMock.getNombre()),
                                            any(MultipartFile.class)))
        .thenReturn(archivoMock);

        when(servicioPedidoMock.registrarPedido(
                                            eq(datosPedidoMock.getCantidadCopias()),
                                            eq(archivoMock)))
        .thenReturn(pedidoMock);

        ModelAndView modelAndView = controladorPedido.procesarPedido(datosPedidoMock, fileMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalle-pedido"));
        assertEquals(pedidoMock, modelAndView.getModel().get("pedidoNuevo"));
    }

    @Test
    public void queDevuelvaUnErrorSiLaCantidadDeCopiasEsInvalida() throws IOException {
        datosPedidoMock.setCantidadCopias(0);
        ModelAndView modelAndView = controladorPedido.procesarPedido(datosPedidoMock, fileMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-pedido"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Ingrese la cantidad de copias"));
    }

    @Test
    public void queDevuelvaUnErrorSiElFileNoEsValido() throws IOException {
        MockMultipartFile fileNulo = null;
        ModelAndView modelAndView = controladorPedido.procesarPedido(datosPedidoMock, fileNulo);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-pedido"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Debe subir un archivo"));
    }

    @Test
    public void queDevuelvaUnErrorSiElFormatoNoEsJPEG() throws IOException {
        MockMultipartFile fileFormato = new MockMultipartFile("file", "camiseta.jpg", "image/png", "datos".getBytes());
        ModelAndView modelAndView = controladorPedido.procesarPedido(datosPedidoMock, fileFormato);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-pedido"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Ingrese un archivo valido (.JPG o .JPEG)"));
    }

}
