package com.example.Proyecto.Models.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.Proyecto.Models.Entity.Consejo;

public interface IConsejoDao extends CrudRepository<Consejo, Long> {
    @Query(value = "SELECT * FROM consejo as cj WHERE cj.nombre_consejo IN ('CONSEJO ACADEMICO UNIVERSITARIO', 'CONSEJO FACULTATIVO', 'CONSEJO DE CARRERA');\r\n" + //
            "", nativeQuery = true)
    public List<Consejo> listarConsejoCau();

    @Query(value = "SELECT * FROM consejo as cj WHERE cj.nombre_consejo IN ('CONSEJO FACULTATIVO', 'CONSEJO DE CARRERA');\r\n" + //
            "", nativeQuery = true)
    public List<Consejo> listarConsejoFacultad();

    @Query(value = "SELECT * FROM consejo as cj WHERE cj.nombre_consejo IN ('CONSEJO DE CARRERA');\r\n" + //
            "", nativeQuery = true)
    public List<Consejo> listarConsejoCarrera();

}
