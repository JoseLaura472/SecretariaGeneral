package com.example.Proyecto.Models.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
    
    @Query(value = "select * from insertar_adm(?1, ?2, ?3)", nativeQuery = true)
    public Long insertar_adm(String usuario_nom, String contrasena, Integer id_persona);
}
