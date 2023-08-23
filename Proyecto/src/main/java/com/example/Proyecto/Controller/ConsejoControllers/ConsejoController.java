package com.example.Proyecto.Controller.ConsejoControllers;

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

import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class ConsejoController {

    @Autowired
    private IConsejoService consejoService;

    @RequestMapping(value = "ConsejoR", method = RequestMethod.GET)
    public String ConsejoR(HttpServletRequest request, @Validated Consejo consejo, Model model) throws Exception {

        List<Consejo> consejos = consejoService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Consejo consejo2 : consejos) {
            String id_encryptado = Encryptar.encrypt(Long.toString(consejo2.getId_consejo()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("consejo", new Consejo());
        model.addAttribute("consejos", consejos);
        model.addAttribute("id_encryptado", encryptedIds);

        return "consejo/gestionar-consejo";

    }

    @RequestMapping(value = "ConsejoF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String ConsejoF(HttpServletRequest request, @Validated Consejo consejo) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        consejo.setId_usu(usuario.getId_usuario());

        consejo.setEstado_consejo("A");
        consejoService.save(consejo);

        return "redirect:/adm/ConsejoR";
    }

    @RequestMapping(value = "/editar-consejo/{id_consejo}")
    public String editar_c(@PathVariable("id_consejo") String id_consejo, Model model) {
        try {
            Long id_con = Long.parseLong(Encryptar.decrypt(id_consejo));
            Consejo consejo = consejoService.findOne(id_con);
            model.addAttribute("consejo", consejo);

            List<Consejo> consejos = consejoService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Consejo consejo2 : consejos) {
                String id_encryptado = Encryptar.encrypt(Long.toString(consejo2.getId_consejo()));
                encryptedIds.add(id_encryptado);
            }
            model.addAttribute("consejos", consejos);
            model.addAttribute("id_encryptado", encryptedIds);
            return "consejo/gestionar-consejo";

        } catch (Exception e) {

            return "redirect:/adm/InicioAdm";
        }
    }

    @RequestMapping(value = "/ConsejoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String consejo_mod(HttpServletRequest request, @Validated Consejo consejo,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        consejo.setId_usu(usuario.getId_usuario());
        consejo.setEstado_consejo("A");
        consejoService.save(consejo);
        return "redirect:/adm/ConsejoR";
    }

    @RequestMapping(value = "/eliminar-consejo/{id_consejo}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_consejo") String id_consejo)
            throws Exception {
        try {
            Long id_con = Long.parseLong(Encryptar.decrypt(id_consejo));
            Consejo consejo = consejoService.findOne(id_con);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            consejo.setId_usu(usuario.getId_usuario());
            consejo.setEstado_consejo("X");
            consejoService.save(consejo);
            return "redirect:/adm/ConsejoR";
        } catch (Exception e) {
            return "redirect:/adm/InicioAdm";
        }
    }

    @GetMapping("/tableConsejos")
    public String tableConsejos(@Validated Consejo consejo, Model model) throws Exception {

        List<Consejo> consejos = consejoService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Consejo consejo2 : consejos) {
            String id_encryptado = Encryptar.encrypt(Long.toString(consejo2.getId_consejo()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("consejos", consejos);
        model.addAttribute("id_encryptado", encryptedIds);

        return "consejo/tableFragment :: table";
    }
}
