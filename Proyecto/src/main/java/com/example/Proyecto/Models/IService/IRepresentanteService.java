package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Representante;

public interface IRepresentanteService {

    public List<Representante> findAll();

    public void save(Representante representante);

    public Representante findOne(Long id);

    public void delete(Long id);
}
