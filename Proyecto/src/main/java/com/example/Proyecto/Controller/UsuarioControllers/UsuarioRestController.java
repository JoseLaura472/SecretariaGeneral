package com.example.Proyecto.Controller.UsuarioControllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Dao.IPersonaDao;
import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IUsuarioService;

@Controller
public class UsuarioRestController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private IPersonaDao personaDao;

	@Autowired
	private IConsejoService consejoService;

	@RequestMapping(value = "/LogearseCa", method = RequestMethod.POST)
	public String logearseCa(Model model, HttpServletRequest request,
			@RequestParam(name = "usuario_nom", required = false) String usuario_nom,
			@RequestParam(name = "contrasena", required = false) String contrasena, RedirectAttributes flash)
			throws ParseException {

		Map<String, Object> requests = new HashMap<String, Object>();

		requests.put("usuario", usuario_nom);
		requests.put("contrasena", contrasena);

		//String url = "http://localhost:3333/api/londraPost/v1/obtenerDatos";
		String url = "http://virtual.uap.edu.bo:7174/api/londraPost/v1/obtenerDatos";

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<HashMap> req = new HttpEntity(requests, headers);

		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);	
			System.out.println(resp.getBody().get("status").toString());

			Persona per = new Persona();
			per.setCi_persona(resp.getBody().get("per_num_doc").toString());
			per.setNombre_persona(resp.getBody().get("per_nombres").toString());
			per.setAp_paterno_persona(resp.getBody().get("per_ap_paterno").toString());
			per.setAp_materno_persona(resp.getBody().get("per_ap_materno").toString());
			per.setEstado_persona("A");
			System.out.println(per.getCi_persona());
			Persona pe = personaDao.getPersonaCI(resp.getBody().get("per_num_doc").toString());

			if (pe == null && resp.getBody().get("status").toString().equals("200")) {
			Persona persona = new Persona();
			persona.setCi_persona(resp.getBody().get("per_num_doc").toString());
			persona.setNombre_persona(resp.getBody().get("per_nombres").toString());
			persona.setAp_paterno_persona(resp.getBody().get("per_ap_paterno").toString());
			persona.setAp_materno_persona(resp.getBody().get("per_ap_materno").toString());
			persona.setEmail_persona(resp.getBody().get("perd_email_personal").toString());
			persona.setTelefono_persona(resp.getBody().get("perd_celular").toString());

			// String dDate = resp.getBody().get("fecha_nac").toString();
			// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// Date cDate = df.parse(dDate);
			// persona.setFec_nacimiento(cDate);

			persona.setSexo_persona(resp.getBody().get("per_sexo").toString());
			persona.setDependencia_persona(resp.getBody().get("eo_descripcion").toString());
			persona.setEstado_persona("A");
			personaService.save(persona);

			Usuario usuario = new Usuario();
			usuario.setUsuario_nom(usuario_nom);
			usuario.setUsuario_codigo(contrasena);
			usuario.setPersona(persona);
			usuario.setConsejo(consejoService.findOne(1L));
			usuario.setEstado("I");
			usuarioService.save(usuario);

			HttpSession session = request.getSession(true);
			session.setAttribute("usuario", usuario);
			session.setAttribute("persona", usuario.getPersona());

			flash.addAttribute("success", usuario.getPersona().getNombre_persona());

			return "redirect:/adm/InicioAdm";

		} else if (pe.getCi_persona().equals(per.getCi_persona())) {
	
			Usuario usuario = usuarioService.getUsuarioContraseña(usuario_nom, contrasena);
			if (usuario != null) {
			HttpSession session = request.getSession(true);
			System.out.println("existe la persona desde API "+usuario.getPersona().getNombre_persona());

			session.setAttribute("usuario", usuario);
			session.setAttribute("persona", usuario.getPersona());

			flash.addAttribute("success", usuario.getPersona().getNombre_persona());

			return "redirect:/adm/InicioAdm";	
			}else{
			Usuario usuario2 = usuarioService.getUsuarioContraseña2(usuario_nom, contrasena);
			
			if (usuario2 != null) {
			HttpSession session = request.getSession(true);
			System.out.println("existe la persona desde credenciales modificadas "+usuario2.getPersona().getNombre_persona());

			session.setAttribute("usuario", usuario2);
			session.setAttribute("persona", usuario2.getPersona());

			flash.addAttribute("success", usuario2.getPersona().getNombre_persona());
			return "redirect:/adm/InicioAdm";	
			}	
			}
		
		}


		} catch (HttpServerErrorException.InternalServerError e) {
			Usuario usuario = usuarioService.getUsuarioContraseña2(usuario_nom, contrasena);
			
			if (usuario != null) {
			HttpSession session = request.getSession(true);
			System.out.println("existe la persona desde credenciales modificadas "+usuario.getPersona().getNombre_persona());

			session.setAttribute("usuario", usuario);
			session.setAttribute("persona", usuario.getPersona());

			flash.addAttribute("success", usuario.getPersona().getNombre_persona());
			return "redirect:/adm/InicioAdm";	
			}
			
		}
		System.out.println("Error en las Credenciales");
		
		return "redirect:/";
	}
}
