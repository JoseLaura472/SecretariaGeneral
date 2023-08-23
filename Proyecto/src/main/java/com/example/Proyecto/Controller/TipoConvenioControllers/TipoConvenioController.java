package com.example.Proyecto.Controller.TipoConvenioControllers;

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

import com.example.Proyecto.Models.Entity.TipoConvenio;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.ITipoConvenioService;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class TipoConvenioController {

    @Autowired
    private ITipoConvenioService tipoConvenioService;

    @RequestMapping(value = "TipoConvenioR", method = RequestMethod.GET)
    public String TipoConvenioR(HttpServletRequest request, @Validated TipoConvenio tipoConvenio, Model model)
            throws Exception {

        List<TipoConvenio> tipoConvenios = tipoConvenioService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (TipoConvenio tipoConvenio2 : tipoConvenios) {
            String id_encryptado = Encryptar.encrypt(Long.toString(tipoConvenio2.getId_tipo_convenio()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("tipoConvenio", new TipoConvenio());
        model.addAttribute("tipoConvenios", tipoConvenios);
        model.addAttribute("id_encryptado", encryptedIds);

        return "tpConvenio/gestionar-tpconvenio";

    }

    @RequestMapping(value = "TipoConvenioF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TipoConvenioF(HttpServletRequest request, @Validated TipoConvenio tipoConvenio) { // validar los datos
                                                                                                    // capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoConvenio.setId_usu(usuario.getId_usuario());

        tipoConvenio.setEstado_tipo_convenio("A");

        tipoConvenioService.save(tipoConvenio);

        return "redirect:/adm/TipoConvenioR";
    }

    @RequestMapping(value = "/editar-tpconvenio/{id_tipo_convenio}")
    public String editar_c(@PathVariable("id_tipo_convenio") String id_tipo_convenio, Model model) {
        try {
            Long id_tpcon = Long.parseLong(Encryptar.decrypt(id_tipo_convenio));
            TipoConvenio tipoConvenio = tipoConvenioService.findOne(id_tpcon);
            model.addAttribute("tipoConvenio", tipoConvenio);

            List<TipoConvenio> tipoConvenios = tipoConvenioService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (TipoConvenio tipoConvenio2 : tipoConvenios) {
                String id_encryptado = Encryptar.encrypt(Long.toString(tipoConvenio2.getId_tipo_convenio()));
                encryptedIds.add(id_encryptado);
            }
            model.addAttribute("tipoConvenios", tipoConvenios);
            model.addAttribute("id_encryptado", encryptedIds);
            return "tpconvenio/gestionar-tpconvenio";

        } catch (Exception e) {

            return "redirect:/adm/InicioAdm";
        }
    }

    @RequestMapping(value = "/TipoConvenioModF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String tpconvenio_mod(HttpServletRequest request, @Validated TipoConvenio tipoConvenio,
            RedirectAttributes redirectAttrs) { // validar los datos capturados (1)

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        tipoConvenio.setId_usu(usuario.getId_usuario());
        tipoConvenio.setEstado_tipo_convenio("A");
        tipoConvenioService.save(tipoConvenio);
        return "redirect:/adm/TipoConvenioR";
    }

    @RequestMapping(value = "/eliminar-tpconvenio/{id_tipo_convenio}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_tipo_convenio") String id_tipo_convenio)
            throws Exception {
        try {
            Long id_con = Long.parseLong(Encryptar.decrypt(id_tipo_convenio));
            TipoConvenio tipoConvenio = tipoConvenioService.findOne(id_con);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            tipoConvenio.setId_usu(usuario.getId_usuario());
            tipoConvenio.setEstado_tipo_convenio("X");
            tipoConvenioService.save(tipoConvenio);
            return "redirect:/adm/TipoConvenioR";
        } catch (Exception e) {
            return "redirect:/adm/InicioAdm";
        }
    }

    @GetMapping("/tableTpConvenios")
    public String tableConsejos(@Validated TipoConvenio tipoConvenio, Model model) throws Exception {

        List<TipoConvenio> tipoConvenios = tipoConvenioService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (TipoConvenio tipoConvenio2 : tipoConvenios) {
            String id_encryptado = Encryptar.encrypt(Long.toString(tipoConvenio2.getId_tipo_convenio()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("tipoConvenios", tipoConvenios);
        model.addAttribute("id_encryptado", encryptedIds);

        return "tpconvenio/tableFragmentTp :: table";
    }
}
