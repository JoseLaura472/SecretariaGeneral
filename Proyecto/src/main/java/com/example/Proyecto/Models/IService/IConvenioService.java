package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Convenio;

public interface IConvenioService {
    public List<Convenio> findAll();

    public void save(Convenio convenio);

    public Convenio findOne(Long id);

    public void delete(Long id);

    public List<Convenio> convenioPorIdConsejo(Long id_consejo);
}
