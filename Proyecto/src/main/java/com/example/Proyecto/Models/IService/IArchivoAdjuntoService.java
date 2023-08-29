package com.example.Proyecto.Models.IService;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;

public interface IArchivoAdjuntoService {

	public List<ArchivoAdjunto> listarArchivoAdjunto();
    public ArchivoAdjunto registrarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjunto(Long id_archivo_adjunto);

    public void modificarArchivoAdjunto(ArchivoAdjunto archivoAdjunto);

    public ArchivoAdjunto buscarArchivoAdjuntoPorConvenio(Long id_convenio);
}
