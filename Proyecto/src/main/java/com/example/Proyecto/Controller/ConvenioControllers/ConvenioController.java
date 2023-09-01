package com.example.Proyecto.Controller.ConvenioControllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IConvenioService;
import com.example.Proyecto.Models.IService.IInstitucionService;
import com.example.Proyecto.Models.IService.IRepresentanteService;
import com.example.Proyecto.Models.IService.ITipoConvenioService;
import com.example.Proyecto.Models.Otros.AdjuntarArchivo;
import com.example.Proyecto.Models.Otros.Encryptar;

@Controller
@RequestMapping("/adm")
public class ConvenioController {

    @Autowired
    private IConvenioService convenioService;

    @Autowired
    private IRepresentanteService representanteService;

    @Autowired
    private IAutoridadService autoridadService;

    @Autowired
    private ITipoConvenioService tipoConvenioService;

    @Autowired
    private IInstitucionService institucionService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/ConvenioL", method = RequestMethod.GET) // Pagina principal
    public String ConvenioL(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
   



          model.addAttribute("convenios", convenioService.convenioPorIdConsejo(usuario.getConsejo().getId_consejo()));

        return "convenio/listar-convenio";
        }else {
            return "redirect:/";
        }
      

    }

    @RequestMapping(value = "ConvenioForm", method = RequestMethod.GET)
    public String ConvenioR(HttpServletRequest request, @Validated Convenio convenio, Model model) throws Exception {
         if (request.getSession().getAttribute("usuario") != null) {
           List<Convenio> convenios = convenioService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Convenio convenio2 : convenios) {
            String id_encryptado = Encryptar.encrypt(Long.toString(convenio2.getId_convenio()));
            encryptedIds.add(id_encryptado);
        }

         Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());


        model.addAttribute("convenio", new Convenio());
        model.addAttribute("convenios", convenios);
        model.addAttribute("consejos", consejoService.findAll());
        model.addAttribute("instituciones", institucionService.findAll());
        model.addAttribute("tipoConvenios", tipoConvenioService.findAll());
        model.addAttribute("representantes", representanteService.findAll());
        model.addAttribute("autoridades", autoridadService.autoridadPorIdConsejo(consejo.getId_consejo()));
        model.addAttribute("id_encryptado", encryptedIds);

        return "convenio/gestionar-convenio";
        }else{
         return "redirect:/";
        }
      

    }

    // Generador de Caracteres aleatorios
    public String generateRandomAlphaNumericString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    @RequestMapping(value = "/ConvenioF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String ConvenioF(@Validated Convenio convenio, RedirectAttributes redirectAttrs, Model model,HttpServletRequest request) throws FileNotFoundException, IOException {// validar los
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());    

        MultipartFile multipartFile = convenio.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();  
                                                                                                     // (1)
        Path rootPath = Paths.get("archivos/convenios/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath+"";
          try {
                if (!Files.exists(rootPath)) {
                    Files.createDirectories(rootPath);
                    System.out.println("Directorio creado: " + rutaDirectorio);
                } else {
                    System.out.println("El directorio ya existe: " + rutaDirectorio);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el directorio: " + e.getMessage());
            }
        String alfaString = generateRandomAlphaNumericString();
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);
        model.addAttribute("di", rutaArchivo);
        List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
        convenio.setNombreArchivo((listArchivos.size() + 1)+"-" +alfaString  +".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoConvenio(convenio, rutaArchivo);
        archivoAdjunto.setNombre_archivo((listArchivos.size() + 1)+"-" +alfaString  +".pdf");
       
        archivoAdjunto.setRuta(rutaArchivo);
        archivoAdjunto.setEstado_archivo_adjunto("A");
        ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
    
        convenio.setConsejo(consejo);
        convenio.setArchivoAdjunto(archivoAdjunto2);
        convenio.setEstado_convenio("A");
        convenioService.save(convenio);
        return "redirect:/adm/ConvenioL";
        }

    

    @RequestMapping(value = "/editar-convenio/{id_convenio}")
    public String editar_p(@PathVariable("id_convenio") Long id_convenio, Model model,HttpServletRequest request)
            throws NumberFormatException, Exception {
          if (request.getSession().getAttribute("usuario") != null) {
           Convenio convenio = convenioService.findOne(id_convenio);
        model.addAttribute("convenio", convenio);

        List<Convenio> convenios = convenioService.findAll();

        model.addAttribute("convenios", convenios);
        model.addAttribute("consejos", consejoService.findAll());
        model.addAttribute("instituciones", institucionService.findAll());
        model.addAttribute("tipoConvenios", tipoConvenioService.findAll());
        model.addAttribute("representantes", representanteService.findAll());
        model.addAttribute("autoridades", autoridadService.findAll());

        return "convenio/gestionar-convenio";
        
        }else{
         return "redirect:/";
        }
      
       

    }

    @PostMapping(value = "/ConvenioModF")
    public String ConvenioModF(@Validated Convenio convenio, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request)
            throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());    
        MultipartFile multipartFile = convenio.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
            String alfaString = generateRandomAlphaNumericString();
      
        Path rootPath = Paths.get("archivos/convenios/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath+"";
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);
      
        convenio.setNombreArchivo((alfaString  +".pdf"));
        Integer ad = adjuntarArchivo.adjuntarArchivoConvenio(convenio, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorConvenio(convenio.getId_convenio());
            barchivoAdjunto.setNombre_archivo(convenio.getNombreArchivo());
            barchivoAdjunto.setRuta(rutaArchivo);
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        convenio.setConsejo(consejo);
        convenio.setEstado_convenio("A");
        convenioService.save(convenio);
        return "redirect:/adm/ConvenioL";

        
}

    @RequestMapping(value = "/openFileConvenio/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_convenio) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorConvenio(id_convenio);
        File file = new File(ArchivoAdjuntos.getRuta() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }


    @RequestMapping(value = "/eliminar-convenio/{id_convenio}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_convenio") Long id_convenio)
            throws Exception {
              if (request.getSession().getAttribute("usuario") != null) {
                 Convenio convenio = convenioService.findOne(id_convenio);
           
            convenio.setEstado_convenio("X");
            convenioService.save(convenio);

            return "redirect:/adm/ConvenioL";
              }else{
            return "redirect:/";
              }
           
        }
    }


    


