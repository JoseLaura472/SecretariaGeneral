package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Beneficiado;

public interface IBeneficiadoService {
    public List<Beneficiado> findAll();

    public void save(Beneficiado beneficiado);

    public Beneficiado findOne(Long id);

    public void delete(Long id);
}
