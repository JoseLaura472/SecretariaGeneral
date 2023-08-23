package com.example.Proyecto.Models.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "institucion")
@Setter
@Getter
public class Institucion extends SigaUsicRevisiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_institucion;

    private String nombre_institucion;
    private String sigla_institucion;
    private String estado_institucion;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institucion", fetch = FetchType.EAGER)
    private List<Convenio> convenios;

}
