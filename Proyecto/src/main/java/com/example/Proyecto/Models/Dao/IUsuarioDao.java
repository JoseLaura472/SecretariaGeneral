package com.example.Proyecto.Models.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    @Query(value = "select * from insertar_adm(?1, ?2, ?3, ?4)", nativeQuery = true)
    public Long insertar_adm(String usuario_nom, String contrasena, Integer id_persona, Integer id_consejo);

    @Query(value = "select * from validar_adm(?1, ?2)", nativeQuery = true)
    public Long validar_adm(String usuario_nom, String contrasena);

    @Query(value = "select u from Usuario u where u.usuario_nom = ?1 and u.usuario_codigo = ?2", nativeQuery = true)
    public Usuario getUsuarioContraseña(String correo, String password);
}
