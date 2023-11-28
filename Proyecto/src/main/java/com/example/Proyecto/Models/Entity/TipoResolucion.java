package com.example.Proyecto.Models.Entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_resolucion")
@Setter
@Getter
public class TipoResolucion extends SigaUsicRevisiones{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_resolucion;

    private String nombre_tipo_resolucion;
    private String sigla_tipo_resolucion;

    private String estado_tipo_resolucion;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoResolucion", fetch = FetchType.EAGER)
    private List<Resolucion> resoluciones;

    // Tabla Consejo
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_consejo")
    private Consejo consejo;
}
