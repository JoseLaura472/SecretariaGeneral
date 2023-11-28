package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.Entity.TipoResolucion;

public interface ITipoResolucionDao extends CrudRepository<TipoResolucion, Long>{
    @Query(value = "select * from tipo_resolucion as tpr left join consejo as co ON tpr.id_consejo = co.id_consejo WHERE tpr.id_consejo=?1", nativeQuery = true)
    public List<TipoResolucion> tpResolucionPorIdConsejo(Long id_consejo);
}
