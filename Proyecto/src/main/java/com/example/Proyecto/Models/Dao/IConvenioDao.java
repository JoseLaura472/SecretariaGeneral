package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Convenio;

public interface IConvenioDao extends CrudRepository<Convenio, Long> {

    @Query("select a from Convenio a left join a.consejo c where c.id_consejo=?1")
    public List<Convenio> convenioPorIdConsejo(Long id_consejo);

    @Query(value = "SELECT *\r\n" + //
            "FROM convenio as con INNER JOIN consejo as cj ON con.id_consejo = cj.id_consejo \r\n" + //
            "INNER JOIN autoridad as au ON con.id_autoridad = au.id_autoridad \r\n" + //
            "INNER JOIN persona as per ON au.id_persona = per.id_persona \r\n" + //
            "WHERE cj.id_consejo= ?1 AND au.id_autoridad= ?2", nativeQuery = true)
    public List<Convenio> listarConvenioConsejoAutoridad(Long id_consejo, Long id_autoridad);

}
