package com.example.Proyecto.Controller.PersonaControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IUsuarioService;

@Controller
public class PersonaRestController {

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private IUsuarioService usuarioService;

	@RequestMapping(value = "/PersonaF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
	public String PersonaF(@Validated Persona persona) { // validar los datos capturados (1)

		persona.setEstado_persona("A");
		personaService.save(persona);

		usuarioService.insertar_adm(persona.getEmail_persona(), persona.getCi_persona(),
				Math.toIntExact(persona.getId_persona()));

		return "redirect:/adm/PersonaR";
	}
}
