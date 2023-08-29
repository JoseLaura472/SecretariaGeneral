package com.example.Proyecto.Models.Dao;


import java.util.List;



import com.example.Proyecto.Models.Entity.ArchivoAdjunto;



public interface IArchivoAdjuntoDao {
    
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto);

    public ArchivoAdjunto buscarArchivoAdjuntoPorConvenio(Long id_convenio);

    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public List<ArchivoAdjunto> listarArchivoAdjuntoJPQL();


}
