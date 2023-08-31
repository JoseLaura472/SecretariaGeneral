package com.example.Proyecto.Controller.UsuarioControllers;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IUsuarioService;

@Controller
public class UsuarioController {

	@Autowired
    private IUsuarioService usuarioService;

	@Autowired
    private IConsejoService consejoService;

	@Autowired
    private IPersonaService personaService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() throws Exception {
        return "login/login-main";
    }

    @RequestMapping("/cerrar_sesionAdm")
	public String cerrarSesionAdm(HttpServletRequest request, RedirectAttributes flash) {
		HttpSession sessionAdministrador = request.getSession();
		if (sessionAdministrador != null) {
			sessionAdministrador.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/";
	}

	// Funcion de visualizacion de iniciar sesiòn administrador
	@RequestMapping(value = "/LoginR", method = RequestMethod.GET)
	public String LoginR(Model model) {

		return "login/login-main";
	}

	// FUNCION PARA LISTAR LOS REGISTRO DE USUARIOS
    @RequestMapping(value = "/UsuarioL", method = RequestMethod.GET) // Pagina principal
    public String UsuarioL(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
          model.addAttribute("usuarios", usuarioService.findAll());

        return "usuario/listar-usuario";
        }else {
            return "redirect:/";
        }
      


    }

	    @RequestMapping(value = "UsuarioForm", method = RequestMethod.GET)
    public String UsuarioForm(HttpServletRequest request, @Validated Usuario usuario, Model model) throws Exception {
         if (request.getSession().getAttribute("usuario") != null) {
           List<Usuario> usuarios = usuarioService.findAll();
        
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("consejos", consejoService.findAll());
		model.addAttribute("personas", personaService.findAll());


        return "usuario/gestionar-usuario";
        }else{
         return "redirect:/";
        }
      

    }

	@RequestMapping(value = "/editar-usuario/{id_usuario}")
    public String editar_usu(@PathVariable("id_usuario") Long id_usuario, Model model,HttpServletRequest request)
            throws NumberFormatException, Exception {
          if (request.getSession().getAttribute("usuario") != null) {
			Usuario usuario = usuarioService.findOne(id_usuario);
       
        model.addAttribute("usuario", usuario);

        List<Usuario> usuarios = usuarioService.findAll();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("consejos", consejoService.findAll());
		 model.addAttribute("personas", personaService.findAll());

        return "usuario/gestionar-usuario";
        
        }else{
         return "redirect:/";
        }
      
       

    }



	 @PostMapping(value = "/UsuarioModF")
    public String UsuarioModF(@Validated Usuario usuario, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request)
            throws IOException {

		
        usuarioService.save(usuario);
        return "redirect:/UsuarioL";

        
}


@RequestMapping(value = "/eliminar-usuario/{id_usuario}")
public String eliminar_usu(HttpServletRequest request, @PathVariable("id_usuario") Long id_usuario)
		throws Exception {
		  if (request.getSession().getAttribute("usuario") != null) {
			 Usuario usuario = usuarioService.findOne(id_usuario);
	   
		usuario.setEstado("X");
		usuarioService.save(usuario);

		return "redirect:/UsuarioL";
		  }else{
		return "redirect:/";
		  }
	   
	}

}
