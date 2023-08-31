package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IAutoridadDao;
import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.IService.IAutoridadService;

@Service
public class AutoridadServiceImpl implements IAutoridadService {

    @Autowired
    private IAutoridadDao autoridadDao;

    @Override
    public List<Autoridad> findAll() {
        return (List<Autoridad>) autoridadDao.findAll();
    }

    @Override
    public void save(Autoridad autoridad) {
        autoridadDao.save(autoridad);
    }

    @Override
    public Autoridad findOne(Long id) {
        return autoridadDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        autoridadDao.deleteById(id);
    }

    @Override
    public List<Autoridad> autoridadPorIdConsejo(Long id_consejo) {
        return (List<Autoridad>) autoridadDao.autoridadPorIdConsejo(id_consejo);
    }

}
