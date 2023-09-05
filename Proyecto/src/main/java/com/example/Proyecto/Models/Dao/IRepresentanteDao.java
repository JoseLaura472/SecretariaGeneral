package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Representante;

public interface IRepresentanteDao extends CrudRepository<Representante, Long> {

    @Query("select repre from Representante repre left join repre.institucion ins where ins.id_institucion=?1")
    public List<Representante> ReprePorIdInstitu(Long id_institucion);
}
