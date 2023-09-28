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
@Table(name = "consejo")
@Setter
@Getter
public class Consejo extends SigaUsicRevisiones{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_consejo;

    private String nombre_consejo;
    private String sigla_consejo;
    private String estado_consejo;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consejo", fetch = FetchType.EAGER)
    private List<Autoridad> autoridad;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consejo", fetch = FetchType.EAGER)
    private List<Convenio> convenio;

      @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consejo", fetch = FetchType.EAGER)
    private List<Usuario> usuario;
}
