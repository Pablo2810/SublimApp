package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.CompraTela;
import com.tallerwebi.dominio.entidad.EstadoTela;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

public class RepositorioCompraTelaTest {

    private SessionFactory sessionFactory;
    private Session session;
    private RepositorioCompraTelaImpl repositorio;

    @BeforeEach
    void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        repositorio = new RepositorioCompraTelaImpl(sessionFactory);
    }

    @Test
    void guardarTela_deberiaPersistirCompra() {
        CompraTela compra = new CompraTela();

        repositorio.guardarTela(compra);

        verify(session).persist(compra);
        verify(session).flush();
    }

    @Test
    void obtenerCompraPorId_deberiaRetornarCompra() {
        Long id = 1L;
        CompraTela compraMock = new CompraTela();

        when(session.get(CompraTela.class, id)).thenReturn(compraMock);

        CompraTela resultado = repositorio.obtenerCompraPorId(id);

        assertEquals(compraMock, resultado);
        verify(session).get(CompraTela.class, id);
    }

    @Test
    void eliminarCompra_deberiaEliminarCompraSiExiste() {
        CompraTela compra = new CompraTela();
        compra.setId(1L);

        CompraTela compraGestionada = new CompraTela();
        when(session.get(CompraTela.class, compra.getId())).thenReturn(compraGestionada);

        repositorio.eliminarCompra(compra);

        verify(session).get(CompraTela.class, compra.getId());
        verify(session).delete(compraGestionada);
        verify(session).flush();
    }

    @Test
    void eliminarCompra_noHaceNadaSiCompraNoExiste() {
        CompraTela compra = new CompraTela();
        compra.setId(1L);

        when(session.get(CompraTela.class, compra.getId())).thenReturn(null);

        repositorio.eliminarCompra(compra);

        verify(session).get(CompraTela.class, compra.getId());
        verify(session, never()).delete(any());
        verify(session, never()).flush();
    }

    @Test
    void buscarComprasPorUsuarioYEstado_conEstado_deberiaEjecutarConsulta() {
        Long usuarioId = 1L;
        EstadoTela estado = EstadoTela.EN_DEPOSITO;

        Query<CompraTela> queryMock = mock(Query.class);

        when(session.createQuery(anyString(), eq(CompraTela.class))).thenReturn(queryMock);
        when(queryMock.setParameter(eq("usuarioId"), eq(usuarioId))).thenReturn(queryMock);
        when(queryMock.setParameter(eq("estado"), eq(estado))).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        List<CompraTela> resultado = repositorio.buscarComprasPorUsuarioYEstado(usuarioId, estado);

        assertNotNull(resultado);
        verify(session).createQuery(contains("AND c.estado = :estado"), eq(CompraTela.class));
        verify(queryMock).setParameter("usuarioId", usuarioId);
        verify(queryMock).setParameter("estado", estado);
        verify(queryMock).getResultList();
    }

    @Test
    void buscarComprasPorUsuarioYEstado_sinEstado_deberiaEjecutarConsulta() {
        Long usuarioId = 1L;

        Query<CompraTela> queryMock = mock(Query.class);

        when(session.createQuery(anyString(), eq(CompraTela.class))).thenReturn(queryMock);
        when(queryMock.setParameter(eq("usuarioId"), eq(usuarioId))).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        List<CompraTela> resultado = repositorio.buscarComprasPorUsuarioYEstado(usuarioId, null);

        assertNotNull(resultado);
        verify(session).createQuery(not(contains("AND c.estado = :estado")), eq(CompraTela.class));
        verify(queryMock).setParameter("usuarioId", usuarioId);
        verify(queryMock, never()).setParameter(eq("estado"), any());
        verify(queryMock).getResultList();
    }
}


