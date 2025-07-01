package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.ServicioTelaImpl;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioTela {
    void agregarTelasDeFabrica();
    List<MisTelas> obtenerTelasDeFabrica();
    List<Tela> obtenerTelas();
    Tela obtenerTela(Long id);
    void crearOActualizar(DatosTela datosTela, MultipartFile archivo);
    void borrarTela(Long id);
    void dejarSinStockTelaDeFabrica();
    List<Tela> buscarTelasDePrendaPorIdPrenda(Long prendaId);
    void comprarTelaDeFabrica(Long idTela, Double metrosComprados, Usuario usuario) throws TelaNoEncontrada, StockInsuficiente;
    void crearTelaDelUsuario(TelaUsuario telaUsuario);
    List<Tela> obtenerTelasDelUsuario(Usuario usuario);


    Tela buscarTelaDelUsuario(Long id, Usuario usuario) throws TelaNoEncontrada;
    // Tela buscarTelaPorId(Long telaId, Usuario usuario) throws TelaNoEncontrada;
}
