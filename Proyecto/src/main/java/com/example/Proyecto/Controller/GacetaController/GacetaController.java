package com.example.Proyecto.Controller.GacetaController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IResolucionService;

@Controller
public class GacetaController {

    @Autowired
    private IResolucionService resolucionService;

    @Autowired
    private IConsejoService consejoService;
    
    @GetMapping(value = "/gaceta")
    public String vista_gaceta_inicio(){

        return "gaceta/gaceta";
    }
    @GetMapping(value = "/gaceta_res")
    public String vista_gaceta_resoluciones(Model model){

        List<Resolucion> resoluciones;

        resoluciones = resolucionService.findAll();

        Set<Integer> years = resoluciones.stream()
                .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate().getYear())
                .collect(Collectors.toSet());

        model.addAttribute("years", years.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList()));
        model.addAttribute("consejos", consejoService.findAll());

        return "gaceta/gaceta_res";
    }

    @PostMapping("/gacetaResoluciones")
    public String generarReporteAutoridadResolucion(
        @RequestParam(name = "gestion") Integer selectedYear,
        @RequestParam("id_consejo") Long idConsejo, Model model)
        throws FileNotFoundException, IOException {
    
        List<Resolucion> resoluciones = resolucionService.findAll();
        Set<Integer> years = resoluciones.stream()
            .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().getYear())
            .collect(Collectors.toSet());
    
        if (selectedYear != null) {
            // Filtrar resoluciones por el año seleccionado
            resoluciones = resoluciones.stream()
                .filter(resolucion -> resolucion.getFecha_resolucion().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == selectedYear)
                .collect(Collectors.toList());
        }
    
        if (idConsejo != null) {
            // Filtrar resoluciones por el ID del consejo
            resoluciones = resoluciones.stream()
                .filter(resolucion -> resolucion.getConsejo().getId_consejo() == idConsejo)
                .collect(Collectors.toList());
        }
    
        model.addAttribute("resoluciones", resoluciones);
           model.addAttribute("years", years.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList()));
        model.addAttribute("consejos", consejoService.findAll());
        return "gaceta/gaceta_res";
    }
    
}
