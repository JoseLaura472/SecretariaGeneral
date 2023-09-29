package com.example.Proyecto.Models.ServiceImpl;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IArchivoAdjuntoDao;
import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.RespaldoResolucion;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;

@Service
@Transactional
public class ArchivoAdjuntoServiceImpl implements IArchivoAdjuntoService {

     @Autowired
     private IArchivoAdjuntoDao archivoAdjuntoDao;

    @Override
    public List<ArchivoAdjunto> listarArchivoAdjunto() {
        return archivoAdjuntoDao.listarArchivoAdjuntoJPQL();
    }

    @Override
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        return archivoAdjuntoDao.registrarArchivoAdjunto(archivoAdjunto);
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto) {
        return archivoAdjuntoDao.buscarArchivoAdjunto(id_archivo_adjunto);
    }

    @Override
    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        archivoAdjuntoDao.modificarArchivoAdjunto(archivoAdjunto);
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorConvenio(Long id_convenio) {
        return archivoAdjuntoDao.buscarArchivoAdjuntoPorConvenio(id_convenio);
    }

    @Override
    public ArchivoAdjunto buscarArchivoAdjuntoPorResolucion(Long id_resolucion) {
        return archivoAdjuntoDao.buscarArchivoAdjuntoPorResolucion(id_resolucion);
    }

  

 

 
    
}

