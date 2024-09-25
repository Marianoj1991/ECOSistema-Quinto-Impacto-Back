package com.quintoimpacto.ecosistema.service.impl;

import com.quintoimpacto.ecosistema.dto.*;
import com.quintoimpacto.ecosistema.exception.MaxProductoServicioReachedException;
import com.quintoimpacto.ecosistema.exception.ProductoYServicioNotFoundException;
import com.quintoimpacto.ecosistema.model.*;
import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import com.quintoimpacto.ecosistema.repository.ImagenProductoServicioRepository;
import com.quintoimpacto.ecosistema.repository.ProductoServicioRepository;
import com.quintoimpacto.ecosistema.repository.UsuarioRepository;
import com.quintoimpacto.ecosistema.service.abstraction.CategoriaService;
import com.quintoimpacto.ecosistema.service.abstraction.PaisService;
import com.quintoimpacto.ecosistema.service.abstraction.ProvinciaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//TODO: todos los metodos que devuelvan proveedores deben devolver solo los activos
public class ProductoServicioServiceImpl {

    private final PaisService paisService;
    private final UsuarioRepository usuarioRepository;
    private final ProvinciaService provinciaService;
    private final CategoriaService categoriaService;
    private final ProductoServicioRepository productoServicioRepository;
    private final ModelMapper modelMapper;
    private final ImagenService imagenService;
    private final ImagenProductoServicioRepository imagenProductoServicioRepository;


    // User
    @Transactional
    public String createProductoServicio(ProductoServicioRequest productoServicioRequest, List<ImagenesRequestDTO> imagenes, Authentication authUser) {
        Usuario usuarioAutenticado = (Usuario) authUser.getPrincipal();
        Usuario usuarioActual = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        //Si el contador es nulo lo seteamos a 0
        if (usuarioActual.getProductoyservicioCantidad() == null) {
            usuarioActual.setProductoyservicioCantidad(0);
        }
        //verificamos que el contador no sea mayor a 3
        if (usuarioActual.getProductoyservicioCantidad() < 3) {

            ProductoServicio nuevoProductoServicio = productoServicioRequestToProductoServicio(productoServicioRequest);
            // Persisto el proveedor para generar su id
            ProductoServicio productoServicioPersistido = productoServicioRepository.save(nuevoProductoServicio);

            productoServicioPersistido.setEstado(EstadoProductoServicio.REVISION_INICIAL);
            productoServicioPersistido.setFeedback("Producto/servicio en revision");
            // Manejamos las imagenes - guardar url
            List<ImagenDTO> urls = imagenes.stream().map(imagen -> imagenService.subirImagenProveedor(imagen, usuarioActual.getId().toString(), productoServicioPersistido.getId().toString())).collect(Collectors.toList());

            List <ImagenProductoServicio> imagenProductoServicios = urls.stream().map(imagen -> new ImagenProductoServicio(imagen.getUrl(), imagen.getOrden(),imagen.getPublicId())).collect(Collectors.toList());

            // Asignamos las imagenes al productoServicioPersistido persistido
            imagenProductoServicios.forEach(imagen -> {
                imagen.setProductoServicio(productoServicioPersistido);
                productoServicioPersistido.getImagenes().add(imagen);

            });

            usuarioActual.getProductoServicios().add(productoServicioPersistido);
            usuarioActual.setProductoyservicioCantidad(usuarioActual.getProductoyservicioCantidad() + 1);
            nuevoProductoServicio.setUsuario(usuarioActual);
            usuarioRepository.save(usuarioActual);

            return "Producto o servicio creado exitosamente";

        } else {
            throw new MaxProductoServicioReachedException("El usuario " + usuarioActual.getNombre() + " alcanzo el maximo de productos/servicios permitido (3)");
        }
    }

    // Cualquier usuario
    public List<ProductoServicioResponse> getByCategoriaAceptadoActivo(String nombreCategoria){
        //List<Proveedor> proveedorPorCategoria = categoriaService.getCategoriaByNombre(nombreCategoria).getProveedores();
        Categoria categoria = categoriaService.getCategoriaByNombre(nombreCategoria);

        List<ProductoServicio> proveedorPorCategoria = productoServicioRepository.findByCategoriaAndEstadoAndIsDeletedFalse(categoria, EstadoProductoServicio.ACEPTADO);
        return proveedorPorCategoria.stream().map(this::productoServicioToProductoServicioResponse).toList();

    }
    public List<ProductoServicioResponse> searchByNombreAceptadoActivo(String productoServicioNombre) {
        List<ProductoServicio> productoServicioList = productoServicioRepository.findByNombre(productoServicioNombre);

        return productoServicioList.stream().map(this::productoServicioToProductoServicioResponse).toList();
    }


