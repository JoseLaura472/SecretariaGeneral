package com.example.Proyecto.Controller.InstitucionControllers;

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
import com.example.Proyecto.Models.Entity.Institucion;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IInstitucionService;
import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class InstitucionController {

    @Autowired
    private IInstitucionService institucionService;

    @Autowired IResolucionService resolucionService;

    @Autowired
    private IConsejoService consejoService;

    @RequestMapping(value = "InstitucionR", method = RequestMethod.GET)
    public String InstitucionR(HttpServletRequest request, @Validated Institucion institucion, Model model)
            throws Exception {

        if (request.getSession().getAttribute("persona") != null) {

            List<Institucion> institucions = institucionService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Institucion institucion2 : institucions) {
                String id_encryptado = Encryptar.encrypt(Long.toString(institucion2.getId_institucion()));
                encryptedIds.add(id_encryptado);
            }
             Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo3 = consejoService.findOne(usuario.getConsejo().getId_consejo());
              List<Resolucion> resoluciones;
            if (usuario.getEstado().equals("AU")) {
                resoluciones = resolucionService.findAll();
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(consejo3.getId_consejo());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("years", years);
            model.addAttribute("institucion", new Institucion());
            model.addAttribute("institucions", institucions);
            model.addAttribute("id_encryptado", encryptedIds);

            return "institucion/gestionar-institucion";
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "InstitucionF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String InstitucionF(HttpServletRequest request, @Validated Institucion institucion) { // validar los datos
                                                                                                 // capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        institucion.setId_usu(usuario.getId_usuario());

        institucion.setEstado_institucion("A");
        institucionService.save(institucion);

        return "redirect:/adm/InstitucionR";
    }

    @RequestMapping(value = "/editar-institucion/{id_institucion}")
    public String editar_c(HttpServletRequest request, @PathVariable("id_institucion") String id_institucion,
            Model model) {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_ins = Long.parseLong(Encryptar.decrypt(id_institucion));
                Institucion institucion = institucionService.findOne(id_ins);
                model.addAttribute("institucion", institucion);

                List<Institucion> institucions = institucionService.findAll();
                List<String> encryptedIds = new ArrayList<>();
                for (Institucion institucion2 : institucions) {
                    String id_encryptado = Encryptar.encrypt(Long.toString(institucion2.getId_institucion()));
                    encryptedIds.add(id_encryptado);
                }
                model.addAttribute("institucions", institucions);
                model.addAttribute("id_encryptado", encryptedIds);
                return "institucion/gestionar-institucion";

            } catch (Exception e) {

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/InstitucionModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String institucion_mod(HttpServletRequest request, @Validated Institucion institucion,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        institucion.setId_usu(usuario.getId_usuario());
        institucion.setEstado_institucion("A");
        institucionService.save(institucion);
        return "redirect:/adm/InstitucionR";
    }

    @RequestMapping(value = "/eliminar-institucion/{id_institucion}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_institucion") String id_institucion)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_inst = Long.parseLong(Encryptar.decrypt(id_institucion));
                Institucion institucion = institucionService.findOne(id_inst);
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                institucion.setId_usu(usuario.getId_usuario());
                institucion.setEstado_institucion("X");
                institucionService.save(institucion);
                return "redirect:/adm/InstitucionR";
            } catch (Exception e) {
                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tableInstitucion")
    public String tableConsejos(@Validated Institucion institucion, Model model) throws Exception {

        List<Institucion> institucions = institucionService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Institucion institucion2 : institucions) {
            String id_encryptado = Encryptar.encrypt(Long.toString(institucion2.getId_institucion()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("institucions", institucions);
        model.addAttribute("id_encryptado", encryptedIds);

        return "institucion/tableFragmentIns :: table";
    }
}
