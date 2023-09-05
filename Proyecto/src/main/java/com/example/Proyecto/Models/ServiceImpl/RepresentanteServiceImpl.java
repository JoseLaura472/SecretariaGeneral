package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IRepresentanteDao;
import com.example.Proyecto.Models.Entity.Representante;
import com.example.Proyecto.Models.IService.IRepresentanteService;

@Service
public class RepresentanteServiceImpl implements IRepresentanteService {

    @Autowired
    private IRepresentanteDao representanteDao;

    @Override
    public List<Representante> findAll() {
        return (List<Representante>) representanteDao.findAll();
    }

    @Override
    public void save(Representante representante) {
        representanteDao.save(representante);
    }

    @Override
    public Representante findOne(Long id) {
        return representanteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        representanteDao.deleteById(id);
    }

    @Override
    public List<Representante> ReprePorIdInstitu(Long id_institucion) {
        return (List<Representante>) representanteDao.ReprePorIdInstitu(id_institucion);
    }

}
