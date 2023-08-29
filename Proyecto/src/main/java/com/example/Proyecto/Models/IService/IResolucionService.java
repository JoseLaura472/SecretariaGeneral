package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Resolucion;

public interface IResolucionService {

    public List<Resolucion> findAll();

    public void save(Resolucion resolucion);

    public Resolucion findOne(Long id);

    public void delete(Long id);
}
