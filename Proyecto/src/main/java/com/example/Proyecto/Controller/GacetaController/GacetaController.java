package com.example.Proyecto.Controller.GacetaController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GacetaController {
    
    @GetMapping(value = "/gaceta")
    public String vista_gaceta_inicio(){

        return "gaceta/gaceta";
    }
    @GetMapping(value = "/gaceta_res")
    public String vista_gaceta_resoluciones(){

        return "gaceta/gaceta_res";
    }
}
