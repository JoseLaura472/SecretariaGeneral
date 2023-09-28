package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.ITipoBeneficiadoDao;
import com.example.Proyecto.Models.Entity.TipoBeneficiado;
import com.example.Proyecto.Models.IService.ITipoBeneficiadoService;

@Service
public class TipoBeneficiadoServiceImpl implements ITipoBeneficiadoService{

    @Autowired
    private ITipoBeneficiadoDao tipoBeneficiadoDao;

    @Override
    public List<TipoBeneficiado> findAll() {
        return (List<TipoBeneficiado>) tipoBeneficiadoDao.findAll();
    }

    @Override
    public void save(TipoBeneficiado tipoBeneficiado) {
        tipoBeneficiadoDao.save(tipoBeneficiado);
    }

    @Override
    public TipoBeneficiado findOne(Long id) {
        return tipoBeneficiadoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tipoBeneficiadoDao.deleteById(id);
    }
    
}
