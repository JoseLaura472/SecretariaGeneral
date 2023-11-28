package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.TipoResolucion;

public interface ITipoResolucionService {
    public List<TipoResolucion> findAll();

    public void save(TipoResolucion tipoResolucion);

    public TipoResolucion findOne(Long id);

    public void delete(Long id);

    public List<TipoResolucion> tpResolucionPorIdConsejo(Long id_consejo);
}
