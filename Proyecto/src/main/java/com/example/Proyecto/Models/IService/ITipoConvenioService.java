package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.TipoConvenio;

public interface ITipoConvenioService {

    public List<TipoConvenio> findAll();

    public void save(TipoConvenio tipoConvenio);

    public TipoConvenio findOne(Long id);

    public void delete(Long id);
}
