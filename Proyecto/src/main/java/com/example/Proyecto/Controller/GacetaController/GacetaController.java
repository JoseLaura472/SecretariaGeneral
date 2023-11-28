package com.example.Proyecto.Controller.GacetaController;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
