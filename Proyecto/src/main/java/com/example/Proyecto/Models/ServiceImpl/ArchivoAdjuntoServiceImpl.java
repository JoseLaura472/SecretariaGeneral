package com.example.Proyecto.Models.ServiceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IArchivoAdjuntoDao;
import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;


@Service
public class ArchivoAdjuntoServiceImpl implements IArchivoAdjuntoService {

     @Autowired
     private IArchivoAdjuntoDao archivoAdjuntoDao;

    @Override
    public List<ArchivoAdjunto> findAll() {
         return (List<ArchivoAdjunto>) archivoAdjuntoDao.findAll();
    }

    @Override
    public void save(ArchivoAdjunto archivoAdjunto) {
        archivoAdjuntoDao.save(archivoAdjunto);
    }

    @Override
    public ArchivoAdjunto findOne(Long id) {
        return archivoAdjuntoDao.findById(id).orElse(null);
    }

 
    
}

