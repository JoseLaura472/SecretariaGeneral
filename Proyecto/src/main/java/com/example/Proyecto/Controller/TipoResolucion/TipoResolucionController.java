package com.example.Proyecto.Controller.TipoResolucion;

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
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.TipoResolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.IService.ITipoResolucionService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class TipoResolucionController {
    @Autowired
    private ITipoResolucionService tipoResolucionService;

    @Autowired
    private IResolucionService resolucionService;

    @Autowired
    private IConsejoService consejoService;

    @RequestMapping(value = "TipoResolucionR", method = RequestMethod.GET)
    public String TipoConvenioR(HttpServletRequest request, @Validated TipoResolucion tipoResolucion, Model model)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            List<TipoResolucion> tipoResolucions = tipoResolucionService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (TipoResolucion tipoResolucion2 : tipoResolucions) {
                String id_encryptado = Encryptar.encrypt(Long.toString(tipoResolucion2.getId_tipo_resolucion()));
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
            model.addAttribute("tipoResolucion", new TipoResolucion());
            model.addAttribute("tipoResoluciones", tipoResolucions);
            model.addAttribute("id_encryptado", encryptedIds);

            return "tpresolucion/gestionar-tpresolucion";
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "TipoResolucionF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TipoConvenioF(HttpServletRequest request, @Validated TipoResolucion tipoResolucion) { // validar los datos
                                                                                                    // capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoResolucion.setId_usu(usuario.getId_usuario());

        tipoResolucion.setEstado_tipo_resolucion("A");

        tipoResolucionService.save(tipoResolucion);

        return "redirect:/adm/TipoResolucionR";
    }

    @RequestMapping(value = "/editar-tpresolucion/{id_tipo_resolucion}")
    public String editar_c(HttpServletRequest request, @PathVariable("id_tipo_resolucion") String id_tipo_resolucion,
            Model model) {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_tpresol = Long.parseLong(Encryptar.decrypt(id_tipo_resolucion));
                TipoResolucion tipoResolucion = tipoResolucionService.findOne(id_tpresol);
                model.addAttribute("tipoResolucion", tipoResolucion);

                List<TipoResolucion> tipoResolucions = tipoResolucionService.findAll();
                List<String> encryptedIds = new ArrayList<>();
                for (TipoResolucion tipoResolucion2 : tipoResolucions) {
                    String id_encryptado = Encryptar.encrypt(Long.toString(tipoResolucion2.getId_tipo_resolucion()));
                    encryptedIds.add(id_encryptado);
                }
                model.addAttribute("tipoResoluciones", tipoResolucions);
                model.addAttribute("id_encryptado", encryptedIds);
                return "tpresolucion/gestionar-tpresolucion";

            } catch (Exception e) {

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/TipoResolucionModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String tpconvenio_mod(HttpServletRequest request, @Validated TipoResolucion tipoResolucion,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoResolucion.setId_usu(usuario.getId_usuario());
        tipoResolucion.setEstado_tipo_resolucion("A");
        tipoResolucionService.save(tipoResolucion);
        return "redirect:/adm/TipoResolucionR";
    }


    @RequestMapping(value = "/eliminar-tpresolucion/{id_tipo_resolucion}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_tipo_resolucion") String id_tipo_resolucion)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_tpresol = Long.parseLong(Encryptar.decrypt(id_tipo_resolucion));
                TipoResolucion tipoResolucion = tipoResolucionService.findOne(id_tpresol);
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                tipoResolucion.setId_usu(usuario.getId_usuario());
                tipoResolucion.setEstado_tipo_resolucion("X");
                tipoResolucionService.save(tipoResolucion);
                return "redirect:/adm/TipoResolucionR";
            } catch (Exception e) {
                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tableTpResolucion")
    public String tableConsejos(@Validated TipoResolucion tipoResolucion, Model model) throws Exception {

        List<TipoResolucion> tipoResolucions = tipoResolucionService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (TipoResolucion tipoResolucion2 : tipoResolucions) {
            String id_encryptado = Encryptar.encrypt(Long.toString(tipoResolucion2.getId_tipo_resolucion()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("tipoResoluciones", tipoResolucions);
        model.addAttribute("id_encryptado", encryptedIds);

        return "tpresolucion/tableFragmentTpresol :: table";
    }
}
