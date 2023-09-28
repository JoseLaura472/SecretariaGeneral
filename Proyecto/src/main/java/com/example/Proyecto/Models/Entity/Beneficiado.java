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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "beneficiado")
@Setter
@Getter
public class Beneficiado extends SigaUsicRevisiones{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_beneficiado;

    private String nombre_beneficiado;
    private String estado_beneficiado;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_beneficiado")
    private TipoBeneficiado tipoBeneficiado;
}
