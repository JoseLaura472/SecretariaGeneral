package com.example.Proyecto.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/adm")
public class IndexController {
    @RequestMapping(value = "InicioAdm", method = RequestMethod.GET)
    public String PlantillaBasia() {

        return "adm/inicio-adm";
    }
}
