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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
  
  
             model.addAttribute("convenios", convenioService.findAll());
             
  
  
              return "convenio/listar-convenio";
         
          
      }




    @RequestMapping(value = "ConvenioForm", method = RequestMethod.GET)
    public String ConvenioR(HttpServletRequest request, @Validated Convenio convenio, Model model) throws Exception {

        List<Convenio> convenios = convenioService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Convenio convenio2: convenios) {
            String id_encryptado = Encryptar.encrypt(Long.toString(convenio2.getId_convenio()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("convenio", new Convenio());
        model.addAttribute("convenios", convenios);
        model.addAttribute("consejos", consejoService.findAll());
        model.addAttribute("instituciones", institucionService.findAll());
        model.addAttribute("tipoConvenios", tipoConvenioService.findAll());
        model.addAttribute("representantes", representanteService.findAll());
        model.addAttribute("autoridades", autoridadService.findAll());
        model.addAttribute("id_encryptado", encryptedIds);

        return "convenio/gestionar-convenio";

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

      @RequestMapping(value = "ConvenioF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String ConvenioF(HttpServletRequest request, @Validated Convenio convenio, @RequestParam("archivo") MultipartFile archivo) throws IOException  { // validar los datos capturados (1)

     
            Path rootPath = Paths.get("archivos/");
            Path rootAbsolutPath = rootPath.toAbsolutePath();
            String rutaDirectorio = rootAbsolutPath + "/";

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

            ArchivoAdjunto adjunto = new ArchivoAdjunto();
            String alfanuString = generateRandomAlphaNumericString();
            adjunto.setNombre_archivo(alfanuString + ".pdf");
            String rutaArchivo = rutaDirectorio + alfanuString  + ".pdf";
            Files.write(Paths.get(rutaArchivo), archivo.getBytes());
            adjunto.setRuta(rutaArchivo);
            adjunto.setEstado_archivo_adjunto("A");
            archivoAdjuntoService.save(adjunto);


        convenio.setArchivoAdjunto(adjunto);
        convenio.setEstado_convenio("A");
        convenioService.save(convenio);

        return "redirect:/adm/ConvenioL";
    }



}
