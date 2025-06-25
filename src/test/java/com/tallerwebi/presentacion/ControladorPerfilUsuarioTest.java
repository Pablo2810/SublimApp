package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.integracion.config.TestBeansConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringWebTestConfig.class,
        HibernateTestConfig.class,
        TestBeansConfig.class
})
@WebAppConfiguration
public class ControladorPerfilUsuarioTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Usuario usuario;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");
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
                        .param("email", "nuevo@email.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil-usuario?exito=Datos actualizados correctamente."));
    }

    @Test
    public void actualizarPerfilConPasswordIncorrectaRedirigeConError() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "email@dominio.com")
                        .param("password", "nueva123")
                        .param("passwordActual", "incorrecta")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil-usuario?error=La contraseña actual es incorrecta."));
    }

    @Test
    public void eliminarUsuarioRedirigeAHomeYDestruyeSesion() throws Exception {
        HttpSession session = iniciarSesion();

        mockMvc.perform(post("/perfil-usuario/eliminar")
                        .session((MockHttpSession) session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void actualizarPerfilConPasswordCorrectaActualizaYRedirigeConExito() throws Exception {
        usuario.setPassword("vieja123");

        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "email@dominio.com")
                        .param("password", "nueva123")
                        .param("passwordActual", "vieja123")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil-usuario?exito=Datos actualizados correctamente."));
    }

    @Test
    public void actualizarPerfilConImagenSubida() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile("archivo", "foto.jpg", "image/jpeg", "contenido-imagen".getBytes());

        mockMvc.perform(multipart("/perfil-usuario/actualizar")
                        .file(archivo)
                        .param("nombre", "Nombre")
                        .param("email", "email@dominio.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil-usuario?exito=Datos actualizados correctamente."));
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

}


