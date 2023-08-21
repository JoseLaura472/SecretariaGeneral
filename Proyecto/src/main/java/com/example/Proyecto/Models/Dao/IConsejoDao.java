package com.example.Proyecto.Models.Dao;

import org.springframework.data.repository.CrudRepository;


import com.example.Proyecto.Models.Entity.Consejo;

public interface IConsejoDao extends CrudRepository<Consejo, Long> {
    
}
