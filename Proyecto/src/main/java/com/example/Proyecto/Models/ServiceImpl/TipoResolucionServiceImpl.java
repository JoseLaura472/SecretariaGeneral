package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.ITipoResolucionDao;
import com.example.Proyecto.Models.Entity.TipoResolucion;
import com.example.Proyecto.Models.IService.ITipoResolucionService;

@Service
public class TipoResolucionServiceImpl implements ITipoResolucionService{

    @Autowired
    private ITipoResolucionDao tipoResolucionDao;

    @Override
    public List<TipoResolucion> findAll() {
        return (List<TipoResolucion>) tipoResolucionDao.findAll();
    }

    @Override
    public void save(TipoResolucion tipoResolucion) {
        tipoResolucionDao.save(tipoResolucion);
    }

    @Override
    public TipoResolucion findOne(Long id) {
        return tipoResolucionDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tipoResolucionDao.deleteById(id);
    }
    
}