    public List<ProductoServicioResponse> getAllProductoServicioActivoAceptado() {
        List<ProductoServicio> productoServicios = productoServicioRepository.findByIsDeletedFalseAndEstado(EstadoProductoServicio.ACEPTADO);

        return productoServicios.stream().map(this::productoServicioToProductoServicioResponse).collect(Collectors.toList());
    }

    // Guest
    public ProductoServicioResponse getProductoYServicioByIdActivoAceptado(Long productoServiceId) throws Exception {
        ProductoServicio productoServicio = productoServicioRepository.findByIdAndIsDeletedFalseAndEstado(productoServiceId, EstadoProductoServicio.ACEPTADO).orElseThrow(() -> new Exception("Producto/servicio with ID " + productoServiceId + " not found."));
        return productoServicioToProductoServicioResponse(productoServicio);
    }

    // User (autenticado)
    @Transactional
    public String updateProductoServicio(Authentication authUser, ProductoServicioUpdateRequest productoServicioUpdateRequest, List<ImagenesRequestDTO> imagenes, Long productoServicioId) {

        //El estado de aceptacion del proveedor no se actualiza, ya que este metodo solo lo utilizan los usuarios para editar
        // sus proveedores publicados, para cambiar el estado y otras cosas solo la gente

        //con permiso de admin podrian
        Usuario actualUser = (Usuario) authUser.getPrincipal();

        ProductoServicio updatedProductoServicio = productoServicioRepository.findByIdAndIsDeletedFalse(productoServicioId).orElseThrow(() -> new EntityNotFoundException("Producto/Servicio no encontrado"));


        if (updatedProductoServicio.getUsuario().getId().equals(actualUser.getId())){

            /*List<ImagenProductoServicio> imagenesExistentes = updatedProductoServicio.getImagenes();*/
            //Imagenes
            for (ImagenesRequestDTO imagen : imagenes){
                Optional<ImagenProductoServicio> imagenExiste = imagenProductoServicioRepository.findByOrdenAndProductoServicio(imagen.getOrden(), updatedProductoServicio);
                ImagenDTO imagenDTO;
                if (imagenExiste.isPresent()){
                    //Si la imagen existe, se elimina de cloudinary
                    imagenService.eliminarImagen(imagenExiste.get().getPublicId());
                    //Subir la nueva imagen a cloudinary y almacenar url, orden, publicId
                    imagenDTO = imagenService.subirImagenProveedor(imagen, productoServicioId.toString(), actualUser.getId().toString());
                    //Actualizar la imagen existente con los nuevos datos
                    ImagenProductoServicio imagenActualizada = imagenExiste.get();
                    imagenActualizada.setOrden(imagenDTO.getOrden());
                    imagenActualizada.setUrl(imagenDTO.getUrl());
                    imagenActualizada.setPublicId(imagenDTO.getPublicId());

                    imagenProductoServicioRepository.save(imagenActualizada);
                }else{
                    //Si la imagen existe, se sube al cloudinary y se persiste a la DB
                    imagenDTO = imagenService.subirImagenProveedor(imagen, productoServicioId.toString(), actualUser.getId().toString());
                    ImagenProductoServicio nuevaImagen = new ImagenProductoServicio(imagenDTO.getUrl(), imagenDTO.getOrden(), imagenDTO.getPublicId());
                    nuevaImagen.setProductoServicio(updatedProductoServicio);
                    updatedProductoServicio.getImagenes().add(nuevaImagen);
                }
            }

            //Demas campos
            Pais pais = paisService.getByName(productoServicioUpdateRequest.getPais());
            Provincia provincia = provinciaService.getProvinciaByNombreYPais(productoServicioUpdateRequest.getProvincia(), pais);
            Categoria categoria = categoriaService.getCategoriaByNombre(productoServicioUpdateRequest.getCategoria());
            updatedProductoServicio.setNombre(productoServicioUpdateRequest.getNombre());
            updatedProductoServicio.setDescripcionLarga(productoServicioUpdateRequest.getDescripcionLarga());
            updatedProductoServicio.setDescripcionCorta(productoServicioUpdateRequest.getDescripcionCorta());
            updatedProductoServicio.setPais(pais);
            updatedProductoServicio.setProvincia(provincia);
            updatedProductoServicio.setCiudad(productoServicioUpdateRequest.getCiudad());
            updatedProductoServicio.setCategoria(categoria);
            updatedProductoServicio.setFacebook(productoServicioUpdateRequest.getFacebook());
            updatedProductoServicio.setInstagram(productoServicioUpdateRequest.getInstagram());
            updatedProductoServicio.setTelefono(productoServicioUpdateRequest.getTelefono());
            updatedProductoServicio.setEstado(EstadoProductoServicio.CAMBIOS_REALIZADOS);

            productoServicioRepository.save(updatedProductoServicio);

            return "Producto/servicio actualizado exitosamente";

        }else{return "El producto/servicio id: " + productoServicioId +" no le pertenece al usuario actualmente autenticado";}

    }



