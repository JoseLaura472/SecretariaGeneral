package com.example.Proyecto.Controller.TipoBeneficiadoControllers;

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

import com.example.Proyecto.Models.Entity.TipoBeneficiado;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.ITipoBeneficiadoService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class TipoBeneficiadoController {
    @Autowired
    private ITipoBeneficiadoService tipoBeneficiadoService;

    @RequestMapping(value = "TipoBeneficiarioR", method = RequestMethod.GET)
    public String TipoConvenioR(HttpServletRequest request, @Validated TipoBeneficiado tipoBeneficiado, Model model)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            List<TipoBeneficiado> tipoBeneficiados = tipoBeneficiadoService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (TipoBeneficiado tipoBeneficiado2 : tipoBeneficiados) {
                String id_encryptado = Encryptar.encrypt(Long.toString(tipoBeneficiado2.getId_tipo_beneficiado()));
                encryptedIds.add(id_encryptado);
            }
            model.addAttribute("tipoBeneficiado", new TipoBeneficiado());
            model.addAttribute("tipoBeneficiados", tipoBeneficiados);
            model.addAttribute("id_encryptado", encryptedIds);

            return "tpbeneficiado/gestionar-tp-beneficiado";
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "TipoBeneficiadoF", method = RequestMethod.POST)
    public String TipoConvenioF(HttpServletRequest request, @Validated TipoBeneficiado tipoBeneficiado) { 

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoBeneficiado.setId_usu(usuario.getId_usuario());

        tipoBeneficiado.setEstado_tipo_beneficiado("A");

        tipoBeneficiadoService.save(tipoBeneficiado);

        return "redirect:/adm/TipoBeneficiarioR";
    }

    @RequestMapping(value = "/editar-tpbeneficiario/{id_tipo_beneficiario}")
    public String editar_c(HttpServletRequest request, @PathVariable("id_tipo_beneficiario") String id_tipo_beneficiario,
            Model model) {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_tpbene = Long.parseLong(Encryptar.decrypt(id_tipo_beneficiario));
                TipoBeneficiado tipoBeneficiado = tipoBeneficiadoService.findOne(id_tpbene);
                model.addAttribute("tipoBeneficiado", tipoBeneficiado);

                List<TipoBeneficiado> tipoBeneficiados = tipoBeneficiadoService.findAll();
                List<String> encryptedIds = new ArrayList<>();
                for (TipoBeneficiado tipoBeneficiado2 : tipoBeneficiados) {
                    String id_encryptado = Encryptar.encrypt(Long.toString(tipoBeneficiado2.getId_tipo_beneficiado()));
                    encryptedIds.add(id_encryptado);
                }
                model.addAttribute("tipoBeneficiados", tipoBeneficiados);
                model.addAttribute("id_encryptado", encryptedIds);
                return "tpbeneficiado/gestionar-tp-beneficiado";

            } catch (Exception e) {

                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/TipoBeneficiarioModF", method = RequestMethod.POST) 
    public String tpconvenio_mod(HttpServletRequest request, @Validated TipoBeneficiado tipoBeneficiado,
            RedirectAttributes redirectAttrs) { 

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoBeneficiado.setId_usu(usuario.getId_usuario());
        tipoBeneficiado.setEstado_tipo_beneficiado("A");
        tipoBeneficiadoService.save(tipoBeneficiado);
        return "redirect:/adm/TipoBeneficiarioR";
    }

    @RequestMapping(value = "/eliminar-tpbeneficiario/{id_tipo_beneficiario}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_tipo_beneficiario") String id_tipo_beneficiario)
            throws Exception {
        if (request.getSession().getAttribute("persona") != null) {
            try {
                Long id_bene = Long.parseLong(Encryptar.decrypt(id_tipo_beneficiario));
                TipoBeneficiado tipoBeneficiado = tipoBeneficiadoService.findOne(id_bene);
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                tipoBeneficiado.setId_usu(usuario.getId_usuario());
                tipoBeneficiado.setEstado_tipo_beneficiado("X");
                tipoBeneficiadoService.save(tipoBeneficiado);
                return "redirect:/adm/TipoBeneficiarioR";
            } catch (Exception e) {
                return "redirect:/adm/InicioAdm";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/tableTpBeneficiados")
    public String tableTpBeneficiados(@Validated TipoBeneficiado tipoBeneficiado, Model model) throws Exception {

        List<TipoBeneficiado> tipoBeneficiados = tipoBeneficiadoService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (TipoBeneficiado tipoBeneficiado2 : tipoBeneficiados) {
            String id_encryptado = Encryptar.encrypt(Long.toString(tipoBeneficiado2.getId_tipo_beneficiado()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("tipoBeneficiados", tipoBeneficiados);
        model.addAttribute("id_encryptado", encryptedIds);

        return "tpconvenio/tableFragmentTp :: table";
    }

}
