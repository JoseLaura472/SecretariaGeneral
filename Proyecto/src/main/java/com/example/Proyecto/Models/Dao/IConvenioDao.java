package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.Entity.Convenio;

public interface IConvenioDao extends CrudRepository<Convenio, Long> {



    @Query("select a from Convenio a left join a.consejo c where c.id_consejo=?1")
    public List<Convenio> convenioPorIdConsejo(Long id_consejo);

}
