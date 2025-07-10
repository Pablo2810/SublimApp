package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioPais;
import com.tallerwebi.dominio.servicio.ServicioTalle;
import com.tallerwebi.presentacion.controlador.ControladorTalle;
import com.tallerwebi.presentacion.dto.DatosPais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class ControladorTalleTest {
    private ServicioTalle servicioTalle;
    private ServicioPais servicioPais;
    private ControladorTalle controladorTalle;

    @BeforeEach
    public void init(){
        servicioTalle = mock(ServicioTalle.class);
        servicioPais = mock(ServicioPais.class);

        controladorTalle = new ControladorTalle(servicioTalle, servicioPais);
    }

    @Test
    public void queSePuedaObtenerLaGuiaDeTalles() {
       
    }


    @Test
    public void queDevuelvaUnTalleRecomendadoEnBaseAlosDatosQueColocoElUsuario(){

    }

    @Test
    public void queDevuelvaUnMensajeDeErrorSiLosDatosNoSonValidos(){

    }
}
