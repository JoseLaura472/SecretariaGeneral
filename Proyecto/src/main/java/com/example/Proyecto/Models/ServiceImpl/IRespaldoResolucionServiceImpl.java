package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IRespaldoResolucionDao;
import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.RespaldoResolucion;
import com.example.Proyecto.Models.IService.IRespaldoResolucionService;

@Service
@Transactional
public class IRespaldoResolucionServiceImpl implements IRespaldoResolucionService{

    @Autowired
    private IRespaldoResolucionDao respaldoResolucionDao;

 

    @Override
    public RespaldoResolucion registrarArchivoAdjunto(RespaldoResolucion respaldoResolucion) {
        return respaldoResolucionDao.registrarArchivoAdjunto(respaldoResolucion);
    }

    @Override
    public void modificarArchivoAdjunto(RespaldoResolucion respaldoResolucion) {
        respaldoResolucionDao.modificarArchivoAdjunto(respaldoResolucion);
    }

    @Override
    public List<RespaldoResolucion> listarArchivoAdjuntoJPQL() {
          return respaldoResolucionDao.listarArchivoAdjuntoJPQL();
    }

    @Override
    public RespaldoResolucion buscarArchivoAdjunto(Long id_respaldo_resolucion) {
        return respaldoResolucionDao.buscarArchivoAdjunto(id_respaldo_resolucion);
    }

    @Override
    public RespaldoResolucion buscarArchivoAdjuntoPorResolucion(Long id_resolucion) {
        return respaldoResolucionDao.buscarArchivoAdjuntoPorResolucion(id_resolucion);
    }


}
