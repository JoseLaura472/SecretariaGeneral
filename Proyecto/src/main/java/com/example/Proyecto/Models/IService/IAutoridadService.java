package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Autoridad;

public interface IAutoridadService {
    
    public List<Autoridad> findAll();

	public void save(Autoridad autoridad);

	public Autoridad findOne(Long id);

    public void delete(Long id);
}
