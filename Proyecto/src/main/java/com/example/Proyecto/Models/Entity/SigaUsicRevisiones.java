package com.example.Proyecto.Models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Setter
@Getter
public abstract class SigaUsicRevisiones implements Serializable {
    private static final long serialVersionUID = 2629195288020321924L;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "_registro")
    @CreatedDate
    private Date registro = new Timestamp(System.currentTimeMillis());

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "_modificacion")
    @LastModifiedDate
    private Date modificacion = new Timestamp(System.currentTimeMillis());

    @Transient // Indica que este campo no se mapea a la base de datos
    private int gestion;

    @PrePersist
    private void setGestion() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registro);
        gestion = calendar.get(Calendar.YEAR);
    }

    private Long id_usu;
}
