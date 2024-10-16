package com.example.Proyecto.Models.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "convenio")
@Setter
@Getter
public class Convenio extends SigaUsicRevisiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_convenio;

    private String titulo_convenio;
    private String numero_convenio;
    private String folio_convenio;
    
    @Column(length = 10485760)
    private String objeto_convenio;
    private String gestion_convenio;
    private String estado_convenio;

    private String ruta_marca_convenio;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_inicio;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_final;

    @Transient
    private MultipartFile file; 
    
    @Transient
    private String nombreArchivo; 

    // Tabla Autoridad
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autoridad")
    private Autoridad autoridad;

    // Tabla Representante
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_representante")
    private Representante representante;

    // Tabla TipoConvenio
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_convenio")
    private TipoConvenio tipoConvenio;

    // Tabla Institucion
    //@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "id_institucion")
    //private Institucion institucion;

    // Tabla Consejo
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consejo")
    private Consejo consejo;

    // Tabla Archivo Adjunto
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_archivo_adjunto")
    private ArchivoAdjunto archivoAdjunto;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_beneficiado")
    private Beneficiado beneficiado;

}
