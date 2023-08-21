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
@Table(name = "autoridad")
@Setter
@Getter
public class Autoridad extends SigaUsicRevisiones{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_autoridad;

     @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_inicio;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_final;

    private String estado_autoridad;


    //Tabla Persona
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    //Tabla Consejo
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_consejo")
    private Consejo consejo;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autoridad", fetch = FetchType.EAGER)
    private List<Convenio> convenio;

}
