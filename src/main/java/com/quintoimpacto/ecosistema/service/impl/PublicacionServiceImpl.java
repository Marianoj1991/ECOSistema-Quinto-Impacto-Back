package com.quintoimpacto.ecosistema.service.impl;

import com.quintoimpacto.ecosistema.dto.*;
import com.quintoimpacto.ecosistema.model.ImagenPublicacion;
import com.quintoimpacto.ecosistema.model.Publicacion;
import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.repository.ImagenPublicacionRepository;
import com.quintoimpacto.ecosistema.repository.PublicacionRepository;
import com.quintoimpacto.ecosistema.repository.UsuarioRepository;
import com.quintoimpacto.ecosistema.service.abstraction.PublicacionService;
import com.quintoimpacto.ecosistema.service.abstraction.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final ModelMapper modelMapper;
    private final ImagenService imagenService;
    private final UsuarioRepository usuarioRepository;
    private final ImagenPublicacionRepository imagenPublicacionRepository;


    @Override
    @Transactional
    public String crearPublicacion(PublicacionRequest publicacionRequest, List<ImagenesRequestDTO> imagenes){

        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("usuario no encontrado")
        );

        Publicacion publicacion = modelMapper.map(publicacionRequest, Publicacion.class);
        publicacion.setEstaOculto(false);
        publicacion.setDeleted(false);
        publicacion.setCantVis(0);
        publicacion.setFechaAlta(LocalDateTime.now());
        publicacion.setUsuario(usuario);
        Publicacion publicacionId = publicacionRepository.save(publicacion);

        List<ImagenDTO> imagenesDto = imagenes.stream().map(imagen -> imagenService.subirImagenPublicacion( imagen, usuario.getId().toString(), publicacion.getId().toString())).collect(Collectors.toList());

        List<ImagenPublicacion> imagenesEntidad = imagenesDto.stream().map(imagenDTO -> new ImagenPublicacion(imagenDTO.getUrl(), imagenDTO.getOrden(), imagenDTO.getPublicId())).collect(Collectors.toList());

        imagenesEntidad.forEach(imagen-> imagen.setPublicacion(publicacionId));
        publicacionId.setImagenes(imagenesEntidad);

        usuario.getPublicaciones().add(publicacion);
        usuarioRepository.save(usuario);

        return "publicacion creada exitosamente";

    }
    @Override
    @Transactional
    public List<PublicacionDTO> getAllPublicacionesNoOcultas() {
        List<Publicacion> publicaciones = publicacionRepository.findByIsDeletedFalseAndEstaOcultoFalse();
        return publicaciones.stream().map(this::publicacionToPublicacionDTO).toList();
    }
    @Override
    @Transactional
    public PublicacionDTO getPublicacionNoOcultaById(Long publicacionId) throws Exception {
        return publicacionRepository.findByIdAndIsDeletedFalseAndEstaOcultoFalse(publicacionId).map(publicacion -> {

            /*Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean isAdmin = usuario.getRol().name().equals("ADMIN");
            if (!isAdmin) {
                publicacion.setCantVis(publicacion.getCantVis() + 1);
                publicacionRepository.save(publicacion);
            }*/

            /*publicacion.setCantVis(publicacion.getCantVis() + 1);
            publicacionRepository.save(publicacion);*/

            return publicacionToPublicacionDTO(publicacion);
        }).orElseThrow(() -> new Exception("Publicacion with ID " + publicacionId + " not found."));
    }

    public void aumentarVisualizacion(Long publicacionId){
        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalseAndEstaOcultoFalse(publicacionId).orElseThrow(
                () -> new RuntimeException("La publicacion con id: " + publicacionId + " no se encuentra. Probablemente no exista, este eliminada u oculta")
        );
        publicacion.setCantVis(publicacion.getCantVis() + 1);
        publicacionRepository.save(publicacion);
    }

    @Override
    public List<PublicacionAdminDTO> getAllPublicaciones(){
        List<Publicacion> getAll = publicacionRepository.findByIsDeletedFalse();

        return getAll.stream().map(this::publicacionToPublicacionAdminDTO).toList();
    }

    @Override
    public PublicacionAdminDTO getPublicacionById(Long publicacionId){
        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalse(publicacionId).orElseThrow(
                () -> new EntityNotFoundException("La publicacion con id: " + publicacionId + " no existe")
        );
        return publicacionToPublicacionAdminDTO(publicacion);
    }


    @Override
    @Transactional
    public String updatePublicacion(Long publicacionId, PublicacionRequest publicacionUpdateRequest, List<ImagenesRequestDTO> imagenes) throws Exception {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalse(publicacionId).orElseThrow(
                () -> new EntityNotFoundException("No existe la publicacion con id " + publicacionId)
        );

        if (publicacion.getUsuario().getId().equals(usuarioAutenticado.getId())){
            try {
                Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).orElseThrow(
                        () -> new EntityNotFoundException("usuario no encontrado")
                );

                publicacion.setTitulo(publicacionUpdateRequest.getTitulo());
                publicacion.setDescripcion(publicacionUpdateRequest.getDescripcion());


                for (ImagenesRequestDTO imagen : imagenes){
                    Optional<ImagenPublicacion> imagenExiste = imagenPublicacionRepository.findByPublicacionAndOrden(publicacion, imagen.getOrden());
                    ImagenDTO imagenDTO;
                    if (imagenExiste.isPresent()){
                        //Si la imagen existe, se elimina de cloudinary
                        imagenService.eliminarImagen(imagenExiste.get().getPublicId());
                        //Subir la nueva imagen a cloudinary y almacenar url, orden, publicId
                        imagenDTO = imagenService.subirImagenPublicacion(imagen, publicacionId.toString(), usuario.getId().toString());
                        //Actualizar la imagen existente con los nuevos datos
                        ImagenPublicacion imagenActualizada = imagenExiste.get();
                        imagenActualizada.setOrden(imagenDTO.getOrden());
                        imagenActualizada.setUrl(imagenDTO.getUrl());
                        imagenActualizada.setPublicId(imagenDTO.getPublicId());

                        imagenPublicacionRepository.save(imagenExiste.get());
                    }else {
                        //Si la imagen no existe, se sube al cloudinary y se persiste en la DB
                        imagenDTO = imagenService.subirImagenPublicacion(imagen, publicacionId.toString(), usuario.getId().toString());
                        ImagenPublicacion nuevaImagen = new ImagenPublicacion(imagenDTO.getUrl(), imagenDTO.getOrden(), imagenDTO.getPublicId());
                        nuevaImagen.setPublicacion(publicacion);
                        publicacion.getImagenes().add(nuevaImagen);
                    }
                }

                publicacion.setUsuario(usuario);
                usuario.getPublicaciones().add(publicacion);
                usuarioRepository.save(usuario);
                return "publicacion actualizado correctamente";
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }else{return "la publicacion con id "+ publicacionId + " no le pertenece al usuario actualmente autenticado";}


    }

    @Override
    @Transactional
    public void bajaPublicacion (Long publicacionId) throws Exception{
        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalse(publicacionId).orElseThrow(
                ()-> new EntityNotFoundException("publicacion no encontrada")
        );
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (publicacion.getUsuario().getId().equals(usuarioAutenticado.getId())){
            publicacion.setDeleted(true);
            publicacionRepository.save(publicacion);
        }else{
            throw new RuntimeException("La publicacion con id: " + publicacionId + " no le pertenece al usuario actualmente autenticado");
        }
    }

    public void ocultarPublicacion(Long publicacionId){

        Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalse(publicacionId).orElseThrow(
                () -> new EntityNotFoundException("La publicacion con id: " + publicacionId + " no esta oculta, eliminada o no existe")
        );

        if (!usuarioActual.getId().equals(publicacion.getUsuario().getId())){
            throw new RuntimeException("La publicacion con id: " + publicacionId + " no le pertenece al usuario actualmente autenticado");
        }

        publicacion.setEstaOculto(true);
        publicacionRepository.save(publicacion);
    }

    public void desocultarPublicacion(Long publicacionId){

        Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Publicacion publicacion = publicacionRepository.findByIdAndIsDeletedFalse(publicacionId).orElseThrow(
                () -> new EntityNotFoundException("La publicacion con id: " + publicacionId + " no esta oculta, eliminada o no existe")
        );

        if (!usuarioActual.getId().equals(publicacion.getUsuario().getId())){
            throw new RuntimeException("La publicacion con id: " + publicacionId + " no le pertenece al usuario actualmente autenticado");
        }

        publicacion.setEstaOculto(false);
        publicacionRepository.save(publicacion);
    }

    public List<PublicacionDashboardDTO> dataDashboard(){

        List<Publicacion> publicaciones = publicacionRepository.findByisDeletedFalseAndFechaOrdenDesc();

        return publicaciones.stream().map(this::publicacionToPublicacionDashboardDTO).toList();

    }

    private PublicacionDTO publicacionToPublicacionDTO(Publicacion publicacion){
        List<ImagenResponse> imagenes = publicacion.getImagenes().stream().map(imagen ->
                new ImagenResponse(imagen.getUrl(), imagen.getOrden())
        ).toList();
        return PublicacionDTO.builder()
                .id(publicacion.getId())
                .titulo(publicacion.getTitulo())
                .descripcion(publicacion.getDescripcion())
                .fechaAlta(publicacion.getFechaAlta())
                .imagenes(imagenes)
                .build();
    }

    private PublicacionDashboardDTO publicacionToPublicacionDashboardDTO(Publicacion publicacion){
        return PublicacionDashboardDTO.builder()
                .id(publicacion.getId())
                .titulo(publicacion.getTitulo())
                .fecha(publicacion.getFechaAlta())
                .vistas(publicacion.getCantVis())
                .build();
    }

    private PublicacionAdminDTO publicacionToPublicacionAdminDTO(Publicacion publicacion){
        List<ImagenResponse> imagenes = publicacion.getImagenes().stream().map(imagen ->
                new ImagenResponse(imagen.getUrl(), imagen.getOrden())
        ).toList();
        return PublicacionAdminDTO.builder()
                .id(publicacion.getId())
                .titulo(publicacion.getTitulo())
                .descripcion(publicacion.getDescripcion())
                .fechaAlta(publicacion.getFechaAlta())
                .estaOculto(publicacion.isEstaOculto())
                .cantidadVistos(publicacion.getCantVis())
                .imagenes(imagenes)
                .build();
    }



}
