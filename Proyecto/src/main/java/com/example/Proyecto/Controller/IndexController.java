package com.example.Proyecto.Controller;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

            List<Resolucion> resolucionesPublicoCao = resolucionService.resolucionPorIdConsejo(3L);
            List<Convenio> conveniosPublicoCao = convenioService.convenioPorIdConsejo(3L);
            List<Resolucion> resolucionesPublicoHcu = resolucionService.resolucionPorIdConsejo(2L);
            List<Convenio> conveniosPublicoHcu = convenioService.convenioPorIdConsejo(2L);
            int numResolucionCao = resolucionesPublicoCao.size();
            int numConvenioCao = conveniosPublicoCao.size();
            int numResolucionHcu = resolucionesPublicoHcu.size();
            int numConvenioHcu = conveniosPublicoHcu.size();

            List<Resolucion> resoluciones;
            List<Convenio> convenios;
            if (usuario.getEstado().equals("AU") || usuario.getEstado().equals("S")) {
                resoluciones = resolucionService.findAll();
                convenios = convenioService.findAll();
                model.addAttribute("numResolucionCao", numResolucionCao);
                model.addAttribute("numConvenioCao", numConvenioCao);
                model.addAttribute("numResolucionHcu", numResolucionHcu);
                model.addAttribute("numConvenioHcu", numConvenioHcu);
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(usuario.getConsejo().getId_consejo());
                convenios = convenioService.convenioPorIdConsejo(usuario.getConsejo().getId_consejo());
                model.addAttribute("titulo", usuario.getConsejo().getNombre_consejo() + " ("+usuario.getConsejo().getSigla_consejo()+")");
                model.addAttribute("numResolucion", resoluciones.size());
                model.addAttribute("numConvenio", convenios.size());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("years", years);
            
            // Agrupar por año y contar las resoluciones
        Map<Integer, Long> resolucionesPorAnio = resoluciones.stream()
                .collect(Collectors.groupingBy(
                        resolucion -> resolucion.getFecha_resolucion().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate().getYear(),
                        Collectors.counting()
                ));

        Map<Integer, Long> conveniosPorAnio = convenios.stream()
                .collect(Collectors.groupingBy(
                        convenio -> convenio.getRegistro().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate().getYear(),
                        Collectors.counting()));

        // Convertir el resultado a una lista de pares (año, cantidad) y ordenarla por año
        List<Entry<Integer, Long>> resolucionesPorAnioList = new ArrayList<>(resolucionesPorAnio.entrySet());
        resolucionesPorAnioList.sort(Entry.comparingByKey());

        List<Entry<Integer, Long>> conveniosPorAnioList = new ArrayList<>(conveniosPorAnio.entrySet());
        conveniosPorAnioList.sort(Entry.comparingByKey());

        for (Entry<Integer,Long> entry : conveniosPorAnioList) {
            System.out.println(entry.getKey());
        }

        model.addAttribute("resoluciones", resolucionesPorAnioList);
        model.addAttribute("convenios", conveniosPorAnioList);
        
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
