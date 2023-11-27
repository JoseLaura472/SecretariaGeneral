package com.example.Proyecto.Controller.Autoridad;

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

import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IPersonaService;
import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class AutoridadController {

    @Autowired
    private IAutoridadService autoridadService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IResolucionService resolucionService;

    @RequestMapping(value = "/AutoridadR", method = RequestMethod.GET) // Pagina principal
    public String Autoridad(@Validated Autoridad autoridad, Model model, HttpServletRequest request) throws Exception {

        if (request.getSession().getAttribute("persona") != null) {
            List<Autoridad> autoridades = autoridadService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Autoridad autoridad2 : autoridades) {
                String id_encryptado = Encryptar.encrypt(Long.toString(autoridad2.getId_autoridad()));
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

            model.addAttribute("autoridades", autoridadService.findAll());
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);

            return "autoridad/gestionar-autoridad";
        } else {
            return "redirect:/";
        }

    }

    // FUNCION PARA GUARDAR EL departamento
    @RequestMapping(value = "/AutoridadF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String AutoridadF(@Validated Autoridad autoridad) { // validar los datos capturados (1)

        autoridad.setEstado_autoridad("A");
        autoridadService.save(autoridad);
        return "redirect:/adm/AutoridadR";
    }

    @RequestMapping(value = "/editar-autoridad/{id_autoridad}")
    public String editar_r(@PathVariable("id_autoridad") String id_autoridad, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_auto = Long.parseLong(Encryptar.decrypt(id_autoridad));
                Autoridad autoridad = autoridadService.findOne(id_auto);
                model.addAttribute("autoridad", autoridad);

                List<Autoridad> autoridades = autoridadService.findAll();
                List<String> encryptedIds = new ArrayList<>();
                for (Autoridad autoridad2 : autoridades) {
                    String id_encryptado = Encryptar.encrypt(Long.toString(autoridad2.getId_autoridad()));
                    encryptedIds.add(id_encryptado);
                }
                model.addAttribute("autoridades", autoridades);
                model.addAttribute("personas", personaService.findAll());
                model.addAttribute("consejos", consejoService.findAll());
                model.addAttribute("id_encryptado", encryptedIds);
                return "autoridad/gestionar-autoridad";

            } catch (Exception e) {

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    // FUNCION PARA GUARDAR EL departamento
    @RequestMapping(value = "/AutoridadModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String departamentoModF(@Validated Autoridad autoridad, RedirectAttributes redirectAttrs) { // validar los
                                                                                                       // datos
                                                                                                       // capturados (1)

        autoridad.setEstado_autoridad("A");
        autoridadService.save(autoridad);
        return "redirect:/adm/AutoridadR";
    }

    // FUNCION PARA ELIMINAR EL REGISTRO DE departamento
    @RequestMapping(value = "/eliminar-autoridad/{id_autoridad}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_autoridad") String id_autoridad)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_auto = Long.parseLong(Encryptar.decrypt(id_autoridad));
                Autoridad autoridad = autoridadService.findOne(id_auto);
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                autoridad.setId_usu(usuario.getId_usuario());
                autoridad.setEstado_autoridad("X");
                autoridadService.save(autoridad);
                return "redirect:/adm/AutoridadR";
            } catch (Exception e) {
                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tableAutoridades")
    public String tableRequisitos(@Validated Autoridad autoridad, Model model) throws Exception {

        List<Autoridad> autoridades = autoridadService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Autoridad autoridad2 : autoridades) {
            String id_encryptado = Encryptar.encrypt(Long.toString(autoridad2.getId_autoridad()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("autoridades", autoridades);
        model.addAttribute("id_encryptado", encryptedIds);

        return "autoridad/tableFragmentAuto :: table";
    }
}
