package com.example.Proyecto.Models.Entity;

import java.util.Date;

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
@Table(name = "resolucion")
@Setter
@Getter
public class Resolucion extends SigaUsicRevisiones{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_resolucion;

    private String numero_resolucion;
    private String folio_resolucion;
    private String objeto_resolucion;
    private String gestion_resolucion;
    private String estado_resolucion;
    private String ruta_marca_resolucion;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date fecha_resolucion;

    @Transient
    private MultipartFile file; 
    
    @Transient
    private String nombreArchivo; 

    // Tabla Autoridad
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_autoridad")
    private Autoridad autoridad;

    // Tabla Consejo
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_consejo")
    private Consejo consejo;
    
    // Tabla Archivo Adjunto
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_archivo_adjunto")
    private ArchivoAdjunto archivoAdjunto;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_resolucion")
    private TipoResolucion tipoResolucion;
}
