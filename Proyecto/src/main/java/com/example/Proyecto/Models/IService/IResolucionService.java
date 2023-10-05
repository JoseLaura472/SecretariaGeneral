package com.example.Proyecto.Models.IService;

import java.util.Date;
import java.util.List;

import com.example.Proyecto.Models.Entity.Resolucion;

public interface IResolucionService {

    public List<Resolucion> findAll();

    public void save(Resolucion resolucion);

    public Resolucion findOne(Long id);

    public void delete(Long id);

    public List<Resolucion> resolucionPorIdConsejo(Long id_consejo);

    //public List<Resolucion> listarResolucionConsejoAutoridad(Long id_consejo, Long id_autoridad);
    public List<Resolucion> resolucionPorAutoridadConsejo(Long id_autoridad, Long id_consejo);

    public List<Resolucion> buscarResolucionesPorIntervaloDeFechas(Date fechaInicio, Date fechaFin, Long id_consejo);
}
