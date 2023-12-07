package com.example.Proyecto.Controller.ConvenioControllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
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
import com.example.Proyecto.Models.Entity.Representante;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IBeneficiadoService;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IConvenioService;
import com.example.Proyecto.Models.IService.IInstitucionService;
import com.example.Proyecto.Models.IService.IRepresentanteService;
import com.example.Proyecto.Models.IService.ITipoBeneficiadoService;
import com.example.Proyecto.Models.IService.ITipoConvenioService;
import com.example.Proyecto.Models.Otros.AdjuntarArchivo;
import com.example.Proyecto.Models.Otros.Encryptar;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

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

    @Autowired
    private IBeneficiadoService beneficiadoService;

    @Autowired
    private ITipoBeneficiadoService tipoBeneficiadoService;

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/ConvenioL", method = RequestMethod.GET) // Pagina principal
    public String ConvenioL(@Validated Convenio convenio, HttpServletRequest request, Model model) throws Exception {
        if (request.getSession().getAttribute("usuario") != null) {

            List<Convenio> convenios = convenioService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Convenio convenio2 : convenios) {
                String id_encryptado = Encryptar.encrypt(Long.toString(convenio2.getId_convenio()));
                encryptedIds.add(id_encryptado);
            }

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            if (usuario.getEstado().equals("AU")) {
                model.addAttribute("convenios", convenioService.findAll());
            } else {
                model.addAttribute("convenios",
                        convenioService.convenioPorIdConsejo(usuario.getConsejo().getId_consejo()));
            }

            model.addAttribute("instituciones", institucionService.findAll());
            model.addAttribute("representantes", representanteService.findAll());
            model.addAttribute("id_encryptado", encryptedIds);

            return "convenio/listar-convenio";
        } else {
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/Represen", method = RequestMethod.GET)
    public @ResponseBody List<Representante> findAllAgencie(
            @RequestParam(value = "repreId", required = true) Long citId) {
        List<Representante> representantes = representanteService.ReprePorIdInstitu(citId);
        return representantes;
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
            model.addAttribute("beneficiados", beneficiadoService.findAll());
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("representantes", representanteService.findAll());
            model.addAttribute("autoridades", autoridadService.autoridadPorIdConsejo(consejo.getId_consejo()));
            model.addAttribute("id_encryptado", encryptedIds);

            return "convenio/gestionar-convenio";
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

    @RequestMapping(value = "/ConvenioF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String ConvenioF(@Validated Convenio convenio, RedirectAttributes redirectAttrs, Model model,
            HttpServletRequest request, @RequestParam("id_representante") Long id_representante)
            throws FileNotFoundException, IOException {// validar los

        if (request.getSession().getAttribute("usuario") != null) {

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
            Representante representante = representanteService.findOne(id_representante);
            MultipartFile multipartFile = convenio.getFile();
            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
            AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
            // (1)
            Path rootPath = Paths.get("archivos/convenios/");
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

            Path rootPathM = Paths.get("archivos/marca_agua");
            Path rootAbsolutPathM = rootPathM.toAbsolutePath();
            String rutaDirectorioM = rootAbsolutPathM + "/";

            String alfaString = generateRandomAlphaNumericString();
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);
            String rutaArchivoM = adjuntarArchivo.crearSacDirectorio(rutaDirectorioM);
            model.addAttribute("di", rutaArchivo);
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            convenio.setNombreArchivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");
            Integer ad = adjuntarArchivo.adjuntarArchivoConvenio(convenio, rutaArchivo);
            archivoAdjunto.setNombre_archivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");

            archivoAdjunto.setRuta(rutaArchivo);
            archivoAdjunto.setEstado_archivo_adjunto("A");
            ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
            // Ruta completa del archivo PDF original que recibes
            String pdfFilePath = rutaArchivo + File.separator + convenio.getNombreArchivo();

            // Ruta donde guardarás el PDF con marca de agua
            String pdfOutputPath = rutaArchivo + File.separator + "con_marca_" + convenio.getNombreArchivo();

            // Ruta del PDF de la marca de agua
            String watermarkImagePath = rutaDirectorioM + "marca_agua.png";

            try {
                // Crear un nuevo documento PDF de salida
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();

                // Inicializar el escritor de PDF para el nuevo documento
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfOutputPath));
                document.open();

                // Cargar el PDF original
                PdfReader reader = new PdfReader(pdfFilePath);

                // Obtener el número total de páginas en el PDF original
                int pageCount = reader.getNumberOfPages();

                // Cargar la imagen de la marca de agua
                com.itextpdf.text.Image watermarkImage = com.itextpdf.text.Image.getInstance(watermarkImagePath);

                // Definir la posición y la escala de la marca de agua
                float xPosition = 80; // Cambia esto según tus necesidades
                float yPosition = 100; // Cambia esto según tus necesidades
                float scaleFactor = 0.4f; // Cambia esto para ajustar la escala

                // Iterar a través de las páginas del PDF original
                for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
                    // Agregar una nueva página al documento de salida
                    document.newPage();

                    // Obtener el contenido de la página actual
                    PdfContentByte contentByte = writer.getDirectContent();

                    // Obtener la página actual del PDF original
                    PdfImportedPage page = writer.getImportedPage(reader, pageNumber);

                    // Agregar la página del PDF original al nuevo documento
                    contentByte.addTemplate(page, 0, 0);

                    // Agregar la marca de agua (imagen) a la página actual
                    watermarkImage.setAbsolutePosition(xPosition, yPosition);
                    watermarkImage.scaleAbsolute(watermarkImage.getWidth() * scaleFactor,
                            watermarkImage.getHeight() * scaleFactor);
                    contentByte.addImage(watermarkImage);
                }

                // Cerrar el documento
                document.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            convenio.setRepresentante(representante);
            convenio.setRuta_marca_convenio(pdfOutputPath);
            convenio.setConsejo(consejo);
            convenio.setArchivoAdjunto(archivoAdjunto2);
            convenio.setEstado_convenio("A");
            convenioService.save(convenio);
            return "redirect:/adm/ConvenioL";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/editar-convenio/{id_convenio}")
    public String editar_p(@PathVariable("id_convenio") String id_convenio, Model model, HttpServletRequest request)
            throws NumberFormatException, Exception {

        try {
            Long id_conve = Long.parseLong(Encryptar.decrypt(id_convenio));
            Convenio convenio = convenioService.findOne(id_conve);
            model.addAttribute("convenio", convenio);

            List<Convenio> convenios = convenioService.findAll();
            List<String> encryptedIds = new ArrayList<>();
            for (Convenio convenio2 : convenios) {
                String id_encryptado = Encryptar.encrypt(Long.toString(convenio2.getId_convenio()));
                encryptedIds.add(id_encryptado);
            }

            model.addAttribute("convenios", convenios);
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("instituciones", institucionService.findAll());
            model.addAttribute("tipoConvenios", tipoConvenioService.findAll());
            model.addAttribute("beneficiados", beneficiadoService.findAll());
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("representantes", representanteService.findAll());
            model.addAttribute("autoridades", autoridadService.findAll());
            model.addAttribute("edit", "true");

            return "convenio/gestionar-convenio";
        } catch (Exception e) {

            return "redirect:/adm/InicioAdm";
        }

    }

    @PostMapping(value = "/ConvenioModF")
    public String ConvenioModF(@Validated Convenio convenio, RedirectAttributes redirectAttrs, Model model,
            HttpServletRequest request)
            throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
        // Representante representante = representanteService.findOne(id_representante);
        MultipartFile multipartFile = convenio.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        String alfaString = generateRandomAlphaNumericString();

        Path rootPath = Paths.get("archivos/convenios/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath + "";
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);

        Path rootPathM = Paths.get("archivos/marca_agua");
        Path rootAbsolutPathM = rootPathM.toAbsolutePath();
        String rutaDirectorioM = rootAbsolutPathM + "/";

        convenio.setNombreArchivo((alfaString + ".pdf"));
        Integer ad = adjuntarArchivo.adjuntarArchivoConvenio(convenio, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorConvenio(convenio.getId_convenio());
            barchivoAdjunto.setNombre_archivo(convenio.getNombreArchivo());
            barchivoAdjunto.setRuta(rutaArchivo);
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
            // Ruta completa del archivo PDF original que recibes
            String pdfFilePath = rutaArchivo + File.separator + convenio.getNombreArchivo();

            // Ruta donde guardarás el PDF con marca de agua
            String pdfOutputPath = rutaArchivo + File.separator + "con_marca_" + convenio.getNombreArchivo();

            // Ruta del PDF de la marca de agua
            String watermarkImagePath = rutaDirectorioM + "marca_agua.png";

            try {
                // Crear un nuevo documento PDF de salida
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();

                // Inicializar el escritor de PDF para el nuevo documento
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfOutputPath));
                document.open();

                // Cargar el PDF original
                PdfReader reader = new PdfReader(pdfFilePath);

                // Obtener el número total de páginas en el PDF original
                int pageCount = reader.getNumberOfPages();

                // Cargar la imagen de la marca de agua
                com.itextpdf.text.Image watermarkImage = com.itextpdf.text.Image.getInstance(watermarkImagePath);

                // Definir la posición y la escala de la marca de agua
                float xPosition = 80; // Cambia esto según tus necesidades
                float yPosition = 100; // Cambia esto según tus necesidades
                float scaleFactor = 0.4f; // Cambia esto para ajustar la escala

                // Iterar a través de las páginas del PDF original
                for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
                    // Agregar una nueva página al documento de salida
                    document.newPage();

                    // Obtener el contenido de la página actual
                    PdfContentByte contentByte = writer.getDirectContent();

                    // Obtener la página actual del PDF original
                    PdfImportedPage page = writer.getImportedPage(reader, pageNumber);

                    // Agregar la página del PDF original al nuevo documento
                    contentByte.addTemplate(page, 0, 0);

                    // Agregar la marca de agua (imagen) a la página actual
                    watermarkImage.setAbsolutePosition(xPosition, yPosition);
                    watermarkImage.scaleAbsolute(watermarkImage.getWidth() * scaleFactor,
                            watermarkImage.getHeight() * scaleFactor);
                    contentByte.addImage(watermarkImage);
                }

                // Cerrar el documento
                document.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // convenio.setRepresentante(representante);
            convenio.setRuta_marca_convenio(pdfOutputPath);
        }
        convenio.setRuta_marca_convenio(convenio.getRuta_marca_convenio());
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

    @RequestMapping(value = "/openFileConvenioMarca", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<ByteArrayResource> abrirArchivoMedianteRuta(HttpServletResponse response,
            @RequestParam("ruta") String ruta) throws IOException {
        File file = new File(ruta);

        if (file.exists() && file.isFile()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/eliminar-convenio/{id_convenio}")
    public String eliminar_c(HttpServletRequest request, @PathVariable("id_convenio") String id_convenio)
            throws Exception {
        try {
            Long id_conv = Long.parseLong(Encryptar.decrypt(id_convenio));
            Convenio convenio = convenioService.findOne(id_conv);
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            convenio.setId_usu(usuario.getId_usuario());
            convenio.setEstado_convenio("X");
            convenioService.save(convenio);
            return "redirect:/adm/ConvenioL";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
}
