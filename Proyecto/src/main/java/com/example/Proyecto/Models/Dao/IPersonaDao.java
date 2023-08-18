package com.example.Proyecto.Models.Dao;

import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long>{
    
}
