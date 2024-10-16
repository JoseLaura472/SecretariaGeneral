package com.example.Proyecto.Controller.ReporteControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Proyecto.Models.Entity.ArchivoAdjunto;
import com.example.Proyecto.Models.Entity.Autoridad;
import com.example.Proyecto.Models.Entity.Consejo;
import com.example.Proyecto.Models.Entity.Resolucion;
import com.example.Proyecto.Models.Entity.Usuario;
import com.example.Proyecto.Models.IService.IArchivoAdjuntoService;
import com.example.Proyecto.Models.IService.IAutoridadService;
import com.example.Proyecto.Models.IService.IConsejoService;
import com.example.Proyecto.Models.IService.IConvenioService;
import com.example.Proyecto.Models.IService.IResolucionService;

@Controller
@RequestMapping("/adm")
public class ReporteController {

    @Autowired
    private IConvenioService convenioService;

    @Autowired
    private IAutoridadService autoridadService;

    @Autowired
    private IConsejoService consejoService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    @Autowired
    private IResolucionService resolucionService;

    @RequestMapping(value = "ReporteCon", method = RequestMethod.GET)
    public String PlantillaBasia(HttpServletRequest request, Model model) {

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

            model.addAttribute("years", years);

            model.addAttribute("convenios", convenioService.findAll());
            model.addAttribute("autoridades", autoridadService.findAll());
            model.addAttribute("consejos", consejoService.findAll());
            model.addAttribute("resoluciones", resolucionService.findAll());
            model.addAttribute("consejocau", consejoService.listarConsejoCau());
            model.addAttribute("consejofacultad", consejoService.listarConsejoFacultad());
            model.addAttribute("consejocarrera", consejoService.listarConsejoCarrera());

            return "reporte/generar-reporte";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/Autori", method = RequestMethod.GET)
    public @ResponseBody List<Autoridad> findAllAgencie(
            @RequestParam(value = "autoriId", required = true) Long citId) {
        List<Autoridad> autoridades = autoridadService.autoridadPorIdConsejo(citId);
        return autoridades;
    }

    /* INICIO GENERAR REPORTE PARA EL CONVENIO */

    @PostMapping("/generarReporteAutoridadConvenio")
    public String generarReporteAutoridadTipoCon(
            @RequestParam("id_autoridad") Long id_autoridad,
            @RequestParam("id_consejo") Long id_consejo, Model model) {

        Autoridad autoridad = autoridadService.findOne(id_autoridad);
        Consejo consejo = consejoService.findOne(id_consejo);

        model.addAttribute("convenios", convenioService.convenioPorAutoridadConsejo(id_autoridad, id_consejo));

        model.addAttribute("autoridad", autoridad);
        model.addAttribute("consejo", consejo);

        return "reporte/tabla-convenios";
    }

    @PostMapping("/generarReporteFechasConvenio")
    public String generarReporteFechasConvenio(
        @RequestParam("fechaInicio2") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaInicio,
        @RequestParam("fechaFin2") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaFin,
        @RequestParam(value = "id_consejo3") Long id_consejo, Model model)
            throws FileNotFoundException, IOException {

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateado = formato.format(fechaInicio);
                String fechaFinFormateado = formato.format(fechaFin);
                 Consejo consejo = consejoService.findOne(id_consejo);
        
        model.addAttribute("consejo", consejo);
        model.addAttribute("convenios", convenioService.buscarConveniosPorIntervaloDeFechas(fechaInicio, fechaFin, id_consejo));
        model.addAttribute("fechaIni", fechaInicioFormateado);
        model.addAttribute("fechaFin", fechaFinFormateado);
   
        return "reporte/tabla-convenios";
    }

    @RequestMapping(value = "/openFileReportConsjAuto/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_convenio) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorConvenio(id_convenio);
        File file = new File(ArchivoAdjuntos.getRuta() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/openFileReportConsjAutoMarca", method = RequestMethod.GET, produces = "application/pdf")
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

    /* FIN GENERAR REPORTE PARA EL CONVENIO */

    /* INICIO GENERAR REPORTE PARA LA RESOLUCION */

    @PostMapping("/generarReporteAutoridadResolucion")
    public String generarReporteAutoridadResolucion(
            @RequestParam("id_autoridad1") Long id_autoridad,
            @RequestParam("id_consejo1") Long id_consejo, Model model)
            throws FileNotFoundException, IOException {

        Autoridad autoridad = autoridadService.findOne(id_autoridad);
        Consejo consejo = consejoService.findOne(id_consejo);

        model.addAttribute("autoridad", autoridad);
        model.addAttribute("consejo", consejo);

        model.addAttribute("resoluciones", resolucionService.resolucionPorAutoridadConsejo(id_autoridad, id_consejo));
        

   
        return "reporte/tabla-resolucion";
    }

    @PostMapping("/generarReporteFechasResolucion")
    public String generarReporteFechasResolucion(
        @RequestParam("fechaInicio") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaFin,
        @RequestParam(value = "id_consejo1") Long id_consejo, Model model)
            throws FileNotFoundException, IOException {

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateado = formato.format(fechaInicio);
                String fechaFinFormateado = formato.format(fechaFin);
                 Consejo consejo = consejoService.findOne(id_consejo);
        
        model.addAttribute("consejo", consejo);
        model.addAttribute("resoluciones", resolucionService.buscarResolucionesPorIntervaloDeFechas(fechaInicio, fechaFin, id_consejo));
        model.addAttribute("fechaIni", fechaInicioFormateado);
        model.addAttribute("fechaFin", fechaFinFormateado);
   
        return "reporte/tabla-resolucion";
    }

    @RequestMapping(value = "/openFileReporteResolucion/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource RabrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_resolucion) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorResolucion(id_resolucion);
        File file = new File(ArchivoAdjuntos.getRuta() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/openFileReporteResolucionMarca", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<ByteArrayResource> RabrirArchivoMedianteRuta(HttpServletResponse response,
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





}
