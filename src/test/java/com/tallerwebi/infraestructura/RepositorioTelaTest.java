package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepositorioTelaTest {

    private SessionFactory sessionFactory;
    private Session session;
    private RepositorioTelaImpl repositorio;

    @BeforeEach
    void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);

        repositorio = new RepositorioTelaImpl();
        repositorio.sessionFactory = sessionFactory;
    }

    @Test
    void listarTelas_devuelveListaDeTelas() {
        Query<Tela> query = mock(Query.class);
        List<Tela> telas = List.of(new Tela());

        when(session.createQuery("FROM Tela", Tela.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(telas);

        List<Tela> resultado = repositorio.listarTelas();

        assertEquals(1, resultado.size());
        verify(session).createQuery("FROM Tela", Tela.class);
    }

    @Test
    void obtenerTela_porId_devuelveTela() {
        Tela tela = new Tela();
        when(session.get(Tela.class, 1L)).thenReturn(tela);

        Tela resultado = repositorio.obtenerTela(1L);

        assertEquals(tela, resultado);
        verify(session).get(Tela.class, 1L);
    }

    @Test
    void crearOActualizarTela_guardaTela() {
        Tela tela = new Tela();

        repositorio.crearOActualizarTela(tela);

        verify(session).saveOrUpdate(tela);
    }

    @Test
    void borrarTela_eliminaTela() {
        Tela tela = new Tela();

        repositorio.borrarTela(tela);

        verify(session).delete(tela);
    }

    @Test
    void listarTelasDeFabrica_excluyeTelaUsuario() {
        Query<Tela> query = mock(Query.class);
        List<Tela> telas = List.of(new Tela());

        String hql = "FROM com.tallerwebi.dominio.entidad.Tela t WHERE t.id NOT IN (SELECT tu.id FROM com.tallerwebi.dominio.entidad.TelaUsuario tu)";
        when(session.createQuery(hql, Tela.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(telas);

        List<Tela> resultado = repositorio.listarTelasDeFabrica();

        assertEquals(1, resultado.size());
        verify(session).createQuery(hql, Tela.class);
    }

    @Test
    void buscarTelaUsuarioPorTipoYColor_devuelveTelaExistente() {
        Usuario usuario = new Usuario();
        TipoTela tipo = TipoTela.ALGODON;
        String color = "Rojo";
        TelaUsuario tela = new TelaUsuario();

        Query<TelaUsuario> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(TelaUsuario.class))).thenReturn(query);
        when(query.setParameter("usuario", usuario)).thenReturn(query);
        when(query.setParameter("tipoTela", tipo)).thenReturn(query);
        when(query.setParameter("color", color)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(tela));

        TelaUsuario resultado = repositorio.buscarTelaUsuarioPorTipoYColor(usuario, tipo, color);

        assertEquals(tela, resultado);
    }

    @Test
    void buscarTelasUsuarioPorUsuarioYEstado_devuelveFiltrado() {
        Long usuarioId = 1L;
        EstadoTela estado = EstadoTela.EN_DEPOSITO;
        List<TelaUsuario> resultadoEsperado = List.of(new TelaUsuario());

        Query<TelaUsuario> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(TelaUsuario.class))).thenReturn(query);
        when(query.setParameter("usuarioId", usuarioId)).thenReturn(query);
        when(query.setParameter("estado", estado)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultadoEsperado);

        List<TelaUsuario> resultado = repositorio.buscarTelasUsuarioPorUsuarioYEstado(usuarioId, estado);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarTelasUsuarioPorUsuario_devuelveLista() {
        Long usuarioId = 1L;
        List<TelaUsuario> resultadoEsperado = List.of(new TelaUsuario());

        Query<TelaUsuario> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(TelaUsuario.class))).thenReturn(query);
        when(query.setParameter("usuarioId", usuarioId)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultadoEsperado);

        List<TelaUsuario> resultado = repositorio.buscarTelasUsuarioPorUsuario(usuarioId);

        assertEquals(1, resultado.size());
    }

    @Test
    void listarTelas_deberiaRetornarListaDeTelas() {
        String hql = "FROM Tela";
        List<Tela> telas = List.of(new Tela(), new Tela());

        Query<Tela> queryMock = mock(Query.class);
        when(session.createQuery(hql, Tela.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(telas);

        List<Tela> resultado = repositorio.listarTelas();

        assertEquals(2, resultado.size());
        verify(session).createQuery(hql, Tela.class);
    }

    @Test
    void obtenerTela_deberiaRetornarTelaPorId() {
        Tela telaMock = new Tela();
        when(session.get(Tela.class, 1L)).thenReturn(telaMock);

        Tela resultado = repositorio.obtenerTela(1L);

        assertEquals(telaMock, resultado);
        verify(session).get(Tela.class, 1L);
    }

    @Test
    void crearOActualizarTela_deberiaLlamarSaveOrUpdate() {
        Tela tela = new Tela();

        repositorio.crearOActualizarTela(tela);

        verify(session).saveOrUpdate(tela);
    }

    @Test
    void borrarTela_deberiaLlamarDelete() {
        Tela tela = new Tela();

        repositorio.borrarTela(tela);

        verify(session).delete(tela);
    }

    @Test
    void listarTelasDeFabrica_deberiaEjecutarQueryCorrecta() {
        String hql = "FROM com.tallerwebi.dominio.entidad.Tela t WHERE t.id NOT IN (SELECT tu.id FROM com.tallerwebi.dominio.entidad.TelaUsuario tu)";
        List<Tela> telas = List.of(new Tela());

        Query<Tela> queryMock = mock(Query.class);
        when(session.createQuery(hql, Tela.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(telas);

        List<Tela> resultado = repositorio.listarTelasDeFabrica();

        assertEquals(1, resultado.size());
        verify(session).createQuery(hql, Tela.class);
    }

    @Test
    void buscarTelaUsuarioPorTipoYColor_deberiaRetornarTelaSiExiste() {
        Usuario usuario = new Usuario();
        TelaUsuario telaUsuario = new TelaUsuario();
        String hql = "FROM TelaUsuario tu WHERE tu.usuario = :usuario AND tu.tipoTela = :tipoTela AND tu.color = :color";

        Query<TelaUsuario> queryMock = mock(Query.class);
        when(session.createQuery(hql, TelaUsuario.class)).thenReturn(queryMock);
        when(queryMock.setParameter("usuario", usuario)).thenReturn(queryMock);
        when(queryMock.setParameter("tipoTela", TipoTela.ALGODON)).thenReturn(queryMock);
        when(queryMock.setParameter("color", "Rojo")).thenReturn(queryMock);
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.of(telaUsuario));

        TelaUsuario resultado = repositorio.buscarTelaUsuarioPorTipoYColor(usuario, TipoTela.ALGODON, "Rojo");

        assertEquals(telaUsuario, resultado);
    }

    @Test
    void buscarTelasUsuarioPorUsuarioYEstado_deberiaRetornarTelasCorrectas() {
        String hql = "FROM TelaUsuario tu WHERE tu.usuario.id = :usuarioId AND tu.estado = :estado";
        List<TelaUsuario> lista = List.of(new TelaUsuario());

        Query<TelaUsuario> query = mock(Query.class);
        when(session.createQuery(hql, TelaUsuario.class)).thenReturn(query);
        when(query.setParameter("usuarioId", 1L)).thenReturn(query);
        when(query.setParameter("estado", EstadoTela.EN_DEPOSITO)).thenReturn(query);
        when(query.getResultList()).thenReturn(lista);

        List<TelaUsuario> resultado = repositorio.buscarTelasUsuarioPorUsuarioYEstado(1L, EstadoTela.EN_DEPOSITO);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarTelasUsuarioPorUsuario_deberiaRetornarTelasDelUsuario() {
        String hql = "FROM TelaUsuario tu WHERE tu.usuario.id = :usuarioId";
        List<TelaUsuario> lista = List.of(new TelaUsuario());

        Query<TelaUsuario> query = mock(Query.class);
        when(session.createQuery(hql, TelaUsuario.class)).thenReturn(query);
        when(query.setParameter("usuarioId", 1L)).thenReturn(query);
        when(query.getResultList()).thenReturn(lista);

        List<TelaUsuario> resultado = repositorio.buscarTelasUsuarioPorUsuario(1L);

        assertEquals(1, resultado.size());
    }


}

