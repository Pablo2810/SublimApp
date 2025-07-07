package com.tallerwebi.presentacion;
import com.tallerwebi.config.RestTemplateConfig;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.integracion.config.TestBeansConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.servlet.http.HttpSession;
import io.imagekit.sdk.models.results.Result;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringWebTestConfig.class,
        HibernateTestConfig.class,
        TestBeansConfig.class,
        RestTemplateConfig.class
})
@WebAppConfiguration
public class ControladorPerfilUsuarioTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Usuario usuario;

    private ServicioUsuario servicioUsuario;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");

        servicioUsuario = (ServicioUsuario) wac.getBean("servicioUsuario");
        Mockito.when(servicioUsuario.emailDisponible(Mockito.anyString())).thenReturn(true);

        ServicioStorageImagen mockStorage = (ServicioStorageImagen) wac.getBean("servicioStorageImagen");
        Result fakeResult = new Result();
        fakeResult.setUrl("http://imagen.mock");
        Mockito.when(mockStorage.subirImagen(Mockito.any(), Mockito.anyString())).thenReturn(fakeResult);
    }


    private HttpSession iniciarSesion() throws Exception {
        return this.mockMvc.perform(get("/perfil-usuario")
                        .sessionAttr("usuarioLogueado", usuario))
                .andReturn()
                .getRequest()
                .getSession();
    }

    @Test
    public void siNoHayUsuarioEnSesionRedirigeALogin() throws Exception {
        mockMvc.perform(get("/perfil-usuario"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void muestraPerfilSiUsuarioEstaEnSesion() throws Exception {
        mockMvc.perform(get("/perfil-usuario")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil-usuario"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("pedidos"));
    }

    @Test
    public void actualizarPerfilSinCambiarPasswordNiImagen() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nuevo Nombre")
                        .param("email", "libre@test.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilConPasswordIncorrectaRedirigeConError() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "libre@test.com")
                        .param("password", "nueva123")
                        .param("passwordActual", "incorrecta")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilConPasswordCorrectaActualizaYRedirigeConExito() throws Exception {
        usuario.setPassword("vieja123");

        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "libre@test.com")
                        .param("password", "nueva123")
                        .param("passwordActual", "vieja123")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilConImagenSubida() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile("imagenPerfil", "foto.jpg", "image/jpeg", "contenido-imagen".getBytes());

        mockMvc.perform(multipart("/perfil-usuario/actualizar")
                        .file(archivo)
                        .param("nombre", "Nombre")
                        .param("email", "libre@test.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilSinEstarLogueadoRedirigeALogin() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "email@test.com") // este faltaba antes
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void verPerfilConMensajesDeExitoYError() throws Exception {
        mockMvc.perform(get("/perfil-usuario")
                        .param("exito", "Perfil actualizado")
                        .param("error", "Algo falló")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exito"))
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("perfil-usuario"));
    }

    @Test
    public void actualizarPerfilConEmailYaExistenteRedirigeConError() throws Exception {
        usuario.setEmail("usuario@actual.com");
        Mockito.when(servicioUsuario.emailDisponible("correo@existente.com")).thenReturn(false);

        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nuevo Nombre")
                        .param("email", "correo@existente.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilConImagenVaciaNoRompe() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile("imagenPerfil", "foto.jpg", "image/jpeg", "contenido-imagen".getBytes());

        mockMvc.perform(multipart("/perfil-usuario/actualizar")
                        .file(archivo)
                        .param("nombre", "Nombre")
                        .param("email", "libre@test.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void verConfiguracionPerfilSinSesionRedirigeALogin() throws Exception {
        mockMvc.perform(get("/configuracion-perfil"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?exito=&error="));
    }

    @Test
    public void verConfiguracionPerfilConMensajes() throws Exception {
        mockMvc.perform(get("/configuracion-perfil")
                        .param("exito", "Cambios guardados")
                        .param("error", "Error grave")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("configuracion_perfil"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("exito"))
                .andExpect(model().attributeExists("error"));
    }

}


