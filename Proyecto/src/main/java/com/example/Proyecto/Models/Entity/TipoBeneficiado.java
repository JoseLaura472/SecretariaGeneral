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
@Table(name = "tipo_beneficiado")
@Setter
@Getter
public class TipoBeneficiado extends SigaUsicRevisiones{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_beneficiado;

    private String nombre_tipo_beneficiado;
    private String estado_tipo_beneficiado;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoBeneficiado", fetch = FetchType.EAGER)
    private List<Beneficiado> beneficiados;
}
