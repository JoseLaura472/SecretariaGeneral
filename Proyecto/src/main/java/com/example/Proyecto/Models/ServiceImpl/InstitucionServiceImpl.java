package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IInstitucionDao;
import com.example.Proyecto.Models.Entity.Institucion;
import com.example.Proyecto.Models.IService.IInstitucionService;

@Service
public class InstitucionServiceImpl implements IInstitucionService {

    @Autowired
    private IInstitucionDao institucionDao;

    @Override
    public List<Institucion> findAll() {
        return (List<Institucion>) institucionDao.findAll();
    }

    @Override
    public void save(Institucion institucion) {
        institucionDao.save(institucion);
    }

    @Override
    public Institucion findOne(Long id) {
        return institucionDao.findById(id).orElse(null);

    }

    @Override
    public void delete(Long id) {
        institucionDao.deleteById(id);
    }

}
