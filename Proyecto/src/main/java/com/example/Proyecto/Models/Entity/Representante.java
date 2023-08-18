package com.example.Proyecto.Models.Entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "representante")
@Setter
@Getter
public class Representante extends SigaUsicRevisiones{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_representante;

     @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_inicio;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_final;

    private String estado_representante;


    //Tabla Persona
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "representante", fetch = FetchType.EAGER)
    private List<Convenio> convenio;

}
