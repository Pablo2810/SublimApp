package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.controlador.ControladorPerfil;
import com.tallerwebi.presentacion.dto.DatosUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorPerfilTest {

    DatosUsuario mockDatosUsuario;
    ControladorPerfil controladorPerfil;
    ServicioUsuario mockServicioUsuario;
    HttpSession mockSession;
    Usuario mockUsuario;
    RepositorioUsuario repositorioUsuario;

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockUsuario = mock(Usuario.class);
        mockUsuario.setNombre("Usuario 1");
        mockUsuario.setEmail("test@test.com");
        mockUsuario.setPassword("1234");
        mockUsuario.setRol("ADMIN");
        mockUsuario.setActivo(true);
        mockUsuario.setId(1L);
        mockUsuario.setUrlImg("");
        mockDatosUsuario = mock(DatosUsuario.class);
        when(mockUsuario.getEmail()).thenReturn("test@test.com");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServicioUsuario = mock(ServicioUsuario.class);
        controladorPerfil = new ControladorPerfil(mockServicioUsuario);
        mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("userEmail")).thenReturn("test@test.com");
        when(mockServicioUsuario.consultarUsuario("test@test.com")).thenReturn(mockUsuario);
        repositorioUsuario = mock(RepositorioUsuario.class);
        doAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);  // Obtener el usuario que se pasa al método
            mockUsuario.setNombre("Usuario 2");          // Cambiar nombre del mockUsuario (u otro objeto que quieras)
            return true;                                  // Si el método modificar es void, devuelve null
        }).when(repositorioUsuario).modificar(any(Usuario.class));
    }

    @Test
    public void queNavegarAPerfilRetorneEnModelElModeloLosDatosDelUsuario() throws Exception {
        mockSession.setAttribute("userEmail", "test@test.com");

        ModelAndView modelAndView = controladorPerfil.navegarPerfil(mockSession);
        assert modelAndView != null;
        DatosUsuario datosUsuario = (DatosUsuario) modelAndView.getModel().get("datosUsuario");
        assertThat("perfil", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
        assertThat(mockUsuario.getEmail(), is(datosUsuario.getEmail()));
    }

    @Test
    public void queSePuedaActualizarNombreDeUsuario() {
        mockUsuario = mock(Usuario.class);
        mockUsuario.setNombre("Usuario 1");
        mockUsuario.setEmail("test@test.com");
        mockUsuario.setPassword("1234");
        mockUsuario.setRol("ADMIN");
        mockUsuario.setActivo(true);
        mockUsuario.setId(1L);
        mockUsuario.setUrlImg("");

        mockDatosUsuario = new DatosUsuario();

        mockSession.setAttribute("userEmail", "test@test.com");
        mockDatosUsuario.setNombre("Usuario 2");
        ModelAndView modelAndView = controladorPerfil.guardarPerfil(mockDatosUsuario, mockSession);
        DatosUsuario datosUsuario = (DatosUsuario) modelAndView.getModel().get("datosUsuario");

        assertThat("Usuario 2", is(datosUsuario.getNombre()));
    }
}
