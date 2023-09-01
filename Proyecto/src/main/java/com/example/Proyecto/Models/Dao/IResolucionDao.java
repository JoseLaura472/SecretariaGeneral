package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.Entity.Resolucion;

public interface IResolucionDao extends CrudRepository<Resolucion, Long>{

    @Query("select a from Resolucion a left join a.consejo c where c.id_consejo=?1")
    public List<Resolucion> resolucionPorIdConsejo(Long id_consejo);
}
