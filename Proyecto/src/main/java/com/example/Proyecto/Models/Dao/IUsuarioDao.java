package com.example.Proyecto.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
    
}
