package com.quintoimpacto.ecosistema.service.abstraction;

import com.quintoimpacto.ecosistema.dto.*;
import com.quintoimpacto.ecosistema.model.Publicacion;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PublicacionService {
    String crearPublicacion(PublicacionRequest publicacionRequest, List<ImagenesRequestDTO> imagenes);
    PublicacionDTO getPublicacionNoOcultaById(Long publicacionId) throws Exception;
    void aumentarVisualizacion(Long publicacionId);
    List<PublicacionDTO> getAllPublicacionesNoOcultas();
    List<PublicacionAdminDTO> getAllPublicaciones();
    PublicacionAdminDTO getPublicacionById(Long publicacionId);

    List<PublicacionDashboardDTO> dataDashboard();
    String updatePublicacion(Long publicacionId, PublicacionRequest publicacionUpdateRequest, List<ImagenesRequestDTO> imagenes) throws Exception;
    void ocultarPublicacion(Long publicacionId);
    void desocultarPublicacion(Long publicacionId);
    void bajaPublicacion(Long publicacionId) throws Exception;
}
