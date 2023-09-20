package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Consejo;

public interface IConsejoService {

    public List<Consejo> findAll();

    public void save(Consejo consejo);

    public Consejo findOne(Long id);

    public void delete(Long id);

    public List<Consejo> listarConsejoCau();

    public List<Consejo> listarConsejoFacultad();

    public List<Consejo> listarConsejoCarrera();
}
