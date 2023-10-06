package com.example.Proyecto.Models.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.Entity.Resolucion;

public interface IConvenioDao extends CrudRepository<Convenio, Long> {

    @Query("select a from Convenio a left join a.consejo c where c.id_consejo=?1")
    public List<Convenio> convenioPorIdConsejo(Long id_consejo);

    /* 
    @Query(value = "SELECT *\r\n" + //
            "FROM convenio as con INNER JOIN consejo as cj ON con.id_consejo = cj.id_consejo \r\n" + //
            "INNER JOIN autoridad as au ON con.id_autoridad = au.id_autoridad \r\n" + //
            "INNER JOIN persona as per ON au.id_persona = per.id_persona \r\n" + //
            "WHERE cj.id_consejo= ?1 AND au.id_autoridad= ?2", nativeQuery = true)
    public List<Convenio> listarConvenioConsejoAutoridad(Long id_consejo, Long id_autoridad);
*/

    @Query(value = "select * from convenio as con where con.id_autoridad=?1 AND con.id_consejo=?2", nativeQuery = true)
    public List<Convenio> convenioPorAutoridadConsejo(Long id_autoridad, Long id_consejo);

    @Query(value = "SELECT * FROM convenio AS c WHERE c.fecha_inicio BETWEEN ?1 AND ?2 AND c.id_consejo = ?3", nativeQuery = true)
    public List<Convenio> buscarConveniosPorIntervaloDeFechas(Date fechaInicio, Date fechaFin, Long id_consejo);
}
