package com.example.Proyecto.Controller;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IConvenioService;
import com.example.Proyecto.Models.IService.IResolucionService;

@Controller
@RequestMapping("/adm")
public class IndexController {
    
    @Autowired
    private IResolucionService resolucionService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IConvenioService convenioService;


    @RequestMapping(value = "InicioAdm", method = RequestMethod.GET)
    public String PlantillaBasia(Model model,HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
            List<Resolucion> resolucionesPublico = resolucionService.findAll();
            List<Convenio> conveniosPublico = convenioService.findAll();
            int numResolucion = resolucionesPublico.size();
            int numConvenio = conveniosPublico.size();

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
            model.addAttribute("numResolucion", numResolucion);
            model.addAttribute("numConvenio", numConvenio);


            return "adm/inicio-adm";
        }else{
            return "redirect:/";
        }
        
    }

    @RequestMapping(value = "/respaldos", method = RequestMethod.GET)
    public String tablaRespaldo(Model model,HttpServletRequest request) {
     


            return "prueba/prueba";
      
        
    }
}
