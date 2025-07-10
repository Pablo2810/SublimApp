package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioTela {

    List<Tela> listarTelas();

    Tela obtenerTela(Long id);

    void crearOActualizarTela(Tela tela);

    void borrarTela(Tela id);

    List<Tela> listarTelasDeFabrica();

    TelaUsuario buscarTelaUsuarioPorTipoYColor(Usuario usuario, TipoTela tipoTela, String color);

    List<TelaUsuario> buscarTelasUsuarioPorUsuarioYEstado(Long usuarioId, EstadoTela estado);

    List<TelaUsuario> buscarTelasUsuarioPorUsuario(Long usuarioId);

    List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle);
}
