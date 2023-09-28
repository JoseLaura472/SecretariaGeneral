package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.TipoBeneficiado;

public interface ITipoBeneficiadoService {
    public List<TipoBeneficiado> findAll();

    public void save(TipoBeneficiado tipoBeneficiado);

    public TipoBeneficiado findOne(Long id);

    public void delete(Long id);
}
