package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ServicioTela {

    List<MisTelas> obtenerTelasDeFabrica();

    List<Tela> obtenerTelas();

    Tela obtenerTela(Long id);

    void crearOActualizar(DatosTela datosTela, MultipartFile archivo);

    void borrarTela(Long id);

    void comprarTelaDeFabrica(Long idTela, Double metrosComprados, Usuario usuario) throws TelaNoEncontrada, StockInsuficiente;

    void registrarCompraTela(Long idTela, Usuario usuario, Double metros, String metodoPago,
                             Integer cuotas, boolean pagoEnDolares, double cotizacionDolar,
                             TipoEnvio envioSeleccionado);

    void consumirTelaParaProducto(Tela telaSeleccionada, Double metrosNecesarios, Usuario usuario)
            throws StockInsuficiente, TelaNoEncontrada;

    List<TelaUsuario> obtenerTelasUsuarioPorEstado(Long usuarioId, EstadoTela estado);

    List<TelaUsuario> obtenerTelasUsuario(Long usuarioId);

    void cambiarEstadoTela(Long idTelaUsuario, EstadoTela nuevoEstado) throws TelaUsuarioNoEncontrada;

    List <MisTelas> obtenerTelasParaCarrusel();

    void cancelarCompraTela(Long idCompraTela, Usuario usuario) throws CompraTelaNoEncontrada, CancelacionNoPermitida;

    List<CompraTela> obtenerComprasDeTelasPorUsuarioYEstado(Long usuarioId, EstadoTela estado);

    List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle);

    void cambiarEstadoCompraTela(Long idCompra, EstadoTela nuevoEstado) throws CompraTelaNoEncontrada;

    List<CompraTela> obtenerComprasPorEstados(List<EstadoTela> estados);

    List<CompraTela> obtenerComprasDeTelasPorUsuarioYEstados(Long usuarioId, List<EstadoTela> estados);

    void actualizar(Tela tela);
}
