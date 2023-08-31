package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Autoridad;

public interface IAutoridadDao extends CrudRepository<Autoridad, Long> {

    @Query("select a from Autoridad a left join a.consejo c where c.id_consejo=?1")
    public List<Autoridad> autoridadPorIdConsejo(Long id_consejo);
}