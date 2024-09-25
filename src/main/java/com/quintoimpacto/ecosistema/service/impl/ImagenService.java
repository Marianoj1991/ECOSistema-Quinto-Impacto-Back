package com.quintoimpacto.ecosistema.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quintoimpacto.ecosistema.dto.ImagenDTO;
import com.quintoimpacto.ecosistema.dto.ImagenesRequestDTO;
import com.quintoimpacto.ecosistema.exception.InvalidImagenFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final Cloudinary cloudinary;
    private static final List<String> EXTENSIONES_PERMITIDAS = Arrays.asList(".jpg", ".jpeg", ".png");

    public ImagenDTO subirImagenProveedor(ImagenesRequestDTO imagen, String userId, String proveedorId) {

        if (imagen == null || userId == null || proveedorId == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }

            String nombreImagen = imagen.getImagen().getOriginalFilename().split("\\.")[0];
            String publicId = "usuario/" + userId + "/producto-servicio/" + proveedorId + "/" + nombreImagen;
            Map params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"

            );
            ImagenDTO imagenDto = null;
            try {

                //Si la imagen no tiene un formato valido lanza una excepcion, sin ejecutar
                //el resto del bloque try
                validarImagen(imagen.getImagen());
                cloudinary.uploader().upload(imagen.getImagen().getBytes(), params);


                imagenDto= ImagenDTO.builder()
                                .url(generarUrlImagenes(publicId))
                                .orden(imagen.getOrden())
                                .publicId(publicId)
                        .build();


            } catch (IOException | InvalidImagenFormatException e) {
                System.out.println("Error al subir imagenes: " + e.getMessage());
            }

        if (imagenDto == null) {
            throw new RuntimeException("No se pudo subir la imagen");
        }
        return imagenDto;
    }

    //Todo: hacer un solo metodo para subir imagenes
    public ImagenDTO subirImagenPublicacion(ImagenesRequestDTO imagen, String userId, String publicacionId){
        if (imagen == null || userId == null || publicacionId == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        /*List<ImagenDTO> imagenDto = new ArrayList<>();*/
        /*for (ImagenesRequestDTO imagen : imagenes) {}*/
        
            String nombreImagen = imagen.getImagen().getOriginalFilename().split("\\.")[0];
            String publicId = "usuario/" + userId + "/publicacion/" + publicacionId + "/" + nombreImagen;
            Map params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            );

            ImagenDTO imagenDto = null;
            try {

                //Si la imagen no tiene un formato valido lanza una excepcion, sin ejecutar
                //el resto del bloque try
                validarImagen(imagen.getImagen());
                cloudinary.uploader().upload(imagen.getImagen().getBytes(), params);
                 imagenDto = ImagenDTO.builder()
                        .url(generarUrlImagenes(publicId))
                        .orden(imagen.getOrden())
                        .publicId(publicId)
                        .build();
            } catch (IOException | InvalidImagenFormatException e) {
                System.out.println("Error al subir imagenes: " + e.getMessage());
            }
            return imagenDto;
    }

    public void eliminarImagen(String publicId){

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String generarUrlImagenes(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    private void validarImagen(MultipartFile imagen) throws InvalidImagenFormatException{
        String imagenNombre = Objects.requireNonNull(imagen
                .getOriginalFilename(), "la imagen no tiene nombre").toLowerCase();

        if (!esExtensionPermitida(imagenNombre)){
            throw new InvalidImagenFormatException(imagenNombre + ": Solo se aceptan archivos JPG, JPEG y PNG");
        }

    }

    private Boolean esExtensionPermitida(String imagenNombre){
        //return imagenNombre.endsWith(".jpg") || imagenNombre.endsWith(".jpeg") || imagenNombre.endsWith(".png");
        return EXTENSIONES_PERMITIDAS.stream().anyMatch(imagenNombre::endsWith);
    }

}



