package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import com.tallerwebi.presentacion.controlador.ControladorPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorPedidoTest {

 /*   @InjectMocks
    private ControladorPedido controladorPedido;

    @Mock
    private ServicioPedido servicioPedido;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Usuario usuario;

    @Mock
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuario);
    }

    @Test
    void eliminarProducto_exito() {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(Estado.PENDIENTE);

        doNothing().when(servicioPedido).eliminarProductoDelPedido(pedido, 1L);
        doNothing().when(servicioPedido).asociarProductoPedido(pedido);

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(servicioPedido).eliminarProductoDelPedido(pedido, 1L);
        verify(servicioPedido).asociarProductoPedido(pedido);
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    void eliminarProducto_sinPedido_oEstadoNoPendiente() {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(null);

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se puede eliminar el producto en este estado.");
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    void eliminarProducto_excepcionAlEliminar() {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(Estado.PENDIENTE);

        doThrow(new RuntimeException("Error al eliminar")).when(servicioPedido).eliminarProductoDelPedido(pedido, 1L);

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("mensajeError"), startsWith("Error al eliminar el producto"));
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    public void eliminarProducto_pedidoValido_productoEliminadoYMensajeExito() throws Exception {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(Estado.PENDIENTE);  // <-- Aquí el cambio

        // Comportamiento para capturar addFlashAttribute
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            assertEquals("mensajeExito", key);
            assertEquals("Producto eliminado correctamente.", value);
            return null;
        }).when(redirectAttributes).addFlashAttribute(anyString(), any());

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(servicioPedido).eliminarProductoDelPedido(pedido, 1L);
        verify(servicioPedido).asociarProductoPedido(pedido);
        verify(redirectAttributes).addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    public void eliminarProducto_pedidoNoPendiente_mensajeError() {
        pedido.setEstado(Estado.A_RETIRAR);
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            assertEquals("mensajeError", key);
            assertEquals("No se puede eliminar el producto en este estado.", value);
            return null;
        }).when(redirectAttributes).addFlashAttribute(anyString(), any());

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(servicioPedido, never()).eliminarProductoDelPedido(any(), anyLong());
        verify(servicioPedido, never()).asociarProductoPedido(any());
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se puede eliminar el producto en este estado.");
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    public void eliminarProducto_pedidoNull_mensajeError() {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(null);

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String value = invocation.getArgument(1);
            assertEquals("mensajeError", key);
            assertEquals("No se puede eliminar el producto en este estado.", value);
            return null;
        }).when(redirectAttributes).addFlashAttribute(anyString(), any());

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(servicioPedido, never()).eliminarProductoDelPedido(any(), anyLong());
        verify(servicioPedido, never()).asociarProductoPedido(any());
        verify(redirectAttributes).addFlashAttribute("mensajeError", "No se puede eliminar el producto en este estado.");
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }

    @Test
    public void eliminarProducto_excepcionAlEliminar_mensajeError() throws Exception {
        when(servicioPedido.buscarPedidoEstadoPendiente(usuario)).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(Estado.PENDIENTE); // mockear estado para entrar en la rama

        doThrow(new RuntimeException("Error al eliminar")).when(servicioPedido).eliminarProductoDelPedido(pedido, 1L);

        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            System.out.println("addFlashAttribute key=" + key + ", value=" + value);
            assertEquals("mensajeError", key);
            assertTrue(value.toString().contains("Error al eliminar"));
            return null;
        }).when(redirectAttributes).addFlashAttribute(anyString(), any());

        ModelAndView mv = controladorPedido.eliminarProducto(1L, request, redirectAttributes);

        verify(servicioPedido).eliminarProductoDelPedido(pedido, 1L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeError"), contains("Error al eliminar"));
        assertEquals("redirect:/detalle-pedido", mv.getViewName());
    }
*/
}

