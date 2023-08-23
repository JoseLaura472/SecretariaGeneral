package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IPersonaDao;
import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.IService.IPersonaService;

@Service
public class PersonaServiceImpl implements IPersonaService {

    @Autowired
    private IPersonaDao personaDao;

    @Override
    public List<Persona> findAll() {
        return (List<Persona>) personaDao.findAll();
    }

    @Override
    public void save(Persona persona) {
        personaDao.save(persona);
    }

    @Override
    public Persona findOne(Long id) {
        return personaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        personaDao.deleteById(id);
    }

}
