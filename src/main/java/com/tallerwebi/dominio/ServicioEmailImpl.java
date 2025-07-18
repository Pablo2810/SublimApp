package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioEmail;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Service
public class ServicioEmailImpl implements ServicioEmail {

    @Autowired
    private TemplateEngine motorPlantilla;

    @Value("${emailRemitente}")
    private String emailRemitente;
    @Value("${passwordRemitente}")
    private String passwordRemitente;

    @Override
    public void enviarCorreoEstadoPedido(String destinatario, Pedido pedido, String linkRedireccion) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailRemitente, passwordRemitente);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(emailRemitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject("Cambio de estado del pedido");

        Context context = new Context();
        context.setVariable("nombreUsuario", pedido.getUsuarioPedido().getNombre());
        context.setVariable("estadoPedido", pedido.getEstado().getDescripcion());
        context.setVariable("linkDetalle", linkRedireccion);

        String htmlProcesado = motorPlantilla.process("emails/email-estado-pedido.html", context);

        mensaje.setContent(htmlProcesado, "text/html");

        Transport.send(mensaje);
    }

}
