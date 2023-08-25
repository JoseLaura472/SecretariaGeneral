package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;

public interface IArchivoAdjuntoService {
      public List<ArchivoAdjunto> findAll();

	public void save(ArchivoAdjunto archivoAdjunto);

	public ArchivoAdjunto findOne(Long id);
}
