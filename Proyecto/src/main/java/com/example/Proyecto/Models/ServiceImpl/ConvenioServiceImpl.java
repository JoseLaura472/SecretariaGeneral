package com.example.Proyecto.Models.ServiceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.IConvenioDao;
import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.IService.IConvenioService;

@Service
public class ConvenioServiceImpl implements IConvenioService {

   @Autowired
   private IConvenioDao convenioDao;

   @Override
   public List<Convenio> findAll() {
      return (List<Convenio>) convenioDao.findAll();
   }

   @Override
   public void save(Convenio convenio) {
      convenioDao.save(convenio);
   }

   @Override
   public Convenio findOne(Long id) {
      return convenioDao.findById(id).orElse(null);
   }

   @Override
   public void delete(Long id) {
      convenioDao.deleteById(id);
   }

   @Override
   public List<Convenio> convenioPorIdConsejo(Long id_consejo) {
      return (List<Convenio>) convenioDao.convenioPorIdConsejo(id_consejo);
      }

 

   @Override
   public List<Convenio> convenioPorAutoridadConsejo(Long id_autoridad, Long id_consejo) {
     return (List<Convenio>) convenioDao.convenioPorAutoridadConsejo(id_autoridad, id_consejo);
   }

   @Override
   public List<Convenio> buscarConveniosPorIntervaloDeFechas(Date fechaInicio, Date fechaFin, Long id_consejo) {
      return (List<Convenio>) convenioDao.buscarConveniosPorIntervaloDeFechas(fechaInicio, fechaFin, id_consejo);
   }

 

   
}