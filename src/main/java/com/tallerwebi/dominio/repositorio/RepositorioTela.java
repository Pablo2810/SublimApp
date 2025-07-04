package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.*;

import java.util.List;

public interface RepositorioTela {
    List<Tela> listarTelas();
    Tela obtenerTela(Long id);
    void crearOActualizarTela(Tela tela);
    void borrarTela(Tela id);
    void guardarTelaFabrica();
    List<Tela> buscarTelasDePrendaPorIdPrenda(Long id);
    List<Tela> listarTelasDeFabrica();
    List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle);
    List<TelaUsuario> obtenerTelasPorUsuario(Usuario usuario);

    TelaUsuario buscarTelaUsuarioPorTipoYColor(Usuario usuario, TipoTela tipoTela, String color);

    TelaUsuario obtenerTelaUsuarioPorId(Long id);

    void guardarTelaUsuario(TelaUsuario telaUsuario);

    List<TelaUsuario> buscarTelasUsuarioPorUsuarioYEstado(Long usuarioId, EstadoTela estado);

    List<TelaUsuario> buscarTelasUsuarioPorUsuario(Long usuarioId);
}
