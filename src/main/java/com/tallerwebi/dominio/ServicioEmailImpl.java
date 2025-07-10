package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class ServicioEmailImpl implements ServicioEmail {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine motorPlantilla;


    @Override
    public void enviarCorreoEstadoPedido(String destinatario, Pedido pedido, String linkRedireccion) throws javax.mail.MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("nombreUsuario", pedido.getUsuarioPedido().getNombre());
        context.setVariable("estadoPedido", pedido.getEstado().getDescripcion());
        context.setVariable("linkDetalle", linkRedireccion);

        String htmlProcesado = motorPlantilla.process("emails/email-estado-pedido.html", context);

        helper.setTo(destinatario);
        helper.setSubject("Cambio de estado del pedido");
        helper.setText(htmlProcesado, true);

        mailSender.send(mimeMessage);
    }

}
