package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.Usuario;
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

    @Override
    public List<MisTelas> obtenerTelasDeFabrica() {
//        List<MisTelas> telas = new ArrayList<>();
//        long id = 1;
//
//        telas.add(new MisTelas(id++, TipoTela.ALGODON, "rojo", 12000.0, "img/TELA_1.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.ALGODON, "azul", 12000.0, "img/TELA_2.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.ALGODON, "rosa", 12000.0, "img/TELA_3.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.ALGODON, "gris", 12000.0, "img/TELA_4.jpg"));
//
//        telas.add(new MisTelas(id++, TipoTela.LINO, "azul", 13000.0, "img/TELA_5.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.LINO, "celeste", 13000.0, "img/TELA_6.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.LINO, "verde", 13000.0, "img/TELA_7.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.LINO, "verde agua", 13000.0, "img/TELA_8.jpg"));
//
//        telas.add(new MisTelas(id++, TipoTela.W15, "marron", 10000.0, "img/TELA_9.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.W15, "naranja", 10000.0, "img/TELA_10.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.W15, "negro", 10000.0, "img/TELA_11.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.W15, "gris oscuro", 10000.0, "img/TELA_12.jpg"));
//
//        telas.add(new MisTelas(id++, TipoTela.SET, "amarillo", 15000.0, "img/TELA_13.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.SET, "amarillo oscuro", 15000.0, "img/TELA_14.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.SET, "rosa", 15000.0, "img/TELA_15.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.SET, "verde agua", 15000.0, "img/TELA_16.jpg"));
//
//        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "lila", 18000.0, "img/TELA_17.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "rosa", 18000.0, "img/TELA_18.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "marron", 18000.0, "img/TELA_19.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "rosa oscuro", 18000.0, "img/TELA_20.jpg"));
//
//        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "celes", 20000.0, "img/TELA_21.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "beige", 20000.0, "img/TELA_22.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "marron", 20000.0, "img/TELA_23.jpg"));
//        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "blanco", 20000.0, "img/TELA_24.jpg"));

        return null;
    }

    public List<Tela> obtenerTelas() {
        return repositorioTela.listarTelas();
    }

    public Tela obtenerTela(Long id) {
        return repositorioTela.obtenerTela(id);
    }

    public void crearOActualizar(DatosTela datosTela, MultipartFile archivo) {
        Tela tela = new Tela();

        if (datosTela.getId() != 0) {
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
            Result imagenSubida = servicioStorageImagen.subirImagen(archivo, "tela-" + datosTela.getColor().toLowerCase());
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
    public Tela buscarTelaDelUsuario(Long id, Usuario usuario) throws TelaNoEncontrada {
        Tela tela = repositorioTela.buscarTelasDelUsuario(id, usuario);
        if (tela == null){
            throw new TelaNoEncontrada();
        }
        return tela;
    }

    /*
    @Override
    public Tela buscarTelaPorId(Long telaId, Usuario usuario) throws TelaNoEncontrada {
        Tela telaEncontrada = repositorioTela.buscarTelaPorId(telaId, usuario);
        if (telaEncontrada == null){
            throw new TelaNoEncontrada();
        }
        return telaEncontrada;
    }
    */
}