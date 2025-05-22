package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioArchivo;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.RepositorioPedidoImpl;
import com.tallerwebi.presentacion.controlador.ControladorPedido;
import com.tallerwebi.presentacion.dto.DatosPedido;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;

public class ServicioPedidoTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    public void init(){
        sessionFactory = mock(SessionFactory.class);
    }

    @Test
    public void queSePuedaRegistrarUnPedido() {
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl(new RepositorioPedidoImpl(sessionFactory));
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(9.0); // -> alto * 1500 = 13500
        Usuario usuario = new Usuario();
        usuario.setActivo(true);
        usuario.setEmail("admin@unlam.edu.ar");
        usuario.setId(1L);
        usuario.setPassword("admin");
        usuario.setRol("admin");
        Integer cantidadCopias = 4;

        Pedido pedido = servicioPedido.registrarPedido(cantidadCopias, archivo, usuario);

        assertThat(pedido.getCantCopias().toString(), equalToIgnoringCase("4"));
        assertThat(pedido.getMetrosTotales(), equalToIgnoringCase("5.0x36.0"));
        assertThat(pedido.getCostoServicio().toString(), equalToIgnoringCase("54000.0"));
        assertThat(pedido.getEstado().toString(), equalToIgnoringCase("EN_ESPERA"));
    }

    @Test
    public void queSePuedaCalcularElCostoDeServicioDeUnPedido(){
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl(new RepositorioPedidoImpl(sessionFactory));
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(14.8); // -> alto * 1500
        Integer cantidadCopias = 4;

        Double costo = servicioPedido.calcularCostoTotal(archivo.getAlto(), cantidadCopias);

        assertThat(costo.toString(), equalToIgnoringCase("88800.0"));
    }
}
