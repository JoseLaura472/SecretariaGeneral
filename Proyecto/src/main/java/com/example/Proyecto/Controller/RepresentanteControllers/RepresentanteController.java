package com.example.Proyecto.Controller.RepresentanteControllers;

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

import com.example.Proyecto.Models.Entity.Representante;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IInstitucionService;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IRepresentanteService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class RepresentanteController {

    @Autowired
    private IRepresentanteService representanteService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IInstitucionService institucionService;

    @RequestMapping(value = "/RepresentanteR", method = RequestMethod.GET) // Pagina principal
    public String Representante(@Validated Representante representante, Model model, HttpServletRequest request)
            throws Exception {

        if (request.getSession().getAttribute("persona") != null) {
            List<Representante> representantes = representanteService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Representante representante2 : representantes) {
                String id_encryptado = Encryptar.encrypt(Long.toString(representante2.getId_representante()));
                encryptedIds.add(id_encryptado);
            }

            model.addAttribute("representantes", representanteService.findAll());
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("instituciones", institucionService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);

            return "representante/gestionar-representante";
        } else {
            return "redirect:/";
        }

    }

    // FUNCION PARA GUARDAR EL departamento
    @RequestMapping(value = "/RepresentanteF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String CarreraF(@Validated Representante representante) { // validar los datos capturados (1)

        representante.setEstado_representante("A");
        representanteService.save(representante);
        return "redirect:/adm/RepresentanteR";
    }

    @RequestMapping(value = "/editar-representante/{id_representante}")
    public String editar_r(@PathVariable("id_representante") String id_representante, Model model) {
        try {
            Long id_rep = Long.parseLong(Encryptar.decrypt(id_representante));
            Representante representante = representanteService.findOne(id_rep);
            model.addAttribute("representante", representante);

            List<Representante> representantes = representanteService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Representante representante2 : representantes) {
                String id_encryptado = Encryptar.encrypt(Long.toString(representante2.getId_representante()));
                encryptedIds.add(id_encryptado);
            }
            model.addAttribute("representantes", representantes);
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("instituciones", institucionService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);
            return "representante/gestionar-representante";

        } catch (Exception e) {

            return "redirect:/adm/InicioAdm";
        }
    }

    // FUNCION PARA GUARDAR EL departamento
    @RequestMapping(value = "/RepresentanteModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String departamentoModF(HttpServletRequest request, @Validated Representante representante,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        representante.setId_usu(usuario.getId_usuario());

        representante.setEstado_representante("A");
        representanteService.save(representante);
        return "redirect:/adm/RepresentanteR";
    }

    // FUNCION PARA ELIMINAR EL REGISTRO DE departamento
    @RequestMapping(value = "/eliminar-representante/{id_representate}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_representate") String id_representate)
            throws Exception {
        try {
            Long id_rep = Long.parseLong(Encryptar.decrypt(id_representate));
            Representante representante = representanteService.findOne(id_rep);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            representante.setId_usu(usuario.getId_usuario());
            representante.setEstado_representante("X");
            representanteService.save(representante);
            return "redirect:/adm/RepresentanteR";
        } catch (Exception e) {
            return "redirect:/adm/InicioAdm";
        }
    }

    @GetMapping("/tableRepresentantes")
    public String tableRequisitos(@Validated Representante representante, Model model) throws Exception {

        List<Representante> representantes = representanteService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Representante representante2 : representantes) {
            String id_encryptado = Encryptar.encrypt(Long.toString(representante2.getId_representante()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("representantes", representantes);
        model.addAttribute("id_encryptado", encryptedIds);

        return "representante/tableFragmentRepre :: table";
    }
}