    //USER (autenticado)
    public String bajaProveedorActivo(Long productoServicioID,Authentication authUser ){

        Usuario usuarioActual = (Usuario) authUser.getPrincipal();
        ProductoServicio proveedor = productoServicioRepository.findByIdAndIsDeletedFalse(productoServicioID).orElseThrow(
                ()-> new ProductoYServicioNotFoundException("Producto/servicio con id " + productoServicioID + " no encontrado")
        );

        if (proveedor.getUsuario().getId().equals(usuarioActual.getId())){
            Usuario usuario = proveedor.getUsuario();
            proveedor.setDeleted(true);
            usuario.setProductoyservicioCantidad(usuario.getProductoyservicioCantidad() -1);
            productoServicioRepository.save(proveedor);
            usuarioRepository.save(usuario);
            return "Producto/servicio dado de baja";

        }else {
            return "El roducto/servicio con id: " + productoServicioID +" no le pertenece al usuario actualmente autenticado";
        }

    }

    // User (autenticado)
    public ProductoServicioCompleteResponse getOwnProductoServicioById(Long id){
        Usuario authUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductoServicio productoServicio = productoServicioRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ProductoYServicioNotFoundException("Producto o servicio con id: "+ id +" no encontrado")
        );

        if (!authUser.getId().equals(productoServicio.getUsuario().getId())){
            throw new RuntimeException("El producto/servicio con id: " + id + " no le pertenece al usuario actualmente autenticado");
        }
        return productoServicioToProductoServicioCompleteResponse(productoServicio);
    }

    //ADMIN
    @Transactional
    public String deleteProductoServicio(Long id){
            //si el productoServicio existe entonces el usuario debe tener minimo 1 productoServicio
            ProductoServicio productoServicio = productoServicioRepository.findById(id).orElseThrow(() ->new ProductoYServicioNotFoundException("Proveedor no encontrado con ID: " + id));
            // Obtenemos el usuario
            Usuario usuario = productoServicio.getUsuario();
            //Verificamos que el contador en usuario no sea nulo ni 0 por la posibilidad de que de alguna forma este funcionando mal
            if (usuario.getProductoyservicioCantidad() == null || usuario.getProductoyservicioCantidad() == 0){
                throw new RuntimeException("El contador de producto/servicio es nulo o 0, pero este usuario si tiene productos/servicios");
            }

            //eliminamos productoServicio
            productoServicioRepository.deleteById(id);
            //disminuimos contador en usuario
            usuario.setProductoyservicioCantidad(usuario.getProductoyservicioCantidad() -1);
            //guardamos usuario
            usuarioRepository.save(usuario);
            return "producto/servicio eliminado de forma exitosa";

    }

    public List<ProductoServicioCompleteResponse> getProveedoresByEstados() {
        List<EstadoProductoServicio> estados = List.of(EstadoProductoServicio.REVISION_INICIAL, EstadoProductoServicio.CAMBIOS_REALIZADOS);
        List<ProductoServicio> productoServicios = productoServicioRepository.findByEstadoInAndIsDeletedFalse(estados);
        return productoServicios.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    public String updateProductoServicioEstado(Long proveedorId, ProductoServicioAdminRevision revision) {

        ProductoServicio productoServicio = productoServicioRepository.findById(proveedorId)
                .orElseThrow(() -> new ProductoYServicioNotFoundException("Proveedor no encontrado: " + proveedorId));


        productoServicio.setEstado(revision.getEstado());
        productoServicio.setFeedback(revision.getFeedback());
        productoServicioRepository.save(productoServicio);

        return "Producto actualizado correctamente";
    }

    //_____________________________________________________

    // Perfil usuario (productos activos, trae todos los productos independientemente del estado)

    public List<ProductoServicioCompleteResponse> misProductosYServicios(){
        // usuario actualmente autenticado
        Usuario authUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ProductoServicio> mios = productoServicioRepository.findByUsuarioAndIsDeletedFalse(authUser);
        return mios.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    // Admin

    public ProductoServicioCompleteResponse obtenerPorIdCompleto(Long id){
        return productoServicioToProductoServicioCompleteResponse(productoServicioRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ProductoYServicioNotFoundException("producto o servicio no encontrado")
        ));
    }

    public List<ProductoServicioCompleteResponse> adminObtenerTodos(){
        List<ProductoServicio> productoServicios = productoServicioRepository.findByIsDeletedFalse();
        return productoServicios.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    //Estados (solo admin)

    // Revision
    public List<ProductoServicioCompleteResponse> estadoEnRevision(){
       List<ProductoServicio> enRevision = productoServicioRepository.findByEstadoAndIsDeletedFalse(EstadoProductoServicio.REVISION_INICIAL);
       return enRevision.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    // Aceptados

    public List<ProductoServicioCompleteResponse> obtenerProductoServicioAceptados(){
        List<ProductoServicio> productoServiciosFiltrados = productoServicioRepository.findByEstadoAndIsDeletedFalse(EstadoProductoServicio.ACEPTADO);
        return productoServiciosFiltrados.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    // Denegados
    public List<ProductoServicioCompleteResponse> estadoDenegado(){
        List<ProductoServicio> denegados = productoServicioRepository.findByEstadoAndIsDeletedFalse(EstadoProductoServicio.DENEGADO);

        return denegados.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    // Requieren Cambios

    public List<ProductoServicioCompleteResponse> estadoRequiereCambios(){
        List<ProductoServicio> requiereCambios = productoServicioRepository.findByEstadoAndIsDeletedFalse(EstadoProductoServicio.REQUIERE_CAMBIOS);
        return requiereCambios.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    public List<ProductoServicioCompleteResponse> estadoCambiosRealizados(){
        List<ProductoServicio> cambiosRealizados = productoServicioRepository.findByEstadoAndIsDeletedFalse(EstadoProductoServicio.CAMBIOS_REALIZADOS);
        return cambiosRealizados.stream().map(this::productoServicioToProductoServicioCompleteResponse).toList();
    }

    public ProductoServicioDashboardDTO dataDashboard(){

        List<Object[]> resultadosCategoria = productoServicioRepository.contarProductosPorCategoria();
        List<Object[]> resultadosEstado = productoServicioRepository.contarProductosPorEstado();

        if (resultadosCategoria.size() < 11 || resultadosEstado.size() < 4){
            throw  new IllegalStateException("Los resultados de la consulta no contienen todos los datos necesarios");
        }

        // logica para devolver obtener los 5 estados en el caso que no se retornen los 5 en resultadoEstado
        List<EstadoProductoServicio> todosLosEstados = Arrays.asList(EstadoProductoServicio.values());

        Map<EstadoProductoServicio, Long> conteoPorEstado = new HashMap<>();

        for (EstadoProductoServicio estado : todosLosEstados){
            conteoPorEstado.put(estado, 0L);
        }

        for (Object[] resultado : resultadosEstado){

            EstadoProductoServicio estado = (EstadoProductoServicio) resultado[0];
            Long conteo = (Long) resultado[1];
            conteoPorEstado.put(estado, conteo);
        }


        final long estadoNuevos = conteoPorEstado.getOrDefault(EstadoProductoServicio.REVISION_INICIAL, 0L);
        final long estadoAprobados = conteoPorEstado.getOrDefault(EstadoProductoServicio.ACEPTADO, 0L);
        final long estadoDenegados = conteoPorEstado.getOrDefault(EstadoProductoServicio.DENEGADO, 0L);
        // CAMBIOS_REALIZADOS + REQUIERE_CAMBIOS
        final long estadoEnRevision = conteoPorEstado.getOrDefault(EstadoProductoServicio.REQUIERE_CAMBIOS, 0L) + conteoPorEstado.getOrDefault(EstadoProductoServicio.CAMBIOS_REALIZADOS, 0L);

        // Constantes que representan los índices de estado y categoría en los resultados de la base de datos(mismo indice que en la DB -1).
        // Los índices corresponden a la posicion de los valores en la lista devuelta por las consultas a la base de datos, por lo que comienzan desde 0 y no 1.

        final int CATEGORIA_BIENESTAR = 0;
        final int CATEGORIA_CAPACITACIONES = 1;
        final int CATEGORIA_CONSTRUCCION = 2;
        final int CATEGORIA_CULTIVOS = 3;
        final int CATEGORIA_GASTRONOMIA = 4;
        final int CATEGORIA_INDUMENTARIA = 5;
        final int CATEGORIA_MERCHANDISING = 6;
        final int CATEGORIA_MUEBLES_DECO = 7;
        final int CATEGORIA_RECICLAJE = 8;
        final int CATEGORIA_TECNOLOGIA = 9;
        final int CATEGORIA_TRANSPORTE = 10;

        return ProductoServicioDashboardDTO.builder()
                //Estados: [0] -> nombreEstado [1] -> cantidad
                .nuevosPerfilesCreados((long) estadoNuevos)
                .aprobados((long) estadoAprobados)
                .denegados((long) estadoDenegados)
                .enRevision((long) estadoEnRevision)

                //Categoria: [0] -> nombreCategoria | [1] -> cantidad
                .categoriaBienestar((long) resultadosCategoria.get(CATEGORIA_BIENESTAR)[1])
                .categoriaCapacitaciones((long) resultadosCategoria.get(CATEGORIA_CAPACITACIONES)[1])
                .categoriaConstruccion((long) resultadosCategoria.get(CATEGORIA_CONSTRUCCION)[1])
                .categoriaCultivos((long) resultadosCategoria.get(CATEGORIA_CULTIVOS)[1])
                .categoriaGastronomia(((long) resultadosCategoria.get(CATEGORIA_GASTRONOMIA)[1]))
                .categoriaIndumentaria((long) resultadosCategoria.get(CATEGORIA_INDUMENTARIA)[1])
                .categoriaMerchandising((long) resultadosCategoria.get(CATEGORIA_MERCHANDISING)[1])
                .categoriaMueblesDeco((long) resultadosCategoria.get(CATEGORIA_MUEBLES_DECO)[1])
                .categoriaReciclaje((long) resultadosCategoria.get(CATEGORIA_RECICLAJE)[1])
                .categoriaTecnologia((long) resultadosCategoria.get(CATEGORIA_TECNOLOGIA)[1])
                .categoriaTransporte((long) resultadosCategoria.get(CATEGORIA_TRANSPORTE)[1])
                .build();
    }

    // Convertidores

    // Retorna producto/servicio con informacion que solo debe ver el usuario(dueño del producto/servicio) y el admin, y no otros usuarios o guest
    // informacion adicional (en comparacion con productoServicioResponse): feedback y estado.
    private ProductoServicioCompleteResponse productoServicioToProductoServicioCompleteResponse(ProductoServicio productoYServicio) {
        ProductoServicioCompleteResponse proveedorDTO = new ProductoServicioCompleteResponse();
        proveedorDTO.setId(productoYServicio.getId());
        proveedorDTO.setNombre(productoYServicio.getNombre());
        proveedorDTO.setDescripcionLarga(productoYServicio.getDescripcionLarga());
        proveedorDTO.setDescripcionCorta(productoYServicio.getDescripcionCorta());
        proveedorDTO.setTelefono(productoYServicio.getTelefono());
        proveedorDTO.setEmail(productoYServicio.getEmail());
        proveedorDTO.setFacebook(productoYServicio.getFacebook());
        proveedorDTO.setInstagram(productoYServicio.getInstagram());
        proveedorDTO.setPais(productoYServicio.getPais().getNombre());
        proveedorDTO.setProvincia(productoYServicio.getProvincia().getNombre());
        proveedorDTO.setCiudad(productoYServicio.getCiudad());
        proveedorDTO.setEstado(productoYServicio.getEstado());
        proveedorDTO.setCategoria(productoYServicio.getCategoria().getNombre());
        proveedorDTO.setFeedback(productoYServicio.getFeedback());

        List<ImagenResponse> imagenes = productoYServicio.getImagenes().stream().map(imagen ->
            new ImagenResponse(imagen.getUrl(), imagen.getOrden())
        ).toList();
        proveedorDTO.setImagenes(imagenes);
        return proveedorDTO;
    }

    //No devuelve feedback y estado
    private ProductoServicioResponse productoServicioToProductoServicioResponse(ProductoServicio productoYServicio) {
        ProductoServicioResponse proveedorDTO = new ProductoServicioResponse();
        proveedorDTO.setId(productoYServicio.getId());
        proveedorDTO.setNombre(productoYServicio.getNombre());
        proveedorDTO.setDescripcionLarga(productoYServicio.getDescripcionLarga());
        proveedorDTO.setDescripcionCorta(productoYServicio.getDescripcionCorta());
        proveedorDTO.setTelefono(productoYServicio.getTelefono());
        proveedorDTO.setEmail(productoYServicio.getEmail());
        proveedorDTO.setFacebook(productoYServicio.getFacebook());
        proveedorDTO.setInstagram(productoYServicio.getInstagram());
        proveedorDTO.setPais(productoYServicio.getPais().getNombre());
        proveedorDTO.setProvincia(productoYServicio.getProvincia().getNombre());
        proveedorDTO.setCiudad(productoYServicio.getCiudad());
        proveedorDTO.setCategoria(productoYServicio.getCategoria().getNombre());
        List<ImagenResponse> imagenes = productoYServicio.getImagenes().stream().map(imagen ->
                new ImagenResponse(imagen.getUrl(), imagen.getOrden())
        ).toList();
        proveedorDTO.setImagenes(imagenes);
        return proveedorDTO;
    }

    //Para crear productos/servicios desde un productoServicioRequest
    private ProductoServicio productoServicioRequestToProductoServicio(ProductoServicioRequest productoServicioRequest){
        //obtenemos pais y provincia, y si no existe lanzamos una excepcion(manejada en la capa service)
        Pais pais = paisService.getByName(productoServicioRequest.getPais());
        // Si la provincia no corresponde al pais lanza una excepcion y nos salimos del metodo(la excepcion se lanza al llamar al service)
        // es decir si el proveedorRequest envia por ejemplo: pais=chile y provincia=buenos aires se lanza una excepcion
        Provincia provincia = provinciaService.getProvinciaByNombreYPais(productoServicioRequest.getProvincia(), pais);

        //llegado a este punto sabemos que la provincia si existe en la base de datos, y que el pais existe y tiene relacion (es parte) con la provincia

        //Creamos proveedor y le seteamos los atributos del proveedorRequest
        ProductoServicio nuevoProductoServicio = new ProductoServicio();
        //si la categoria no existe en la DB lanza un error
        Categoria categoria = categoriaService.getCategoriaByNombre(productoServicioRequest.getCategoria());

        // Seteamos otros atributos que no estan incluidos en el proveedorRequest o que son string y los reemplazamos para mantener la integridad de los datos
        nuevoProductoServicio.setCiudad(productoServicioRequest.getCiudad());
        nuevoProductoServicio.setDescripcionCorta(productoServicioRequest.getDescripcionCorta());
        nuevoProductoServicio.setDescripcionLarga(productoServicioRequest.getDescripcionLarga());
        nuevoProductoServicio.setEmail(productoServicioRequest.getEmail());
        nuevoProductoServicio.setFacebook(productoServicioRequest.getFacebook());
        nuevoProductoServicio.setInstagram(productoServicioRequest.getInstagram());
        nuevoProductoServicio.setNombre(productoServicioRequest.getNombre());
        nuevoProductoServicio.setTelefono(productoServicioRequest.getTelefono());
        nuevoProductoServicio.setDeleted(false);
        nuevoProductoServicio.setPais(pais);
        nuevoProductoServicio.setProvincia(provincia);
        nuevoProductoServicio.setCategoria(categoria);
        return nuevoProductoServicio;
    }



}
