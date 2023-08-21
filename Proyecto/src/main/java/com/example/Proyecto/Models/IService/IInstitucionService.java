package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Institucion;

public interface IInstitucionService {

    public List<Institucion> findAll();

	public void save(Institucion institucion);

	public Institucion findOne(Long id);

    public void delete(Long id);
}
