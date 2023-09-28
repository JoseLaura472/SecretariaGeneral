package com.example.Proyecto.Controller.PersonaControllers;

import java.util.ArrayList;
import java.util.List;

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

import com.example.Proyecto.Models.Entity.Persona;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @RequestMapping(value = "PersonaR", method = RequestMethod.GET)
    public String PersonaR(@Validated Persona persona, Model model) throws Exception {

        List<Persona> personas = personaService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Persona persona2 : personas) {
            String id_encryptado = Encryptar.encrypt(Long.toString(persona2.getId_persona()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("personas", personas);
        model.addAttribute("id_encryptado", encryptedIds);

        return "persona/persona-adm";
    }

    @RequestMapping(value = "/editar-persona/{id_persona}")
    public String editar_c(@PathVariable("id_persona") String id_persona, Model model) {
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
    }

    @RequestMapping(value = "/PersonaModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String tpconvenio_mod(HttpServletRequest request, @Validated Persona persona,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        persona.setId_usu(usuario.getId_usuario());
        persona.setEstado_persona("A");
        personaService.save(persona);
        return "redirect:/adm/PersonaR";
    }

    @RequestMapping(value = "/eliminar-persona/{id_persona}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_persona") String id_persona)
            throws Exception {
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
