package com.example.Proyecto.Models.IService;

import java.util.List;

import com.example.Proyecto.Models.Entity.Usuario;

public interface IUsuarioService {
    public List<Usuario> findAll();

    public void save(Usuario usuario);

    public Usuario findOne(Long id);

    public void delete(Long id);

    public Long insertar_adm(String usuario_nom, String contrasena, Integer id_persona, Integer id_consejo);

    public Long validar_adm(String usuario_nom, String contrasena);

    public Usuario getUsuarioContraseña(String correo, String password);
}
