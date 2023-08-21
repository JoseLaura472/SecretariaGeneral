package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IConsejoDao;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.IService.IConsejoService;

@Service
public class ConsejoServiceImpl implements IConsejoService {


    @Autowired
     private IConsejoDao consejoDao;

    @Override
    public List<Consejo> findAll() {
       return (List<Consejo>) consejoDao.findAll();
    }

    @Override
    public void save(Consejo consejo) {
      consejoDao.save(consejo);
    }

    @Override
    public Consejo findOne(Long id) {
        return consejoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        consejoDao.deleteById(id);
    }
    
}
