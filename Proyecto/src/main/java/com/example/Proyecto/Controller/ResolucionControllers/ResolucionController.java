package com.example.Proyecto.Controller.ResolucionControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.RespaldoResolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IBeneficiadoService;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IResolucionService;
import com.example.Proyecto.Models.IService.IRespaldoResolucionService;
import com.example.Proyecto.Models.IService.ITipoBeneficiadoService;
import com.example.Proyecto.Models.IService.ITipoResolucionService;
import com.example.Proyecto.Models.Otros.AdjuntarArchivo;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

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

    @Autowired
    private IRespaldoResolucionService respaldoResolucionService;

    @Autowired
    private ITipoResolucionService tipoResolucionService;

    @Autowired
    private IBeneficiadoService beneficiadoService;

    @Autowired
    private ITipoBeneficiadoService tipoBeneficiadoService;

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/ResolucionL", method = RequestMethod.POST)
    public String ResolucionL(@RequestParam(name = "year", required = false) Integer selectedYear,
            HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());

            List<Resolucion> resoluciones;
            if (usuario.getEstado().equals("AU")) {
                resoluciones = resolucionService.findAll();
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(consejo.getId_consejo());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("years", years);

            if (selectedYear != null) {
                // Filtrar resoluciones por el año seleccionado
                resoluciones = resoluciones.stream()
                        .filter(resolucion -> resolucion.getFecha_resolucion().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == selectedYear)
                        .collect(Collectors.toList());
            }

            model.addAttribute("resoluciones", resoluciones);

            return "resolucion/listar-resolucion";
        } else {
            return "redirect:/";
        }
    }

    // FUNCION PARA LISTAR LOS REGISTRO DE PERSONA
    @RequestMapping(value = "/ResolucionLV", method = RequestMethod.GET)
    public String ResolucionLV(@RequestParam(name = "years", required = false) Integer selectedYear,
            HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());

            List<Resolucion> resoluciones;
            if (usuario.getEstado().equals("AU")) {
                resoluciones = resolucionService.findAll();
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(consejo.getId_consejo());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("years", years);

            if (selectedYear != null) {
                // Filtrar resoluciones por el año seleccionado
                resoluciones = resoluciones.stream()
                        .filter(resolucion -> resolucion.getFecha_resolucion().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == selectedYear)
                        .collect(Collectors.toList());
            }

            model.addAttribute("resoluciones", resoluciones);

            return "resolucion/listar-resolucion";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/ResolucionP", method = RequestMethod.GET)
    public String ResolucionP(@RequestParam(name = "year", required = false) Integer selectedYear,
            HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());

            List<Resolucion> resoluciones;
            if (usuario.getEstado().equals("AU")) {
                resoluciones = resolucionService.findAll();
            } else {
                resoluciones = resolucionService.resolucionPorIdConsejo(consejo.getId_consejo());
            }

            Set<Integer> years = resoluciones.stream()
                    .map(resolucion -> resolucion.getFecha_resolucion().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().getYear())
                    .collect(Collectors.toSet());

            model.addAttribute("resolucion", new Resolucion());
            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("tipoResolucioness",
                    tipoResolucionService.tpResolucionPorIdConsejo(consejo.getId_consejo()));
            model.addAttribute("beneficiados", beneficiadoService.findAll());
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("autoridades", autoridadService.autoridadPorIdConsejo(consejo.getId_consejo()));
            model.addAttribute("years", years);

            if (selectedYear != null) {
                // Filtrar resoluciones por el año seleccionado
                resoluciones = resoluciones.stream()
                        .filter(resolucion -> resolucion.getFecha_resolucion().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate().getYear() == selectedYear)
                        .collect(Collectors.toList());

                return "redirect:/adm/ResolucionL";
            }

            model.addAttribute("resoluciones", resoluciones);

            return "resolucion/gestionar-resolucion";
        } else {
            return "redirect:/";
        }
    }
    /*
     * @RequestMapping(value = "ResolucionForm", method = RequestMethod.GET)
     * public String ResolucionR(HttpServletRequest request, @Validated Resolucion
     * resolucion, Model model)
     * throws Exception {
     * if (request.getSession().getAttribute("usuario") != null) {
     * Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
     * Consejo consejo =
     * consejoService.findOne(usuario.getConsejo().getId_consejo());
     * List<Resolucion> resoluciones = resolucionService.findAll();
     * List<TipoResolucion> listTipoR =
     * tipoResolucionService.tpResolucionPorIdConsejo(consejo.getId_consejo());
     * for (TipoResolucion tipoResolucion : listTipoR) {
     * System.out.println(tipoResolucion.getNombre_tipo_resolucion());
     * }
     * 
     * model.addAttribute("resolucion", new Resolucion());
     * model.addAttribute("resoluciones", resoluciones);
     * model.addAttribute("consejos", consejoService.findAll());
     * model.addAttribute("tipoResolucioness",
     * tipoResolucionService.tpResolucionPorIdConsejo(consejo.getId_consejo()));
     * model.addAttribute("beneficiados", beneficiadoService.findAll());
     * model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
     * model.addAttribute("autoridades",
     * autoridadService.autoridadPorIdConsejo(consejo.getId_consejo()));
     * 
     * return "resolucion/gestionar-resolucion";
     * 
     * } else {
     * return "redirect:/";
     * }
     * 
     * }
     */

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
    public String ResolucionF(@Validated Resolucion resolucion, HttpServletRequest request,
            RedirectAttributes redirectAttrs, Model model)
            throws FileNotFoundException, IOException {
        if (request.getSession().getAttribute("usuario") != null) {

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());

            MultipartFile multipartFile = resolucion.getFile();
            MultipartFile multipartFile2 = resolucion.getFile2();

            RespaldoResolucion respaldoResolucion = new RespaldoResolucion();
            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
            AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();

            // Crear directorios necesarios
            Path rootPath = Paths.get("Proyecto/archivos/resoluciones/");
            Path rootPathR = Paths.get("Proyecto/archivos/resoluciones/respaldo");
            Path rootPathM = Paths.get("Proyecto/archivos/marca_agua");

            createDirectoryIfNotExists(rootPath);
            createDirectoryIfNotExists(rootPathR);
            createDirectoryIfNotExists(rootPathM);

            String alfaString = generateRandomAlphaNumericString();
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rootPath.toString());
            String rutaArchivoR = adjuntarArchivo.crearSacDirectorio(rootPathR.toString());
            String rutaDirectorioM = rootPathM.toString() + "/";

            model.addAttribute("di", rutaArchivo);
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();

            resolucion.setNombreArchivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");
            resolucion.setNombreArchivo2("respaldo" + "-" + alfaString + ".pdf");

            Integer ad = adjuntarArchivo.adjuntarArchivoResolucion(resolucion, rutaArchivo);
            Integer ad2 = adjuntarArchivo.adjuntarArchivoResolucionRespaldo(resolucion, rutaArchivoR);
            archivoAdjunto.setNombre_archivo((listArchivos.size() + 1) + "-" + alfaString + ".pdf");

            archivoAdjunto.setRuta(rutaArchivo);
            archivoAdjunto.setEstado_archivo_adjunto("A");
            ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);

            respaldoResolucion.setNombre_archivo("respaldo" + "-" + alfaString + ".pdf");
            respaldoResolucion.setRuta(rutaArchivoR);
            respaldoResolucion.setEstado_archivo_adjunto("A");
            RespaldoResolucion respaldoResolucion2 = respaldoResolucionService
                    .registrarArchivoAdjunto(respaldoResolucion);

            // Ruta completa del archivo PDF original que recibes
            String pdfFilePath = rutaArchivo + resolucion.getNombreArchivo();
            String pdfOutputPath = rutaArchivo + "con_marca_" + resolucion.getNombreArchivo();

            String pdfFilePath2 = rutaArchivoR + resolucion.getNombreArchivo2();
            String pdfOutputPath2 = rutaArchivoR + "con_marca_" + resolucion.getNombreArchivo2();

            // Obtener la ruta del directorio de trabajo actual
            String basePath = System.getProperty("user.dir");

            // Ajustar la ruta de la imagen de la marca de agua
            String watermarkImagePath = Paths.get(basePath, "Proyecto/archivos", "marca_agua", "marca_agua.png").toString();

            // Chequeo para confirmar que el archivo de marca de agua existe
            File watermarkFile = new File(watermarkImagePath);
            if (!watermarkFile.exists()) {
                throw new FileNotFoundException(
                        "La imagen de marca de agua no se encuentra en la ruta: " + watermarkImagePath);
            }

            // Generar el PDF con marca de agua
            generateWatermarkedPdf(pdfFilePath, pdfOutputPath, watermarkImagePath);
            generateWatermarkedPdf(pdfFilePath2, pdfOutputPath2, watermarkImagePath);

            resolucion.setRuta_marca_resolucion(pdfOutputPath);
            resolucion.setRespaldo_marca_resolucion(pdfOutputPath2);
            resolucion.setConsejo(consejo);
            resolucion.setArchivoAdjunto(archivoAdjunto2);
            resolucion.setRespaldoResolucion(respaldoResolucion2);
            resolucion.setEstado_resolucion("A");
            resolucionService.save(resolucion);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(resolucion.getFecha_resolucion());
            int year = calendar.get(Calendar.YEAR);
            redirectAttrs.addAttribute("years", year);
            return "redirect:/adm/ResolucionLV";
        } else {
            return "redirect:/";
        }
    }

    // Método para crear directorios si no existen
    private void createDirectoryIfNotExists(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Directorio creado: " + path);
            } else {
                System.out.println("El directorio ya existe: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error al crear el directorio: " + e.getMessage());
        }
    }

    // Método para generar PDF con marca de agua
    private void generateWatermarkedPdf(String pdfFilePath, String pdfOutputPath, String watermarkImagePath) {
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfOutputPath));
            document.open();

            PdfReader reader = new PdfReader(pdfFilePath);
            int pageCount = reader.getNumberOfPages();
            com.itextpdf.text.Image watermarkImage = com.itextpdf.text.Image.getInstance(watermarkImagePath);

            float xPosition = 80; // Cambia esto según tus necesidades
            float yPosition = 100; // Cambia esto según tus necesidades
            float scaleFactor = 0.4f; // Cambia esto para ajustar la escala

            for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
                document.newPage();
                PdfContentByte contentByte = writer.getDirectContent();
                PdfImportedPage page = writer.getImportedPage(reader, pageNumber);
                contentByte.addTemplate(page, 0, 0);

                watermarkImage.setAbsolutePosition(xPosition, yPosition);
                watermarkImage.scaleAbsolute(watermarkImage.getWidth() * scaleFactor,
                        watermarkImage.getHeight() * scaleFactor);
                contentByte.addImage(watermarkImage);
            }

            document.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/editar-resolucion/{id_resolucion}")
    public String editar_p(@PathVariable("id_resolucion") Long id_resolucion, HttpServletRequest request, Model model)
            throws NumberFormatException, Exception {
        if (request.getSession().getAttribute("usuario") != null) {

            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
            Resolucion resolucion = resolucionService.findOne(id_resolucion);
            model.addAttribute("resolucion", resolucion);

            List<Resolucion> resoluciones = resolucionService.findAll();

            model.addAttribute("resoluciones", resoluciones);
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("tipoResolucioness",
                    tipoResolucionService.tpResolucionPorIdConsejo(consejo.getId_consejo()));
            model.addAttribute("beneficiados", beneficiadoService.findAll());
            model.addAttribute("tipoBeneficiados", tipoBeneficiadoService.findAll());
            model.addAttribute("autoridades", autoridadService.findAll());

            return "resolucion/gestionar-resolucion";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping(value = "/ResolucionModF")
    public String ResolucionModF(@RequestParam("id_resolucion") Long idResolucion, @Validated Resolucion resolucion,
            RedirectAttributes redirectAttrs, Model model,
            HttpServletRequest request)
            throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Consejo consejo = consejoService.findOne(usuario.getConsejo().getId_consejo());
        Resolucion resolucionActual = resolucionService.findOne(idResolucion);
        ArchivoAdjunto archivoAdjuntoActual = archivoAdjuntoService.buscarArchivoAdjuntoPorResolucion(idResolucion);
        RespaldoResolucion respaldoActual = respaldoResolucionService.buscarArchivoAdjuntoPorResolucion(idResolucion);

        String archivoNombreString = archivoAdjuntoActual.getNombre_archivo();
        String respaldoNombreString = respaldoActual.getNombre_archivo();

        MultipartFile multipartFile = resolucion.getFile();

        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        MultipartFile multipartFile2 = resolucion.getFile2();

        RespaldoResolucion respaldoResolucion = new RespaldoResolucion();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
        String alfaString = generateRandomAlphaNumericString();

        Path rootPath = Paths.get("Proyecto/archivos/resoluciones/");
        Path rootAbsolutPath = rootPath.toAbsolutePath();
        String rutaDirectorio = rootAbsolutPath + "";
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);

        Path rootPathR = Paths.get("Proyecto/archivos/resoluciones/respaldo");
        Path rootAbsolutPathR = rootPathR.toAbsolutePath();
        String rutaDirectorioR = rootAbsolutPathR + "";
        String rutaArchivoR = adjuntarArchivo.crearSacDirectorio(rutaDirectorioR);

        Path rootPathM = Paths.get("Proyecto/archivos/marca_agua");
        Path rootAbsolutPathM = rootPathM.toAbsolutePath();
        String rutaDirectorioM = rootAbsolutPathM + "/";

        if (archivoNombreString.startsWith("mod")) {
            resolucion.setNombreArchivo((archivoAdjuntoActual.getNombre_archivo()));
            resolucion.setNombreArchivo2(respaldoActual.getNombre_archivo());
        } else {
            resolucion.setNombreArchivo(("mod-" + archivoAdjuntoActual.getNombre_archivo()));
            resolucion.setNombreArchivo2("mod-respaldo" + "-" + archivoAdjuntoActual.getNombre_archivo());
        }

        Integer ad = adjuntarArchivo.adjuntarArchivoResolucion(resolucion, rutaArchivo);
        Integer ad2 = adjuntarArchivo.adjuntarArchivoResolucionRespaldo(resolucion, rutaArchivoR);

        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorResolucion(resolucion.getId_resolucion());

            barchivoAdjunto.setNombre_archivo(resolucion.getNombreArchivo());
            barchivoAdjunto.setRuta(rutaArchivo);
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);

            // Ruta completa del archivo PDF original que recibes
            String pdfFilePath = rutaArchivo + resolucion.getNombreArchivo();

            // Ruta donde guardarás el PDF con marca de agua
            String pdfOutputPath = rutaArchivo + "con_marca_" + resolucion.getNombreArchivo();
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

            resolucion.setRuta_marca_resolucion(pdfOutputPath);

        }
        if (ad2 == 1) {
            RespaldoResolucion respaldoArchivo = respaldoResolucionService
                    .buscarArchivoAdjuntoPorResolucion(resolucion.getId_resolucion());
            respaldoArchivo.setNombre_archivo(resolucion.getNombreArchivo2());
            respaldoArchivo.setRuta(rutaArchivoR);
            respaldoResolucionService.modificarArchivoAdjunto(respaldoArchivo);

            // Ruta completa del archivo PDF original que recibes
            String pdfFilePath = rutaArchivoR + resolucion.getNombreArchivo2();

            // Ruta donde guardarás el PDF con marca de agua
            String pdfOutputPath = rutaArchivoR + "con_marca_" + resolucion.getNombreArchivo2();
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
            resolucion.setRespaldo_marca_resolucion(pdfOutputPath);

        }

        resolucion.setRespaldo_marca_resolucion(resolucion.getRespaldo_marca_resolucion());
        resolucion.setRuta_marca_resolucion(resolucion.getRuta_marca_resolucion());

        resolucion.setConsejo(consejo);
        resolucion.setEstado_resolucion("A");
        resolucionService.save(resolucion);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resolucion.getFecha_resolucion());

        // Obtener el año como un entero
        int year = calendar.get(Calendar.YEAR);
        redirectAttrs.addAttribute("years", year);
        return "redirect:/adm/ResolucionLV";

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

    @RequestMapping(value = "/openFileResolucionRespaldo/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourseRespaldo(HttpServletResponse response,
            @PathVariable("id") long id_resolucion) throws FileNotFoundException {
        RespaldoResolucion respaldoResolucion = respaldoResolucionService
                .buscarArchivoAdjuntoPorResolucion(id_resolucion);

        File file = new File(respaldoResolucion.getRuta() + respaldoResolucion.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/openFileResolucionMarca", method = RequestMethod.GET, produces = "application/pdf")
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

    @RequestMapping(value = "/eliminar-resolucion/{id_resolucion}")
    public String eliminar_resolucion(HttpServletRequest request, @PathVariable("id_resolucion") Long id_resolucion)
            throws Exception {
        if (request.getSession().getAttribute("usuario") != null) {
            Resolucion resolucion = resolucionService.findOne(id_resolucion);

            resolucion.setEstado_resolucion("X");
            resolucionService.save(resolucion);

            return "redirect:/adm/ResolucionL";
        } else {
            return "redirect:/";
        }

    }

    /*
     * @GetMapping(value = "/GenerarMarcaAguaResoluciones")
     * public String GenerarMarcaAguaResoluciones( RedirectAttributes redirectAttrs,
     * Model model,
     * HttpServletRequest request)
     * throws IOException {
     * 
     * AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();
     * 
     * Path rootPath = Paths.get("archivos/resoluciones/");
     * Path rootAbsolutPath = rootPath.toAbsolutePath();
     * String rutaDirectorio = rootAbsolutPath + "";
     * String rutaArchivo = adjuntarArchivo.crearSacDirectorio(rutaDirectorio);
     * 
     * Path rootPathM = Paths.get("archivos/marca_agua");
     * Path rootAbsolutPathM = rootPathM.toAbsolutePath();
     * String rutaDirectorioM = rootAbsolutPathM + "/";
     * 
     * long idInicial = 1; // ID inicial
     * long idFinal = 4; // ID final
     * 
     * for (long id = idInicial; id <= idFinal; id++) {
     * 
     * // Busca el archivo adjunto por cada ID
     * ArchivoAdjunto archivo =
     * archivoAdjuntoService.buscarArchivoAdjuntoPorResolucion(id);
     * Resolucion resolucion = resolucionService.findOne(id);
     * 
     * // Ruta completa del archivo PDF original que recibes
     * String pdfFilePath = rutaArchivo + File.separator +
     * archivo.getNombre_archivo();
     * 
     * // Ruta donde guardarás el PDF con marca de agua
     * String pdfOutputPath = rutaArchivo + File.separator + "con_marca_" +
     * archivo.getNombre_archivo();
     * String watermarkImagePath = rutaDirectorioM + "marca_agua.png";
     * try {
     * // Crear un nuevo documento PDF de salida
     * com.itextpdf.text.Document document = new com.itextpdf.text.Document();
     * 
     * // Inicializar el escritor de PDF para el nuevo documento
     * PdfWriter writer = PdfWriter.getInstance(document, new
     * FileOutputStream(pdfOutputPath));
     * document.open();
     * 
     * // Cargar el PDF original
     * PdfReader reader = new PdfReader(pdfFilePath);
     * 
     * // Obtener el número total de páginas en el PDF original
     * int pageCount = reader.getNumberOfPages();
     * 
     * // Cargar la imagen de la marca de agua
     * com.itextpdf.text.Image watermarkImage =
     * com.itextpdf.text.Image.getInstance(watermarkImagePath);
     * 
     * // Definir la posición y la escala de la marca de agua
     * float xPosition = 80; // Cambia esto según tus necesidades
     * float yPosition = 100; // Cambia esto según tus necesidades
     * float scaleFactor = 0.4f; // Cambia esto para ajustar la escala
     * 
     * // Iterar a través de las páginas del PDF original
     * for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
     * // Agregar una nueva página al documento de salida
     * document.newPage();
     * 
     * // Obtener el contenido de la página actual
     * PdfContentByte contentByte = writer.getDirectContent();
     * 
     * // Obtener la página actual del PDF original
     * PdfImportedPage page = writer.getImportedPage(reader, pageNumber);
     * 
     * // Agregar la página del PDF original al nuevo documento
     * contentByte.addTemplate(page, 0, 0);
     * 
     * // Agregar la marca de agua (imagen) a la página actual
     * watermarkImage.setAbsolutePosition(xPosition, yPosition);
     * watermarkImage.scaleAbsolute(watermarkImage.getWidth() * scaleFactor,
     * watermarkImage.getHeight() * scaleFactor);
     * contentByte.addImage(watermarkImage);
     * }
     * 
     * // Cerrar el documento
     * document.close();
     * reader.close();
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * 
     * 
     * resolucion.setRuta_marca_resolucion(pdfOutputPath);
     * resolucion.setEstado_resolucion("A");
     * resolucionService.save(resolucion);
     * }
     * return "redirect:/";
     * }
     */
    @GetMapping(value = "/verificarArchivos")
    public String verificarArchivos(RedirectAttributes redirectAttrs, Model model, HttpServletRequest request) {
        // Obtener todos los registros de ArchivoAdjunto
        List<RespaldoResolucion> respaldos = respaldoResolucionService.listarArchivoAdjuntoJPQL();

        // Crear una lista para almacenar las Resoluciones que necesitan ser verificadas
        List<Resolucion> resolucionesPorVerificar = new ArrayList<>();

        // Iterar sobre cada registro y verificar si el archivo asociado existe
        for (RespaldoResolucion respaldo : respaldos) {
            String rutaArchivo = respaldo.getRuta();
            File archivoVerificacion = new File(rutaArchivo);

            if (!archivoVerificacion.exists()) {
                // Si el archivo no existe, agregar la Resolución correspondiente a la lista

                Resolucion resolucion = resolucionService.resolucionPorRespaldo(respaldo.getId_respaldo_resolucion());
                resolucionesPorVerificar.add(resolucion);
            }
        }

        // Aquí puedes hacer lo que necesites con la lista de resoluciones por verificar
        // Por ejemplo, puedes agregarla al modelo para mostrarla en tu vista
        model.addAttribute("resolucionesPorVerificar", resolucionesPorVerificar);

        // Redirigir a algún lugar, por ejemplo, la página de inicio
        return "prueba/prueba";
    }

}
