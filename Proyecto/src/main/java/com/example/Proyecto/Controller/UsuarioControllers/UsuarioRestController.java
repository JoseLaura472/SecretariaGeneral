package com.example.Proyecto.Controller.UsuarioControllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IUsuarioService;

@Controller
public class UsuarioRestController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IPersonaService personaService;

	@RequestMapping(value = "/LogearseCa", method = RequestMethod.POST)
	public String logearseCa(Model model, HttpServletRequest request,
			@RequestParam(name = "usuario_nom", required = false) String usuario_nom,
			@RequestParam(name = "contrasena", required = false) String contrasena, RedirectAttributes flash)
			throws ParseException {

		Map<String, Object> requests = new HashMap<String, Object>();

		requests.put("usuario", usuario_nom);
		requests.put("contrasena", contrasena);

		String url = "https://apirest-production-0e0a.up.railway.app/api/londraPost/v1/obtenerDatos";

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<HashMap> req = new HttpEntity(requests, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

		if (resp.getBody().get("status").toString().equals("200")) {
			Persona persona = personaService.getPersonaCI("5718210");
			if (persona != null) {
				if (persona.getUsuarios() != null) {

					Usuario usuario = usuarioService.getUsuarioContraseña(usuario_nom, contrasena);
					HttpSession session = request.getSession(true);

					session.setAttribute("usuario", usuario);
					session.setAttribute("persona", usuario.getPersona());

					flash.addAttribute("success", usuario.getPersona().getNombre_persona());

					return "redirect:/adm/InicioAdm";
				} else {
					Usuario usuario = new Usuario();
					usuario.setUsuario_nom(usuario_nom);
					usuario.setUsuario_codigo(contrasena);
					usuario.setPersona(persona);
					usuario.setEstado("A");
					usuarioService.save(usuario);

					HttpSession session = request.getSession(true);

					session.setAttribute("usuario", usuario);
					session.setAttribute("persona", usuario.getPersona());

					flash.addAttribute("success", usuario.getPersona().getNombre_persona());

					return "redirect:/adm/InicioAdm";
				}
			} else {
				persona = new Persona();
				persona.setCi_persona(resp.getBody().get("per_num_doc").toString());
				persona.setNombre_persona(resp.getBody().get("per_nombres").toString());
				persona.setAp_paterno_persona(resp.getBody().get("per_ap_paterno").toString());
				persona.setAp_materno_persona(resp.getBody().get("per_ap_materno").toString());
				persona.setEstado_persona("A");
				personaService.save(persona);

				Usuario usuario = new Usuario();
				usuario.setUsuario_nom(usuario_nom);
				usuario.setUsuario_codigo(contrasena);
				usuario.setPersona(persona);
				usuario.setEstado("A");
				usuarioService.save(usuario);

				HttpSession session = request.getSession(true);
				session.setAttribute("usuario", usuario);
				session.setAttribute("persona", usuario.getPersona());

				flash.addAttribute("success", usuario.getPersona().getNombre_persona());

				return "redirect:/adm/InicioAdm";
			}
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/LogearseC", method = RequestMethod.POST)
    public String logearseC(Model model, HttpServletRequest request,
            @RequestParam(name = "usuario_nom", required = false) String usuario_nom,
            @RequestParam(name = "contrasena", required = false) String contrasena, RedirectAttributes flash)
            throws ParseException {

        Map<String, Object> requests = new HashMap<String, Object>();

        requests.put("usuario", usuario_nom);
        requests.put("contrasena", contrasena);

        String url = "https://apirest-production-0e0a.up.railway.app/api/londraPost/v1/obtenerDatos";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity(requests, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

        if (resp.getBody().get("status").toString().equals("200")) {
            Persona persona = personaService.getPersonaCI(resp.getBody().get("per_num_doc").toString());
            if (persona != null) {
                if (persona.getUsuarios() != null) {

                    Usuario usuario = usuarioService.getUsuarioContraseña(usuario_nom, contrasena);
                    HttpSession session = request.getSession(true);

                    session.setAttribute("usuario", usuario);
                    session.setAttribute("persona", usuario.getPersona());

                    flash.addAttribute("success", usuario.getPersona().getNombre_persona());

                    return "redirect:/adm/InicioAdm";
                } else {
                    Usuario usuario = new Usuario();
                    usuario.setUsuario_nom(usuario_nom);
                    usuario.setUsuario_codigo(contrasena);
                    usuario.setPersona(persona);
                    usuario.setEstado("A");
                    usuarioService.save(usuario);

                    HttpSession session = request.getSession(true);

                    session.setAttribute("usuario", usuario);
                    session.setAttribute("persona", usuario.getPersona());

                    flash.addAttribute("success", usuario.getPersona().getNombre_persona());

                    return "redirect:/adm/InicioAdm";
                }
            } else {
                
                Usuario usuario = new Usuario();
                usuario.setUsuario_nom(usuario_nom);
                usuario.setUsuario_codigo(contrasena);
                usuario.setPersona(persona);
                usuario.setEstado("A");
                usuarioService.save(usuario);

				HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("persona", usuario.getPersona());

                flash.addAttribute("success", usuario.getPersona().getNombre_persona());

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
		}
	}

	@PostMapping(value = "LoginF")
	public String PersonaF(HttpServletRequest request,
			@RequestParam(name = "usuario_nom", required = false) String usuario_nom,
			@RequestParam(name = "contrasena", required = false) String contrasena) {

		Long resultado = usuarioService.validar_adm(usuario_nom, contrasena);

		if (resultado != null) {
			Usuario usuario = usuarioService.findOne(resultado);

			if (usuario.getEstado().equals("S")) {
				HttpSession session = request.getSession(true);
				session.setAttribute("usuario", usuario);
				session.setAttribute("persona", usuario.getPersona());
				return "S";
			} else {
				if (usuario.getEstado().equals("A")) {
					HttpSession session = request.getSession(true);
					session.setAttribute("usuario", usuario);
					session.setAttribute("persona", usuario.getPersona());
					return "A";
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
