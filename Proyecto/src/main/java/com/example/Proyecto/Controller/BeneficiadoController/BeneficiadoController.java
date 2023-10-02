package com.example.Proyecto.Controller.BeneficiadoController;

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

import com.example.Proyecto.Models.Entity.Beneficiado;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IBeneficiadoService;
import com.example.Proyecto.Models.IService.ITipoBeneficiadoService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class BeneficiadoController {

    @Autowired
    private IBeneficiadoService beneficiadoService;

    @Autowired
    private ITipoBeneficiadoService tipoBeneficiadoService;


    @RequestMapping(value = "/BeneficiadoR", method = RequestMethod.GET) // Pagina principal
    public String Representante(@Validated Beneficiado beneficiado, Model model, HttpServletRequest request)
            throws Exception {

        if (request.getSession().getAttribute("persona") != null) {
            List<Beneficiado> beneficiados = beneficiadoService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Beneficiado beneficiado2 : beneficiados) {
                String id_encryptado = Encryptar.encrypt(Long.toString(beneficiado2.getId_beneficiado()));
                encryptedIds.add(id_encryptado);
            }

            model.addAttribute("beneficiados", beneficiadoService.findAll());
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);

            return "beneficiado/gestionar-beneficiado";
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "BeneficiadoF", method = RequestMethod.POST) 
    public String BeneficiadoF(HttpServletRequest request, @Validated Beneficiado beneficiado) { 

        beneficiado.setEstado_beneficiado("A");
        beneficiadoService.save(beneficiado);

        return "redirect:/adm/BeneficiadoR";
    }


    @RequestMapping(value = "/editar-beneficiado/{id_beneficiado}")
    public String editar_c(HttpServletRequest request, @PathVariable("id_beneficiado") String id_beneficiado, Model model) {
        if (request.getSession().getAttribute("persona") != null) {
        try {
            Long id_benefi = Long.parseLong(Encryptar.decrypt(id_beneficiado));
            Beneficiado beneficiado = beneficiadoService.findOne(id_benefi);
            model.addAttribute("beneficiado", beneficiado);

            List<Beneficiado> beneficiados = beneficiadoService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Beneficiado beneficiado2 : beneficiados) {
                String id_encryptado = Encryptar.encrypt(Long.toString(beneficiado2.getId_beneficiado()));
                encryptedIds.add(id_encryptado);
            }
            model.addAttribute("beneficiados", beneficiados);
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);
            return "beneficiado/gestionar-beneficiado";

        } catch (Exception e) {

            return "redirect:/adm/InicioAdm";
        }
        } else {
            return "redirect:/";
        }
    }


    @RequestMapping(value = "/BeneficiadoModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String beneficiado_mod(HttpServletRequest request, @Validated Beneficiado beneficiado,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        beneficiado.setId_usu(usuario.getId_usuario());
        beneficiado.setEstado_beneficiado("A");
        beneficiadoService.save(beneficiado);
        return "redirect:/adm/BeneficiadoR";
    }
    

    @RequestMapping(value = "/eliminar-beneficiado/{id_beneficiado}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_beneficiado") String id_beneficiado)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
        try {
            Long id_benefi = Long.parseLong(Encryptar.decrypt(id_beneficiado));
            Beneficiado beneficiado = beneficiadoService.findOne(id_benefi);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            beneficiado.setId_usu(usuario.getId_usuario());
            beneficiado.setEstado_beneficiado("X");
            beneficiadoService.save(beneficiado);
            return "redirect:/adm/BeneficiadoR";
        } catch (Exception e) {
            return "redirect:/adm/InicioAdm";
        }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tableBeneficiado")
    public String tableConsejos(@Validated Beneficiado beneficiado, Model model) throws Exception {

        List<Beneficiado> beneficiados = beneficiadoService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Beneficiado beneficiado2 : beneficiados) {
            String id_encryptado = Encryptar.encrypt(Long.toString(beneficiado2.getId_beneficiado()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("beneficiados", beneficiados);
        model.addAttribute("id_encryptado", encryptedIds);

        return "institucion/tableFragmentBenefi :: table";
    }
}
