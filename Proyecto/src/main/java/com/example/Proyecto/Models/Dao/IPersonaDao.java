package com.example.Proyecto.Models.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long> {

    @Query(value = "select * from persona as p where p.ci_persona = ?1", nativeQuery = true)
    public Persona getPersonaCI(String ci);

}
