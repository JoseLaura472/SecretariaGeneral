package com.example.Proyecto.Models.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyecto.Models.Dao.ITipoConvenioDao;

import com.example.Proyecto.Models.Entity.TipoConvenio;
import com.example.Proyecto.Models.IService.ITipoConvenioService;

@Service
public class TipoConvenioServiceImpl implements ITipoConvenioService {

    @Autowired
    private ITipoConvenioDao tipoConvenioDao;

    @Override
    public List<TipoConvenio> findAll() {
        return (List<TipoConvenio>) tipoConvenioDao.findAll();
    }

    @Override
    public void save(TipoConvenio tipoConvenio) {
        tipoConvenioDao.save(tipoConvenio);
    }

    @Override
    public TipoConvenio findOne(Long id) {
        return tipoConvenioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tipoConvenioDao.deleteById(id);
    }

}
