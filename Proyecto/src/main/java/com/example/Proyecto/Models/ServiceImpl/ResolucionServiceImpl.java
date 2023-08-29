package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IResolucionDao;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.IService.IResolucionService;

@Service
public class ResolucionServiceImpl implements IResolucionService {

    @Autowired
   private IResolucionDao resolucionDao;

    @Override
    public List<Resolucion> findAll() {
     return (List<Resolucion>) resolucionDao.findAll();
    }

    @Override
    public void save(Resolucion resolucion) {
        resolucionDao.save(resolucion);
    }

    @Override
    public Resolucion findOne(Long id) {
        return resolucionDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        resolucionDao.deleteById(id);
    }
    
}
