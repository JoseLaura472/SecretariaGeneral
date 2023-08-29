package com.example.Proyecto.Models.Otros;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.springframework.web.multipart.MultipartFile;

import com.example.Proyecto.Models.Entity.Convenio;
import com.example.Proyecto.Models.Entity.Resolucion;



public class AdjuntarArchivo {
    MultipartFile file; 

    public AdjuntarArchivo() {
     }
    
    public String crearSacDirectorio(String sDirectorio){
        File directorio = new File(sDirectorio);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                  return  directorio.getPath()+"/";
            } else {
                  return null;
            }
        }
        
        return directorio.getPath()+"/";
    }

    public Integer adjuntarArchivoConvenio(Convenio convenio, String rutaArchivo) throws FileNotFoundException, IOException{

        // Save file on system
    file = convenio.getFile();
    if (!file.getOriginalFilename().isEmpty()) {
       BufferedOutputStream outputStream = new BufferedOutputStream(
             new FileOutputStream(
                   new File(rutaArchivo, convenio.getNombreArchivo())));//file.getOriginalFilename())));
       outputStream.write(file.getBytes());
       outputStream.flush();
       outputStream.close();
    }else{
       return 0; // Error: No es posible adjuntar
    }
    
    return 1; // Adjuntado Correctamente
 }

  public Integer adjuntarArchivoResolucion(Resolucion resolucion, String rutaArchivo) throws FileNotFoundException, IOException{

        // Save file on system
    file = resolucion.getFile();
    if (!file.getOriginalFilename().isEmpty()) {
       BufferedOutputStream outputStream = new BufferedOutputStream(
             new FileOutputStream(
                   new File(rutaArchivo, resolucion.getNombreArchivo())));//file.getOriginalFilename())));
       outputStream.write(file.getBytes());
       outputStream.flush();
       outputStream.close();
    }else{
       return 0; // Error: No es posible adjuntar
    }
    
    return 1; // Adjuntado Correctamente
 }



}

