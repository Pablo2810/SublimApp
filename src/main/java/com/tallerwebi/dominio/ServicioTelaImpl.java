package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.StockInsuficiente;
import com.tallerwebi.dominio.excepcion.TelaNoEncontrada;
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

    @Autowired
    public ServicioTelaImpl(RepositorioTela repositorioTela) {
        this.repositorioTela = repositorioTela;
    }

    @Override
    public void agregarTelasDeFabrica() {

    }

    public List<Tela> obtenerTelas() {
        return repositorioTela.listarTelas();
    }

    public Tela obtenerTela(Long id) {
        return repositorioTela.obtenerTela(id);
    }

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

    public void borrarTela(Long id) {
        try {
            Tela tela = obtenerTela(id);
            repositorioTela.borrarTela(tela);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    @Override
    public void dejarSinStockTelaDeFabrica() {

    }

    @Override
    public List<Tela> buscarTelasDePrendaPorIdPrenda(Long prendaId) {
        return repositorioTela.buscarTelasDePrendaPorIdPrenda(prendaId);
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
    public void comprarTelaDeFabrica(Long idTela, Double metrosComprados, Usuario usuario) throws TelaNoEncontrada, StockInsuficiente {
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
        TelaUsuario telaExistente = repositorioTela.buscarTelaUsuarioPorTipoYColor(usuario, tela.getTipoTela(), tela.getColor());

        if (telaExistente != null) {
            // Ya tiene una tela igual, sumamos metros
            telaExistente.setMetros(telaExistente.getMetros() + metrosComprados);
            repositorioTela.crearOActualizarTela(telaExistente);
        } else {
            // No tiene una igual, creamos una nueva
            TelaUsuario telaComprada = new TelaUsuario();
            telaComprada.setTipoTela(tela.getTipoTela());
            telaComprada.setColor(tela.getColor());
            telaComprada.setImagenUrl(tela.getImagenUrl());
            telaComprada.setPrecio(tela.getPrecio());
            telaComprada.setMetros(metrosComprados);
            telaComprada.setUsuario(usuario);

            repositorioTela.crearOActualizarTela(telaComprada);
        }
    }

    @Override
    public List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle) {
        return repositorioTela.buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(prendaId, metrosTalle);
    }

    @Override
    public void restarMetrosTela(Tela tela, Double metrosTotales) {
        if (tela != null) {
            tela.setMetros(tela.getMetros() - metrosTotales);
            repositorioTela.crearOActualizarTela(tela);
        } else {
            throw new TelaNoEncontrada();
        }

    }

    @Override
    public List<TelaUsuario> obtenerTelasDelUsuario(Usuario usuario) {
        return repositorioTela.obtenerTelasPorUsuario(usuario);
    }

    @Override
    public void consumirTelaParaProducto(Tela telaSeleccionada, Double metrosNecesarios, Usuario usuario)
            throws StockInsuficiente, TelaNoEncontrada {

        // Buscar si el usuario tiene tela
        TelaUsuario telaUsuario = repositorioTela.buscarTelaUsuarioPorTipoYColor(usuario,
                telaSeleccionada.getTipoTela(), telaSeleccionada.getColor());

        // Caso 1: el usuario tiene suficiente
        if (telaUsuario != null && telaUsuario.getMetros() >= metrosNecesarios) {
            telaUsuario.setMetros(telaUsuario.getMetros() - metrosNecesarios);
            repositorioTela.crearOActualizarTela(telaUsuario);
            return;
        }

        // Caso 2: la fábrica tiene suficiente
        if (telaSeleccionada.getMetros() >= metrosNecesarios) {
            telaSeleccionada.setMetros(telaSeleccionada.getMetros() - metrosNecesarios);
            repositorioTela.crearOActualizarTela(telaSeleccionada);
            // No se guarda en stock del usuario
            return;
        }

        // Caso 3: ninguno tiene suficiente
        throw new StockInsuficiente();
    }


}