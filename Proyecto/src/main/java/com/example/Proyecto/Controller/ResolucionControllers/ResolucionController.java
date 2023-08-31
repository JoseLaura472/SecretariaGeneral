package com.example.Proyecto.Controller.ResolucionControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IConsejoService;

import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.Otros.AdjuntarArchivo;

@Controller
@RequestMapping("/adm")
public class ResolucionController {
    @Autowired
    private IResolucionService resolucionService;

    @Autowired
    private IAutoridadService autoridadService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/ResolucionL", method = RequestMethod.GET) // Pagina principal
    public String ResolucionL(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            model.addAttribute("resoluciones", resolucionService.findAll());

            return "resolucion/listar-resolucion";

        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "ResolucionForm", method = RequestMethod.GET)
    public String ResolucionR(HttpServletRequest request, @Validated Resolucion resolucion, Model model)
            throws Exception {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
            List<Resolucion> resoluciones = resolucionService.findAll();

            model.addAttribute("resolucion", new Resolucion());
            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("autoridades", autoridadService.autoridadPorIdConsejo(consejo.getId_consejo()));

            return "resolucion/gestionar-resolucion";

        } else {
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

    @RequestMapping(value = "/ResolucionF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String ResolucionF(@Validated Resolucion resolucion,HttpServletRequest request, RedirectAttributes redirectAttrs, Model model)
            throws FileNotFoundException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());

        MultipartFile multipartFile = resolucion.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        // (1)
        Path rootPath = Paths.get("archivos/resoluciones/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath + "";
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
        resolucion.setNombreArchivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoResolucion(resolucion, rutaArchivo);
        archivoAdjunto.setNombre_archivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");

        archivoAdjunto.setRuta(rutaArchivo);
        archivoAdjunto.setEstado_archivo_adjunto("A");
        ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
        resolucion.setConsejo(consejo);
        resolucion.setArchivoAdjunto(archivoAdjunto2);
        resolucion.setEstado_resolucion("A");
        resolucionService.save(resolucion);
        return "redirect:/adm/ResolucionL";
    }

    @RequestMapping(value = "/editar-resolucion/{id_resolucion}")
    public String editar_p(@PathVariable("id_resolucion") Long id_resolucion, HttpServletRequest request, Model model)
            throws NumberFormatException, Exception {
        if (request.getSession().getAttribute("usuario") != null) {
            Resolucion resolucion = resolucionService.findOne(id_resolucion);
            model.addAttribute("resolucion", resolucion);

            List<Resolucion> resoluciones = resolucionService.findAll();

            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("autoridades", autoridadService.findAll());

            return "resolucion/gestionar-resolucion";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping(value = "/ResolucionModF")
    public String ResolucionModF(@Validated Resolucion resolucion, RedirectAttributes redirectAttrs, Model model,
            HttpServletRequest request)
            throws IOException {

        MultipartFile multipartFile = resolucion.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        String alfaString = generateRandomAlphaNumericString();

        Path rootPath = Paths.get("archivos/resoluciones/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath + "";
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);

        resolucion.setNombreArchivo((alfaString + ".pdf"));
        Integer ad = adjuntarArchivo.adjuntarArchivoResolucion(resolucion, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorResolucion(resolucion.getId_resolucion());
            barchivoAdjunto.setNombre_archivo(resolucion.getNombreArchivo());
            barchivoAdjunto.setRuta(rutaArchivo);
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }

        resolucion.setEstado_resolucion("A");
        resolucionService.save(resolucion);
        return "redirect:/adm/ResolucionL";

    }

    @RequestMapping(value = "/openFileResolucion/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_resolucion) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorResolucion(id_resolucion);
        File file = new File(ArchivoAdjuntos.getRuta() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/eliminar-resolucion/{id_resolucion}")
    public String eliminar_resolucion(HttpServletRequest request, @PathVariable("id_resolucion") Long id_resolucion)
            throws Exception {
        if (request.getSession().getAttribute("usuario") != null) {
            Resolucion resolucion = resolucionService.findOne(id_resolucion);

            resolucion.setEstado_resolucion("X");
            resolucionService.save(resolucion);

            return "redirect:/adm/ResolucionL";
        }else{
           return "redirect:/";  
        }

    }

}
