package com.tallerwebi.dominio;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.tallerwebi.dominio.entidad.TipoTela;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.presentacion.dto.MisTelas;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTelaImpl implements ServicioTela {

    @Override
    public List<MisTelas> obtenerTelasDeFabrica() {
        List<MisTelas> telas = new ArrayList<>();
        long id = 1;
        String IMG_PATH = "../webapp/resources/core/img/";

        telas.add(new MisTelas(id++, TipoTela.ALGODON, "amarillo", 12000.0, IMG_PATH+"tela_algodon_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.ALGODON, "azul", 12000.0, IMG_PATH+"tela_algodon_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.ALGODON, "rojo", 12000.0, IMG_PATH+"tela_algodon_roja.jpg"));
        telas.add(new MisTelas(id++, TipoTela.ALGODON, "negro", 12000.0, IMG_PATH+"tela_algodon_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.ALGODON, "blanco", 12000.0, IMG_PATH+"tela_algodon_blanca.jpg"));
        telas.add(new MisTelas(id++, TipoTela.LINO, "rojo", 13000.0, IMG_PATH+"tela_lino_roja.jpg"));
        telas.add(new MisTelas(id++, TipoTela.LINO, "azul", 13000.0, IMG_PATH+"tela_lino_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.LINO, "amarillo", 13000.0, IMG_PATH+"tela_lino_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.LINO, "negro", 13000.0, IMG_PATH+"tela_lino_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.LINO, "blanco", 13000.0, IMG_PATH+"tela_lino_blanca.jpg"));
        telas.add(new MisTelas(id++, TipoTela.W15, "rojo", 10000.0, IMG_PATH+"tela_w15_roja.jpg"));
        telas.add(new MisTelas(id++, TipoTela.W15, "negro", 10000.0, IMG_PATH+"tela_w15_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.W15, "blanco", 10000.0, IMG_PATH+"tela_w15_blanca.jpg"));
        telas.add(new MisTelas(id++, TipoTela.W15, "amarillo", 10000.0, IMG_PATH+"tela_w15_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.W15, "azul", 10000.0, IMG_PATH+"tela_w15_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.SET, "negro", 15000.0, IMG_PATH+"tela_set_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.SET, "blanco", 15000.0, IMG_PATH+"tela_set_blanca.jpg"));
        telas.add(new MisTelas(id++, TipoTela.SET, "azul", 15000.0, IMG_PATH+"tela_set_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.SET, "rojo", 15000.0, IMG_PATH+"tela_set_roja.jpg"));
        telas.add(new MisTelas(id++, TipoTela.SET, "amarillo", 18000.0, IMG_PATH+"tela_set_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "negro", 18000.0, IMG_PATH+"tela_neopreno_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "azul", 18000.0, IMG_PATH+"tela_neopreno_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "amarillo", 18000.0, IMG_PATH+"tela_neopreno_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "rojo", 18000.0, IMG_PATH+"tela_neopreno_roja.jpg"));
        telas.add(new MisTelas(id++, TipoTela.NEOPRENO, "blanco", 18000.0, IMG_PATH+"tela_neopreno_blanca.jpg"));
        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "negro", 20000.0, IMG_PATH+"tela_poliester_negra.jpg"));
        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "blanco", 20000.0, IMG_PATH+"tela_poliester_blanco.jpg"));
        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "azul", 20000.0, IMG_PATH+"tela_poliester_azul.jpg"));
        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "amarillo", 20000.0, IMG_PATH+"tela_poliester_amarillo.jpg"));
        telas.add(new MisTelas(id++, TipoTela.POLIESTER, "rojo", 20000.0, IMG_PATH+"tela_poliester_roja.jpg"));


        return telas;
    }


}