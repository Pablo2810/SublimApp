package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String rol;

    private Boolean activo = false;

    private String nombre;

    private String telefono;

    @Column(name = "imagen_perfil")
    private String imagenPerfil;

    private Integer edad;


    @OneToMany(mappedBy = "usuario")
    private List<TelaUsuario> telas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioPedido")
    private List<Pedido> pedidos = new ArrayList<>();

    private Integer cantidadPromocionesVistas = 0;

    @ManyToMany
    @JoinTable(name = "usuario_promocion")
    private Set<Promocion> promocionesAceptadas = new HashSet<>();

    private Double promedioItemsPorPedido;

    private Double frecuenciaPedidos;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public boolean activo() {
        return activo;
    }
    public void activar() {
        activo = true;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getImagenPerfil() { return imagenPerfil; }
    public void setImagenPerfil(String imagenPerfil) { this.imagenPerfil = imagenPerfil; }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Integer getCantidadPromocionesVistas() {
        return cantidadPromocionesVistas;
    }
    public void setCantidadPromocionesVistas(Integer cantidadPromocionesVistas) { this.cantidadPromocionesVistas = cantidadPromocionesVistas; }
    public Double getPromedioItemsPorPedido() {
        return promedioItemsPorPedido;
    }
    public void setPromedioItemsPorPedido(Double promedioItemsPorPedido) { this.promedioItemsPorPedido = promedioItemsPorPedido; }
    public Double getFrecuenciaPedidos() {
        return frecuenciaPedidos;
    }
    public void setFrecuenciaPedidos(Double frecuenciaPedidos) {
        this.frecuenciaPedidos = frecuenciaPedidos;
    }
    public Integer getEdad(){ return edad; }
    public void setEdad(Integer edad){ this.edad = edad; }

    public void agregarTela(TelaUsuario tela) {
        this.telas.add(tela);
        tela.setUsuario(this);
    }

    public List<TelaUsuario> getTelas() {
        return telas;
    }

    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setUsuarioPedido(this);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void agregarPromocion(Promocion promocion) {
        this.promocionesAceptadas.add(promocion);
        promocion.getUsuariosAceptaron().add(this);
    }

    public Set<Promocion> getPromocionesAceptadas() {
        return promocionesAceptadas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }
}