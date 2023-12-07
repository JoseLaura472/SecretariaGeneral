package com.example.Proyecto.Controller.PersonaControllers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.IService.IUsuarioService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IResolucionService resolucionService;

    @RequestMapping(value = "PersonaR", method = RequestMethod.GET)
    public String ConsejoR(HttpServletRequest request, @Validated Persona persona, Model model) throws Exception {

        if (request.getSession().getAttribute("usuario") != null) {

            List<Persona> personas = personaService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Persona persona2 : personas) {
                String id_encryptado = Encryptar.encrypt(Long.toString(persona2.getId_persona()));
                encryptedIds.add(id_encryptado);
            }
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
            List<Resolucion> resoluciones;
            if (usuario.getEstado().equals("AU")) {
                resoluciones = resolucionService.findAll();
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(consejo.getId_consejo());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("years", years);
            model.addAttribute("persona", new Persona());
            model.addAttribute("personas", personas);
            model.addAttribute("id_encryptado", encryptedIds);

            return "persona/persona-adm";

        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "PersonaF", method = RequestMethod.POST)
    public String TipoConvenioF(HttpServletRequest request, @Validated Persona persona) {

        if (request.getSession().getAttribute("usuario") != null) {

            String correo = persona.getEmail_persona();
            String numeroCarnet = persona.getCi_persona();

            persona.setEstado_persona("A");
            personaService.save(persona);

            Usuario usuario = new Usuario();
            usuario.setUsuario_nom(correo);
            usuario.setContrasena(numeroCarnet);
            usuario.setEstado("I");
            usuario.setConsejo(consejoService.findOne(1L));
            usuario.setPersona(persona);
            usuarioService.save(usuario);

            return "redirect:/adm/PersonaR";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/editar-persona/{id_persona}")
    public String editar_c(@PathVariable("id_persona") String id_persona, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_per = Long.parseLong(Encryptar.decrypt(id_persona));
                Persona persona = personaService.findOne(id_per);
                model.addAttribute("persona", persona);

                List<Persona> personas = personaService.findAll();
                List<String> encryptedIds = new ArrayList<>();
                for (Persona persona2 : personas) {
                    String id_encryptado = Encryptar.encrypt(Long.toString(persona2.getId_persona()));
                    encryptedIds.add(id_encryptado);
                }
                model.addAttribute("personas", personas);
                model.addAttribute("id_encryptado", encryptedIds);
                return "persona/persona-adm";

            } catch (Exception e) {

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/PersonaModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String tpconvenio_mod(HttpServletRequest request, @Validated Persona persona,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            persona.setId_usu(usuario.getId_usuario());
            persona.setEstado_persona("A");
            personaService.save(persona);
            return "redirect:/adm/PersonaR";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/eliminar-persona/{id_persona}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_persona") String id_persona)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_per = Long.parseLong(Encryptar.decrypt(id_persona));
                Persona persona = personaService.findOne(id_per);
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                persona.setId_usu(usuario.getId_usuario());
                persona.setEstado_persona("X");
                personaService.save(persona);
                return "redirect:/adm/PersonaR";
            } catch (Exception e) {
                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tablePersonas")
    public String tableConsejos(@Validated Persona persona, Model model) throws Exception {

        List<Persona> personas = personaService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Persona persona2 : personas) {
            String id_encryptado = Encryptar.encrypt(Long.toString(persona2.getId_persona()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("personas", personas);
        model.addAttribute("id_encryptado", encryptedIds);

        return "persona/tableFragmentPer :: table";
    }

}
