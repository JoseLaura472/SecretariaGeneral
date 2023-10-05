package com.example.Proyecto.Models.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Resolucion;

public interface IResolucionDao extends CrudRepository<Resolucion, Long>{

    @Query("select a from Resolucion a left join a.consejo c where c.id_consejo=?1")
    public List<Resolucion> resolucionPorIdConsejo(Long id_consejo);
    /* 
    @Query(value = "SELECT * FROM resolucion as re INNER JOIN consejo as cj ON re.id_consejo = cj.id_consejo \r\n" + //
            "INNER JOIN autoridad as au ON re.id_autoridad = au.id_autoridad \r\n" + //
            "INNER JOIN persona as per ON au.id_persona = per.id_persona \r\n" + //
            "WHERE cj.id_consejo= ?1 AND au.id_autoridad= ?2 ", nativeQuery = true)
    public List<Resolucion> listarResolucionConsejoAutoridad(Long id_consejo, Long id_autoridad);
    */

    @Query(value = "select * from resolucion as res where res.id_autoridad=?1 AND res.id_consejo=?2", nativeQuery = true)
    public List<Resolucion> resolucionPorAutoridadConsejo(Long id_autoridad, Long id_consejo);

    @Query(value = "SELECT * FROM resolucion AS r WHERE r.fecha_resolucion BETWEEN ?1 AND ?2 AND r.id_consejo = ?3", nativeQuery = true)
    public List<Resolucion> buscarResolucionesPorIntervaloDeFechas(Date fechaInicio, Date fechaFin, Long id_consejo);
}
