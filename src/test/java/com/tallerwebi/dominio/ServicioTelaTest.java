package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.repositorio.RepositorioCompraTela;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import io.imagekit.sdk.models.results.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioTelaTest {

    @Mock
    private RepositorioTela repositorioTela;

    @Mock
    private RepositorioCompraTela repositorioCompraTela;

    @Mock
    private ServicioStorageImagen servicioStorageImagen;

    @InjectMocks
    private ServicioTelaImpl servicioTela;

    @BeforeEach
    void setUp() {
        repositorioTela = mock(RepositorioTela.class);
        servicioStorageImagen = mock(ServicioStorageImagen.class);
        repositorioCompraTela = mock(RepositorioCompraTela.class);
        servicioTela = new ServicioTelaImpl(repositorioTela, servicioStorageImagen);
        ReflectionTestUtils.setField(servicioTela, "repositorioCompraTela", repositorioCompraTela);
    }

    @Test
    void obtenerTelas_delegaEnRepositorio() {
        List<Tela> listaEsperada = List.of(new Tela(), new Tela());
        when(repositorioTela.listarTelas()).thenReturn(listaEsperada);

        List<Tela> resultado = servicioTela.obtenerTelas();

        assertEquals(listaEsperada, resultado);
        verify(repositorioTela).listarTelas();
    }

    @Test
    void obtenerTela_delegaEnRepositorio() {
        Tela tela = new Tela();
        when(repositorioTela.obtenerTela(1L)).thenReturn(tela);

        Tela resultado = servicioTela.obtenerTela(1L);

        assertEquals(tela, resultado);
        verify(repositorioTela).obtenerTela(1L);
    }

    @Test
    void crearOActualizar_creaTelaNueva_conArchivo() {
        DatosTela datos = new DatosTela();
        datos.setId(null);
        datos.setColor("Rojo");
        datos.setTipoTela(TipoTela.ALGODON);
        datos.setPrecio(100.0);
        datos.setMetros(10.0);

        MockMultipartFile archivo = new MockMultipartFile("file", "tela-algodon-roja.jpg", "image/jpeg", "contenido".getBytes());

        Result resultadoImagen = new Result();
        resultadoImagen.setUrl("urlDeImagen");
        when(servicioStorageImagen.subirImagen(any(MultipartFile.class), eq("telas"), anyString())).thenReturn(resultadoImagen);

        servicioTela.crearOActualizar(datos, archivo);

        ArgumentCaptor<Tela> captor = ArgumentCaptor.forClass(Tela.class);
        verify(repositorioTela).crearOActualizarTela(captor.capture());
        verify(servicioStorageImagen).subirImagen(archivo, "telas", "tela-rojo");


        Tela telaGuardada = captor.getValue();
        assertEquals("Rojo", telaGuardada.getColor());
        assertEquals(TipoTela.ALGODON, telaGuardada.getTipoTela());
        assertEquals(100.0, telaGuardada.getPrecio());
        assertEquals(10.0, telaGuardada.getMetros());
        assertEquals("urlDeImagen", telaGuardada.getImagenUrl());
    }

    @Test
    void crearOActualizar_actualizaTelaExistente_sinArchivo() {
        DatosTela datos = new DatosTela();
        datos.setId(1L);
        datos.setColor("Azul");
        datos.setTipoTela(TipoTela.LINO);
        datos.setPrecio(50.0);
        datos.setMetros(5.0);

        Tela telaExistente = new Tela();
        telaExistente.setId(1L);
        telaExistente.setImagenUrl("urlVieja");

        when(servicioTela.obtenerTela(1L)).thenReturn(telaExistente);

        MockMultipartFile archivoVacio = new MockMultipartFile("file", new byte[0]);

        servicioTela.crearOActualizar(datos, archivoVacio);

        ArgumentCaptor<Tela> captor = ArgumentCaptor.forClass(Tela.class);
        verify(repositorioTela).crearOActualizarTela(captor.capture());

        Tela telaGuardada = captor.getValue();
        assertEquals("Azul", telaGuardada.getColor());
        assertEquals(TipoTela.LINO, telaGuardada.getTipoTela());
        assertEquals(50.0, telaGuardada.getPrecio());
        assertEquals(5.0, telaGuardada.getMetros());
        // La url debe ser la misma porque no se actualizó la imagen
        assertEquals("urlVieja", telaGuardada.getImagenUrl());
    }

    @Test
    void borrarTela_eliminaTelaExistente() {
        Tela tela = new Tela();
        tela.setId(1L);
        when(repositorioTela.obtenerTela(1L)).thenReturn(tela);

        servicioTela.borrarTela(1L);

        verify(repositorioTela).borrarTela(tela);
    }

    @Test
    void borrarTela_telaNoExiste_lanzaRuntimeException() {
        when(repositorioTela.obtenerTela(2L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> servicioTela.borrarTela(2L));
    }

    @Test
    void obtenerTelasDeFabrica_devuelveMisTelas() {
        Tela t1 = new Tela();
        t1.setId(1L);
        t1.setTipoTela(TipoTela.ALGODON);
        t1.setColor("Rojo");
        t1.setPrecio(100.0);
        t1.setMetros(5.0);
        t1.setImagenUrl("url1");

        Tela t2 = new Tela();
        t2.setId(2L);
        t2.setTipoTela(TipoTela.LINO);
        t2.setColor("Azul");
        t2.setPrecio(150.0);
        t2.setMetros(8.0);
        t2.setImagenUrl("url2");

        when(repositorioTela.listarTelasDeFabrica()).thenReturn(List.of(t1, t2));

        List<MisTelas> resultado = servicioTela.obtenerTelasDeFabrica();

        assertEquals(2, resultado.size());
        assertEquals("Rojo", resultado.get(0).getColor());
        assertEquals(8.0, resultado.get(1).getMetros());
    }

    @Test
    void comprarTelaDeFabrica_telaNoEncontrada_lanzaExcepcion() {
        when(repositorioTela.obtenerTela(1L)).thenReturn(null);

        assertThrows(TelaNoEncontrada.class, () -> servicioTela.comprarTelaDeFabrica(1L, 5.0, new Usuario()));
    }

    @Test
    void comprarTelaDeFabrica_stockInsuficiente_lanzaExcepcion() {
        Tela tela = new Tela();
        tela.setMetros(3.0);
        when(repositorioTela.obtenerTela(1L)).thenReturn(tela);

        assertThrows(StockInsuficiente.class, () -> servicioTela.comprarTelaDeFabrica(1L, 5.0, new Usuario()));
    }

    @Test
    void comprarTelaDeFabrica_usuarioTieneTelaExistente_sumaMetros() throws Exception {
        Tela tela = new Tela();
        tela.setMetros(10.0);
        tela.setTipoTela(TipoTela.ALGODON);
        tela.setColor("Rojo");
        tela.setPrecio(100.0);
        tela.setImagenUrl("urlImg");

        Usuario usuario = new Usuario();

        TelaUsuario telaExistente = new TelaUsuario();
        telaExistente.setMetros(2.0);
        telaExistente.setTipoTela(TipoTela.ALGODON);
        telaExistente.setColor("Rojo");

        when(repositorioTela.obtenerTela(1L)).thenReturn(tela);
        when(repositorioTela.buscarTelaUsuarioPorTipoYColor(usuario, TipoTela.ALGODON, "Rojo")).thenReturn(telaExistente);

        servicioTela.comprarTelaDeFabrica(1L, 5.0, usuario);

        assertEquals(7.0, telaExistente.getMetros() - 2.0 + 2.0); // simple check

        verify(repositorioTela).crearOActualizarTela(tela);
        verify(repositorioTela).crearOActualizarTela(telaExistente);
    }

    @Test
    void comprarTelaDeFabrica_usuarioNoTieneTela_creaNuevaTelaUsuario() throws Exception {
        Tela tela = new Tela();
        tela.setMetros(10.0);
        tela.setTipoTela(TipoTela.LINO);
        tela.setColor("Azul");
        tela.setPrecio(200.0);
        tela.setImagenUrl("urlImg");

        Usuario usuario = new Usuario();

        when(repositorioTela.obtenerTela(1L)).thenReturn(tela);
        when(repositorioTela.buscarTelaUsuarioPorTipoYColor(usuario, TipoTela.LINO, "Azul")).thenReturn(null);

        servicioTela.comprarTelaDeFabrica(1L, 5.0, usuario);

        ArgumentCaptor<TelaUsuario> captor = ArgumentCaptor.forClass(TelaUsuario.class);
        verify(repositorioTela).crearOActualizarTela(tela);
        verify(repositorioTela).crearOActualizarTela(captor.capture());

        TelaUsuario creada = captor.getValue();
        assertEquals(TipoTela.LINO, creada.getTipoTela());
        assertEquals("Azul", creada.getColor());
        assertEquals(5.0, creada.getMetros());
        assertEquals(EstadoTela.EN_DEPOSITO, creada.getEstado());
        assertEquals(usuario, creada.getUsuario());
    }

    @Test
    void consumirTelaParaProducto_stockSuficiente_descuentaMetros() throws Exception {
        Tela tela = new Tela();
        tela.setMetros(10.0);

        servicioTela.consumirTelaParaProducto(tela, 5.0, new Usuario());

        assertEquals(5.0, tela.getMetros());
        verify(repositorioTela).crearOActualizarTela(tela);
    }

    @Test
    void consumirTelaParaProducto_stockInsuficiente_lanzaExcepcion() {
        Tela tela = new Tela();
        tela.setMetros(3.0);

        assertThrows(StockInsuficiente.class, () -> {
            servicioTela.consumirTelaParaProducto(tela, 5.0, new Usuario());
        });
    }

    @Test
    void obtenerTelasUsuarioPorEstado_delegaEnRepositorio() {
        Long usuarioId = 1L;
        EstadoTela estado = EstadoTela.EN_DEPOSITO;
        List<TelaUsuario> lista = new ArrayList<>();
        when(repositorioTela.buscarTelasUsuarioPorUsuarioYEstado(usuarioId, estado)).thenReturn(lista);

        List<TelaUsuario> resultado = servicioTela.obtenerTelasUsuarioPorEstado(usuarioId, estado);

        assertSame(lista, resultado);
        verify(repositorioTela).buscarTelasUsuarioPorUsuarioYEstado(usuarioId, estado);
    }

    @Test
    void obtenerTelasUsuario_delegaEnRepositorio() {
        Long usuarioId = 2L;
        List<TelaUsuario> lista = new ArrayList<>();
        when(repositorioTela.buscarTelasUsuarioPorUsuario(usuarioId)).thenReturn(lista);

        List<TelaUsuario> resultado = servicioTela.obtenerTelasUsuario(usuarioId);

        assertSame(lista, resultado);
        verify(repositorioTela).buscarTelasUsuarioPorUsuario(usuarioId);
    }

    @Test
    void cambiarEstadoTela_telaUsuario_existente_cambiaEstado() throws Exception {
        TelaUsuario telaUsuario = new TelaUsuario();
        telaUsuario.setEstado(EstadoTela.EN_DEPOSITO);

        when(repositorioTela.obtenerTela(1L)).thenReturn(telaUsuario);

        servicioTela.cambiarEstadoTela(1L, EstadoTela.EN_VIAJE);

        assertEquals(EstadoTela.EN_VIAJE, telaUsuario.getEstado());
        verify(repositorioTela).crearOActualizarTela(telaUsuario);
    }

    @Test
    void cambiarEstadoTela_noEsTelaUsuario_lanzaExcepcion() {
        Tela telaNormal = new Tela();
        when(repositorioTela.obtenerTela(1L)).thenReturn(telaNormal);

        assertThrows(TelaUsuarioNoEncontrada.class, () -> servicioTela.cambiarEstadoTela(1L, EstadoTela.EN_VIAJE));
    }

    @Test
    void obtenerTelasParaCarrusel_filtroCorrecto() {
        MisTelas t1 = new MisTelas(1L, TipoTela.LINO, "azul", 100.0, "url1");
        MisTelas t2 = new MisTelas(2L, TipoTela.W15, "blanco", 50.0, "url2");
        MisTelas t3 = new MisTelas(3L, TipoTela.NEOPRENO, "marron", 70.0, "url3");
        MisTelas t4 = new MisTelas(4L, TipoTela.LINO, "negro", 20.0, "url4"); // no está en filtro

        ServicioTelaImpl spyServicio = spy(servicioTela);
        doReturn(List.of(t1, t2, t3, t4)).when(spyServicio).obtenerTelasDeFabrica();

        List<MisTelas> resultado = spyServicio.obtenerTelasParaCarrusel();

        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(t1));
        assertTrue(resultado.contains(t2));
        assertTrue(resultado.contains(t3));
        assertFalse(resultado.contains(t4));
    }

    @Test
    void registrarCompraTela_registraCompraCorrectamente() {
        Long idTela = 1L;
        Usuario usuario = new Usuario();
        Double metros = 3.0;
        String metodoPago = "credito";
        Integer cuotas = 3;
        boolean pagoEnDolares = true;
        double cotizacionDolar = 1000.0;
        TipoEnvio envio = TipoEnvio.CABA;

        Tela tela = new Tela();
        tela.setId(idTela);
        tela.setPrecio(200.0);

        when(repositorioTela.obtenerTela(idTela)).thenReturn(tela);

        servicioTela.registrarCompraTela(idTela, usuario, metros, metodoPago, cuotas, pagoEnDolares, cotizacionDolar, envio);

        ArgumentCaptor<CompraTela> captor = ArgumentCaptor.forClass(CompraTela.class);
        verify(repositorioCompraTela).guardarTela(captor.capture());

        CompraTela compraGuardada = captor.getValue();
        assertEquals(usuario, compraGuardada.getUsuario());
        assertEquals(tela, compraGuardada.getTela());
        assertEquals(metros, compraGuardada.getMetros());
        assertEquals(200.0, compraGuardada.getPrecioUnitario());
        assertEquals(200.0 * metros + envio.getCosto(), compraGuardada.getTotalPagado());
        assertEquals(metodoPago, compraGuardada.getMetodoPago());
        assertEquals(cuotas, compraGuardada.getCuotas());
        assertTrue(compraGuardada.isPagoEnDolares());
        assertEquals(cotizacionDolar, compraGuardada.getCotizacionDolar());
        assertEquals(envio.getDescripcion(), compraGuardada.getDescripcionEnvio());
        assertEquals(envio.getCosto(), compraGuardada.getCostoEnvio());
        assertEquals(envio.getTiempoEntrega(), compraGuardada.getTiempoEntrega());
    }

    @Test
    void registrarCompraTela_telaNoEncontrada_lanzaExcepcion() {
        Long idTela = 99L;
        when(repositorioTela.obtenerTela(idTela)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                servicioTela.registrarCompraTela(
                        idTela,
                        new Usuario(),
                        5.0,
                        "credito",
                        3,
                        false,
                        0.0,
                        TipoEnvio.INTERIOR
                )
        );

        verify(repositorioCompraTela, never()).guardarTela(any());
    }

    @Test
    void cancelarCompra_compraNoEncontrada_lanzaExcepcion() {
        when(repositorioCompraTela.obtenerCompraPorId(1L)).thenReturn(null);

        assertThrows(CompraTelaNoEncontrada.class, () -> {
            servicioTela.cancelarCompraTela(1L, new Usuario());
        });
    }

    @Test
    void cancelarCompra_usuarioDistinto_lanzaExcepcion() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);

        Usuario usuarioActual = new Usuario();
        usuarioActual.setId(1L);

        CompraTela compra = new CompraTela();
        compra.setUsuario(otroUsuario);
        compra.setEstado(EstadoTela.EN_DEPOSITO);

        when(repositorioCompraTela.obtenerCompraPorId(1L)).thenReturn(compra);

        assertThrows(CancelacionNoPermitida.class, () -> {
            servicioTela.cancelarCompraTela(1L, usuarioActual);
        });
    }

    @Test
    void cancelarCompra_estadoIncorrecto_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        CompraTela compra = new CompraTela();
        compra.setUsuario(usuario);
        compra.setEstado(EstadoTela.EN_VIAJE); // Estado no válido para cancelación

        when(repositorioCompraTela.obtenerCompraPorId(1L)).thenReturn(compra);

        assertThrows(CancelacionNoPermitida.class, () -> {
            servicioTela.cancelarCompraTela(1L, usuario);
        });
    }

    @Test
    void cancelarCompra_valida_restauraStockYEliminaCompra() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Tela tela = new Tela();
        tela.setMetros(10.0);

        CompraTela compra = new CompraTela();
        compra.setId(1L);
        compra.setUsuario(usuario);
        compra.setEstado(EstadoTela.EN_DEPOSITO);
        compra.setMetros(5.0);
        compra.setTela(tela);

        when(repositorioCompraTela.obtenerCompraPorId(1L)).thenReturn(compra);

        servicioTela.cancelarCompraTela(1L, usuario);

        assertEquals(15.0, tela.getMetros());
        verify(repositorioTela).crearOActualizarTela(tela);
        verify(repositorioCompraTela).eliminarCompra(compra);
    }

    @Test
    void crearOActualizar_conArchivoVacio_noActualizaImagen() {
        DatosTela datos = new DatosTela();
        datos.setId(null);
        datos.setColor("Negro");
        datos.setTipoTela(TipoTela.LINO);
        datos.setPrecio(120.0);
        datos.setMetros(15.0);

        MockMultipartFile archivoVacio = new MockMultipartFile("file", new byte[0]);

        servicioTela.crearOActualizar(datos, archivoVacio);

        ArgumentCaptor<Tela> captor = ArgumentCaptor.forClass(Tela.class);
        verify(repositorioTela).crearOActualizarTela(captor.capture());

        Tela telaGuardada = captor.getValue();
        assertEquals("Negro", telaGuardada.getColor());
        assertEquals(TipoTela.LINO, telaGuardada.getTipoTela());
        assertEquals(120.0, telaGuardada.getPrecio());
        assertEquals(15.0, telaGuardada.getMetros());
        assertNull(telaGuardada.getImagenUrl(), "No debe setear url de imagen si archivo vacío");
    }

    @Test
    void crearOActualizar_conIdInexistente_lanzaRuntimeException() {
        DatosTela datos = new DatosTela();
        datos.setId(999L); // ID inexistente

        when(repositorioTela.obtenerTela(999L)).thenThrow(new RuntimeException());

        MockMultipartFile archivoVacio = new MockMultipartFile("file", new byte[0]);

        assertThrows(RuntimeException.class, () -> {
            servicioTela.crearOActualizar(datos, archivoVacio);
        });
    }

    @Test
    void borrarTela_excepcionEnObtenerTela_lanzaRuntimeException() {
        when(repositorioTela.obtenerTela(10L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> servicioTela.borrarTela(10L));
    }

    @Test
    void cambiarEstadoTela_telaNoEncontrada_lanzaTelaUsuarioNoEncontrada() {
        when(repositorioTela.obtenerTela(123L)).thenReturn(new Tela()); // no es TelaUsuario

        assertThrows(TelaUsuarioNoEncontrada.class, () -> {
            servicioTela.cambiarEstadoTela(123L, EstadoTela.EN_VIAJE);
        });
    }

    @Test
    void obtenerComprasDeTelasPorUsuarioYEstado_delegacionAlRepositorio() {
        Long usuarioId = 5L;
        EstadoTela estado = EstadoTela.EN_DEPOSITO;
        List<CompraTela> compras = new ArrayList<>();

        when(repositorioCompraTela.buscarComprasPorUsuarioYEstado(usuarioId, estado)).thenReturn(compras);

        List<CompraTela> resultado = servicioTela.obtenerComprasDeTelasPorUsuarioYEstado(usuarioId, estado);

        assertSame(compras, resultado);
        verify(repositorioCompraTela).buscarComprasPorUsuarioYEstado(usuarioId, estado);
    }

}
