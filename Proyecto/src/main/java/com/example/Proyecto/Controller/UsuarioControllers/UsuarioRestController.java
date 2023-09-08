package com.example.Proyecto.Controller.UsuarioControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IUsuarioService;

@RestController
@CrossOrigin
public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping(value = "LoginF")
    public String PersonaF(HttpServletRequest request,
            @RequestParam(name = "usuario_nom", required = false) String usuario_nom,
            @RequestParam(name = "contrasena", required = false) String contrasena) {

        Long resultado = usuarioService.validar_adm(usuario_nom, contrasena);

        if (resultado != null) {
            Usuario usuario = usuarioService.findOne(resultado);

            if (usuario.getEstado().equals("A")) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("persona", usuario.getPersona());
                return "A";
            } else {
                if (usuario.getEstado().equals("AD")) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuario", usuario);
                    session.setAttribute("persona", usuario.getPersona());
                    return "AD";
                }
                if (usuario.getEstado().equals("AU")) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuario", usuario);
                    session.setAttribute("persona", usuario.getPersona());
                    return "AU";
                } else {
                    return "2";
                }
            }

        } else {
            return "2";
        }
    }
}
