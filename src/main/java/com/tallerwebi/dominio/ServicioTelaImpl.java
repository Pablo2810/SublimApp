package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.DatosTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioTelaImpl implements ServicioTela {

    @Autowired
    private RepositorioTela repositorioTela;

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

    public void crearOActualizar(DatosTela datosTela) {
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
        tela.setImagenUrl(datosTela.getImagenUrl());

        repositorioTela.crearOActualizarTela(tela);
    }

    public void borrarTela(Long id) {
        try {
            Tela tela = obtenerTela(id);
            repositorioTela.borrarTela(tela.getId());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}