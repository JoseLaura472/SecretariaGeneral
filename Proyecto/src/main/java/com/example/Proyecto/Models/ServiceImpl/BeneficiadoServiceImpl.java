package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.Proyecto.Models.Dao.IBeneficiadoDao;
import com.example.Proyecto.Models.Entity.Beneficiado;
import com.example.Proyecto.Models.IService.IBeneficiadoService;

public class BeneficiadoServiceImpl implements IBeneficiadoService{

    @Autowired
    private IBeneficiadoDao beneficiadoDao;

    @Override
    public List<Beneficiado> findAll() {
        return (List<Beneficiado>) beneficiadoDao.findAll();
    }

    @Override
    public void save(Beneficiado beneficiado) {
        beneficiadoDao.save(beneficiado);
    }

    @Override
    public Beneficiado findOne(Long id) {
        return beneficiadoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        beneficiadoDao.deleteById(id);
    }
    
}
