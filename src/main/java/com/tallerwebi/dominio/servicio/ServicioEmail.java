package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Pedido;

import javax.mail.MessagingException;

public interface ServicioEmail {
    void enviarCorreoEstadoPedido(String destinatario, Pedido pedido, String linkRedireccion) throws MessagingException;
}
