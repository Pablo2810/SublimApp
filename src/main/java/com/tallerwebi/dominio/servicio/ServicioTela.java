package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.EstadoTela;
import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.excepcion.TelaUsuarioNoEncontrada;
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

    void comprarTelaDeFabrica(Long idTela, Double metrosComprados, Usuario usuario) throws TelaNoEncontrada, StockInsuficiente;

    List<TelaUsuario> obtenerTelasDelUsuario(Usuario usuario);

    List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle);

    void restarMetrosTela(Tela tela, Double metrosTotales);

    void consumirTelaParaProducto(Tela telaSeleccionada, Double metrosNecesarios, Usuario usuario)
            throws StockInsuficiente, TelaNoEncontrada;

    void actualizarEstadoEnvio(Long idTelaUsuario, EstadoTela nuevoEstado) throws TelaUsuarioNoEncontrada;

    List<TelaUsuario> obtenerTelasUsuarioPorEstado(Long usuarioId, EstadoTela estado);

    List<TelaUsuario> obtenerTelasUsuario(Long usuarioId);

    void cambiarEstadoTela(Long idTelaUsuario, EstadoTela nuevoEstado) throws TelaUsuarioNoEncontrada;

    List <MisTelas> obtenerTelasParaCarrusel();

}
