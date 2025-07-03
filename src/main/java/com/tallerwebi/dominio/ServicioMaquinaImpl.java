package com.tallerwebi.dominio;
import com.tallerwebi.dominio.servicio.ServicioMaquina;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service("servicioMaquina")
@Transactional
public class ServicioMaquinaImpl implements ServicioMaquina {

    @Override
    public Long calcularTiempoEspera() {
        Random random = new Random();
        return random.nextLong();
    }
}
