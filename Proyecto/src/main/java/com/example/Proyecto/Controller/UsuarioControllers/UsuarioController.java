package com.example.Proyecto.Controller.UsuarioControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {
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

}
