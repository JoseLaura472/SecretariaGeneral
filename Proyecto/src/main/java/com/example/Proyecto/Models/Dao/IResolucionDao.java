package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Resolucion;

public interface IResolucionDao extends CrudRepository<Resolucion, Long>{

    @Query("select a from Resolucion a left join a.consejo c where c.id_consejo=?1")
    public List<Resolucion> resolucionPorIdConsejo(Long id_consejo);

    @Query(value = "SELECT * \r\n" + //
            "FROM resolucion as re INNER JOIN consejo as cj ON re.id_consejo = cj.id_consejo \r\n" + //
            "INNER JOIN autoridad as au ON re.id_autoridad = au.id_autoridad \r\n" + //
            "INNER JOIN persona as per ON au.id_persona = per.id_persona \r\n" + //
            "WHERE cj.id_consejo= ?1 AND au.id_autoridad= ?2", nativeQuery = true)
    public List<Resolucion> listarResolucionConsejoAutoridad(Long id_consejo1, Long id_autoridad1);
}
