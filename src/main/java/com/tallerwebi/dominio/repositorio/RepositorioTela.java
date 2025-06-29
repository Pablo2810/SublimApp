package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Tela;
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

    // Tela buscarTelaPorId(Long id, Usuario usuario);


    Tela buscarTelasDelUsuario(Long id, Usuario usuario);

    List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle);
}
