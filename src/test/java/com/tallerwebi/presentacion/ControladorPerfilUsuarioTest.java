package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.EstadoTela;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.CancelacionNoPermitida;
import com.tallerwebi.dominio.excepcion.CompraTelaNoEncontrada;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.dominio.servicio.ServicioStorageImagen;
import com.tallerwebi.dominio.servicio.ServicioTela;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.presentacion.controlador.ControladorPerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import io.imagekit.sdk.models.results.Result;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class ControladorPerfilUsuarioTest {

    private MockMvc mockMvc;
    private ControladorPerfilUsuario controlador;

    private ServicioTela servicioTela;
    private ServicioUsuario servicioUsuario;
    private ServicioStorageImagen servicioStorageImagen;
    private ServicioPedido servicioPedido;

    private Usuario usuario;

    @BeforeEach
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/thymeleaf/");
        viewResolver.setSuffix(".jsp");

        servicioTela = Mockito.mock(ServicioTela.class);
        servicioUsuario = Mockito.mock(ServicioUsuario.class);
        servicioStorageImagen = Mockito.mock(ServicioStorageImagen.class);
        servicioPedido = Mockito.mock(ServicioPedido.class);

        controlador = new ControladorPerfilUsuario();

        // Inyectar mocks en controlador
        ReflectionTestUtils.setField(controlador, "servicioTela", servicioTela);
        ReflectionTestUtils.setField(controlador, "servicioUsuario", servicioUsuario);
        ReflectionTestUtils.setField(controlador, "servicioStorageImagen", servicioStorageImagen);
        ReflectionTestUtils.setField(controlador, "servicioPedido", servicioPedido);

        mockMvc = MockMvcBuilders.standaloneSetup(controlador)
                .setViewResolvers(viewResolver) // <---- importante
                .build();

        // Crear usuario para la sesión
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");
        usuario.setEmail("test@example.com");
        usuario.setPassword("vieja123");

        // Por defecto, email disponible
        when(servicioUsuario.emailDisponible(anyString())).thenReturn(true);
    }


    @Test
    public void siNoHayUsuarioEnSesionRedirigeALogin() throws Exception {
        mockMvc.perform(get("/perfil-usuario"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void muestraPerfilSiUsuarioEstaEnSesion() throws Exception {
        when(servicioPedido.listarPedidosDelUsuario(usuario.getId())).thenReturn(Collections.emptyList());
        when(servicioTela.obtenerComprasDeTelasPorUsuarioYEstado(eq(usuario.getId()), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/perfil-usuario")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil-usuario"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("pedidos"))
                .andExpect(model().attributeExists("telasEntregadas"));
    }

    @Test
    public void actualizarPerfilSinCambiarPasswordNiImagen() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nuevo Nombre")
                        .param("email", "libre@test.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));

        verify(servicioUsuario).emailDisponible("libre@test.com");
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
        MockMultipartFile archivo = new MockMultipartFile("imagenPerfil", "foto.jpg", "image/jpeg", "contenido".getBytes());

        Result resultadoMock = Mockito.mock(Result.class);
        when(resultadoMock.getUrl()).thenReturn("urlImagenMock");

        when(servicioStorageImagen.subirImagen(any(), eq("perfil_1"))).thenReturn(resultadoMock);

        mockMvc.perform(multipart("/perfil-usuario/actualizar")
                        .file(archivo)
                        .param("nombre", "Nombre")
                        .param("email", "libre@test.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));

        verify(servicioStorageImagen).subirImagen(any(), eq("perfil_1"));
    }

    @Test
    public void actualizarPerfilConEmailYaExistenteRedirigeConError() throws Exception {
        when(servicioUsuario.emailDisponible("correo@existente.com")).thenReturn(false);

        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nuevo Nombre")
                        .param("email", "correo@existente.com")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion-perfil"));
    }

    @Test
    public void actualizarPerfilSinEstarLogueadoRedirigeALogin() throws Exception {
        mockMvc.perform(post("/perfil-usuario/actualizar")
                        .param("nombre", "Nombre")
                        .param("email", "email@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void verPerfilConMensajesDeExitoYError() throws Exception {
        when(servicioPedido.listarPedidosDelUsuario(usuario.getId())).thenReturn(Collections.emptyList());
        when(servicioTela.obtenerTelasUsuarioPorEstado(eq(usuario.getId()), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/perfil-usuario")
                        .param("exito", "Perfil actualizado")
                        .param("error", "Algo falló")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil-usuario"))
                .andExpect(model().attributeExists("exito"))
                .andExpect(model().attributeExists("error"));
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

    @Test
    public void verEstadoEnvioTelasSinSesionRedirigeALogin() throws Exception {
        mockMvc.perform(get("/estado-envio-tela"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void verEstadoEnvioTelasConSesionDevuelveVistaYModelo() throws Exception {
        // Mockear el servicio para que devuelva lista vacía para compras en depósito
        when(servicioTela.obtenerComprasDeTelasPorUsuarioYEstado(usuario.getId(), EstadoTela.EN_DEPOSITO))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/estado-envio-tela")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("estado-envio-tela"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("compras"));
    }

    @Test
    public void cancelarCompraConUsuarioLogueado_redirigeConMensajeExito() throws Exception {
        doNothing().when(servicioTela).cancelarCompraTela(1L, usuario);

        mockMvc.perform(post("/cancelar-compra-tela/1")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estado-envio-tela"));
    }

    @Test
    public void cancelarCompra_sinSesion_redirigeLogin() throws Exception {
        mockMvc.perform(post("/cancelar-compra-tela/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void cancelarCompra_lanzaCompraNoEncontrada_redirigeConError() throws Exception {
        doThrow(new CompraTelaNoEncontrada()).when(servicioTela).cancelarCompraTela(1L, usuario);

        mockMvc.perform(post("/cancelar-compra-tela/1")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estado-envio-tela"));
    }

    @Test
    public void cancelarCompra_lanzaCancelacionNoPermitida_redirigeConError() throws Exception {
        doThrow(new CancelacionNoPermitida("No permitido")).when(servicioTela).cancelarCompraTela(1L, usuario);

        mockMvc.perform(post("/cancelar-compra-tela/1")
                        .sessionAttr("usuarioLogueado", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estado-envio-tela"));
    }

}




