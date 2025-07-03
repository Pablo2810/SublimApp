package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.entidad.Usuario;

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

}
