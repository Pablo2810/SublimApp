package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
import com.tallerwebi.dominio.excepcion.TelaUsuarioNoEncontrada;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("servicioTela")
@Transactional
public class ServicioTelaImpl implements ServicioTela {
    @Autowired
    private RepositorioTela repositorioTela;

    @Autowired
    private ServicioStorageImagen servicioStorageImagen;

    public ServicioTelaImpl(RepositorioTela repositorioTela, ServicioStorageImagen servicioStorageImagen) {
        this.repositorioTela = repositorioTela;
        this.servicioStorageImagen = servicioStorageImagen;
    }


    @Override
    public List<Tela> obtenerTelas() {
        return repositorioTela.listarTelas();
    }

    @Override
    public Tela obtenerTela(Long id) {
        return repositorioTela.obtenerTela(id);
    }

    @Override
    public void crearOActualizar(DatosTela datosTela, MultipartFile archivo) {
        Tela tela = new Tela();

        if (datosTela.getId() != null && datosTela.getId() != 0) {
            try {
                tela = obtenerTela(datosTela.getId());
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        tela.setMetros(datosTela.getMetros());
        tela.setTipoTela(datosTela.getTipoTela());
        tela.setPrecio(datosTela.getPrecio());
        tela.setColor(datosTela.getColor());

        if (!archivo.isEmpty()) {
            Result imagenSubida = servicioStorageImagen.subirImagen(archivo, "telas", "tela-" + datosTela.getColor().toLowerCase());
            tela.setImagenUrl(imagenSubida.getUrl());
        }

        repositorioTela.crearOActualizarTela(tela);
    }

    @Override
    public void borrarTela(Long id) {
        try {
            Tela tela = obtenerTela(id);
            repositorioTela.borrarTela(tela);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<MisTelas> obtenerTelasDeFabrica() {
        List<Tela> telas = repositorioTela.listarTelasDeFabrica();
        return telas.stream()
                .map(t -> {
                    MisTelas misTela = new MisTelas(t.getId(), t.getTipoTela(), t.getColor(), t.getPrecio(), t.getImagenUrl());
                    misTela.setMetros(t.getMetros());
                    return misTela;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void comprarTelaDeFabrica(Long idTela, Double metrosComprados, Usuario usuario)
            throws TelaNoEncontrada, StockInsuficiente {
        Tela tela = repositorioTela.obtenerTela(idTela);
        if (tela == null) {
            throw new TelaNoEncontrada();
        }

        if (tela.getMetros() < metrosComprados) {
            throw new StockInsuficiente();
        }

        // Descontar metros de stock de fábrica
        tela.setMetros(tela.getMetros() - metrosComprados);
        repositorioTela.crearOActualizarTela(tela);

        // Buscar si el usuario ya tiene una tela igual (por tipo + color)
        TelaUsuario telaExistente = repositorioTela.buscarTelaUsuarioPorTipoYColor(
                usuario, tela.getTipoTela(), tela.getColor());

        if (telaExistente != null) {
            // Ya tiene una tela igual, sumamos metros
            telaExistente.setMetros(telaExistente.getMetros() + metrosComprados);
            repositorioTela.crearOActualizarTela(telaExistente);
        } else {
            // No tiene una igual, creamos una nueva con estado "EN_DEPOSITO"
            TelaUsuario telaComprada = new TelaUsuario();
            telaComprada.setTipoTela(tela.getTipoTela());
            telaComprada.setColor(tela.getColor());
            telaComprada.setImagenUrl(tela.getImagenUrl());
            telaComprada.setPrecio(tela.getPrecio());
            telaComprada.setMetros(metrosComprados);
            telaComprada.setUsuario(usuario);
            telaComprada.setEstado(EstadoTela.EN_DEPOSITO); // ← NUEVO

            repositorioTela.crearOActualizarTela(telaComprada);
        }
    }

    @Override
    public void consumirTelaParaProducto(Tela telaSeleccionada, Double metrosNecesarios, Usuario usuario)
            throws StockInsuficiente, TelaNoEncontrada {

        if (telaSeleccionada.getMetros() >= metrosNecesarios) {
            telaSeleccionada.setMetros(telaSeleccionada.getMetros() - metrosNecesarios);
            repositorioTela.crearOActualizarTela(telaSeleccionada);
        } else {
            throw new StockInsuficiente();
        }
    }

    @Override
    public List<TelaUsuario> obtenerTelasUsuarioPorEstado(Long usuarioId, EstadoTela estado) {
        return repositorioTela.buscarTelasUsuarioPorUsuarioYEstado(usuarioId, estado);
    }

    @Override
    public List<TelaUsuario> obtenerTelasUsuario(Long usuarioId) {
        return repositorioTela.buscarTelasUsuarioPorUsuario(usuarioId);
    }

    @Override
    public void cambiarEstadoTela(Long idTelaUsuario, EstadoTela nuevoEstado) throws TelaUsuarioNoEncontrada {
        Tela tela = repositorioTela.obtenerTela(idTelaUsuario);

        if (tela instanceof TelaUsuario) {
            TelaUsuario telaUsuario = (TelaUsuario) tela;
            telaUsuario.setEstado(nuevoEstado);
            repositorioTela.crearOActualizarTela(telaUsuario);
        } else {
            throw new TelaUsuarioNoEncontrada();
        }
    }

    @Override
    public List<MisTelas> obtenerTelasParaCarrusel() {
        List<MisTelas> todas = this.obtenerTelasDeFabrica();

        return todas.stream()
                .filter(t -> (
                        (t.getTipoTela() == TipoTela.LINO && t.getColor().equalsIgnoreCase("azul")) ||
                                (t.getTipoTela() == TipoTela.W15 && t.getColor().equalsIgnoreCase("blanco")) ||
                                (t.getTipoTela() == TipoTela.NEOPRENO && t.getColor().equalsIgnoreCase("marron")) ||
                                (t.getTipoTela() == TipoTela.ALGODON && t.getColor().equalsIgnoreCase("rojo")) ||
                                (t.getTipoTela() == TipoTela.SET && t.getColor().equalsIgnoreCase("amarillo")) ||
                                (t.getTipoTela() == TipoTela.LINO && t.getColor().equalsIgnoreCase("verde agua")) ||
                                (t.getTipoTela() == TipoTela.W15 && t.getColor().equalsIgnoreCase("negro")) ||
                                (t.getTipoTela() == TipoTela.NEOPRENO && t.getColor().equalsIgnoreCase("lila")) ||
                                (t.getTipoTela() == TipoTela.ALGODON && t.getColor().equalsIgnoreCase("gris")) ||
                                (t.getTipoTela() == TipoTela.SET && t.getColor().equalsIgnoreCase("rosa"))
                ))
                .collect(Collectors.toList());
    }

}