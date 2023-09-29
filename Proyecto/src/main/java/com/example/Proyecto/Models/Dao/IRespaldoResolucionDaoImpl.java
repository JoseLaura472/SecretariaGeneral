package com.example.Proyecto.Models.Dao;

import java.util.List;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.RespaldoResolucion;

import javax.transaction.Transactional;

@Repository("IRespaldoResolucionDao")
public class IRespaldoResolucionDaoImpl implements IRespaldoResolucionDao{
    @PersistenceContext
    private javax.persistence.EntityManager em;

    @Transactional

      @Override
    public RespaldoResolucion registrarArchivoAdjunto(RespaldoResolucion respaldoResolucion) {
        em.persist(respaldoResolucion);
        return respaldoResolucion;
    }

    @Override
    public RespaldoResolucion buscarArchivoAdjunto(Long id_respaldo_resolucion) {
        String sql = " SELECT arc "
        + " FROM RespaldoResolucion arc "
        + " WHERE arc.id_respaldo_resolucion =:id_respaldo_resolucion"
        + " AND arc.estado_archivo_Adjunto = 'A' ";
        Query q = em.createQuery(sql);
        q.setParameter("id_respaldo_resolucion", id_respaldo_resolucion);
        try {
        return (RespaldoResolucion) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }

   

     @Override
    public RespaldoResolucion buscarArchivoAdjuntoPorResolucion(Long id_resolucion) {

        String sql = "SELECT gaa  "
        + " FROM Resolucion tr LEFT JOIN  tr.respaldoResolucion gaa"
        + " WHERE tr.id_resolucion =:id_resolucion "
        + " AND gaa.estado_archivo_adjunto = 'A' ";
        /*String sql = "select gaa from pasarela_tramite tr, pasarela_archivo_adjunto ar WHERE tr.id_archivo_adjunto=ar.id_archivo_adjunto and tr.estado='A' and tr.id_tramite=:id_tramite;";*/
        Query q = em.createQuery(sql);
        q.setParameter("id_resolucion", id_resolucion);
        try {
        return (RespaldoResolucion) q.getSingleResult();
        } catch (Exception e) {
        return null;
        }
    }

    @Override
    public void modificarArchivoAdjunto(RespaldoResolucion respaldoResolucion) {
        em.merge(respaldoResolucion);
    }

    @Override
    public List<RespaldoResolucion> listarArchivoAdjuntoJPQL() {
        String sql = "SELECT adj "
        + " FROM RespaldoResolucion adj "
        + " WHERE adj.estado_archivo_adjunto = 'A' ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
}
