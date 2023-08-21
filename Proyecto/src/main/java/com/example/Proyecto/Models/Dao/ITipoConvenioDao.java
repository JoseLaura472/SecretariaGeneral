package com.example.Proyecto.Models.Dao;

import org.springframework.data.repository.CrudRepository;


import com.example.Proyecto.Models.Entity.TipoConvenio;

public interface ITipoConvenioDao extends CrudRepository<TipoConvenio, Long> {
    
}
