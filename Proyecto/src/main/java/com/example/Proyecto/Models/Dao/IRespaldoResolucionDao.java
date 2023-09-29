package com.example.Proyecto.Models.Dao;

import java.util.List;

import com.example.Proyecto.Models.Entity.RespaldoResolucion;



public interface IRespaldoResolucionDao {

    public RespaldoResolucion registrarArchivoAdjunto(RespaldoResolucion respaldoResolucion);

    public RespaldoResolucion buscarArchivoAdjunto(Long id_respaldo_resolucion);

     public RespaldoResolucion buscarArchivoAdjuntoPorResolucion(Long id_resolucion);

    public void modificarArchivoAdjunto(RespaldoResolucion respaldoResolucion);

    public List<RespaldoResolucion> listarArchivoAdjuntoJPQL();
}
