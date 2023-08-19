package com.example.Proyecto.Controller.PersonaControllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.Proyecto.Models.Entity.Persona;
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

    
}
