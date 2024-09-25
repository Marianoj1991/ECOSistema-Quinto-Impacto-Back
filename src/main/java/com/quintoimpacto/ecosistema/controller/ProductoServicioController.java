package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.*;
import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import com.quintoimpacto.ecosistema.service.impl.ProductoServicioServiceImpl;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequiredArgsConstructor
public class ProductoServicioController {

    private final ProductoServicioServiceImpl productoServicioService;

    //user?
    @GetMapping("/inicio/producto-servicio/{id}")
    public ResponseEntity<ProductoServicioResponse> obtenerProductosServiciosPorIdActivoAceptado(@PathVariable Long id) throws Exception {
        ProductoServicioResponse proveedor = productoServicioService.getProductoYServicioByIdActivoAceptado(id);
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    //User
    @GetMapping("/inicio/producto-servicio/buscar")
    public ResponseEntity<List<ProductoServicioResponse>> obtenerProductoServicioPorNombreAceptadoActivo(@RequestParam String nombre) throws Exception {
        List<ProductoServicioResponse> proveedores = productoServicioService.searchByNombreAceptadoActivo(nombre);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    //User
    @GetMapping("/inicio/producto-servicio/categoria")
    public ResponseEntity<List<ProductoServicioResponse>> obtenerProveedorPorCategoriaAceptadoActivo(@RequestParam String nombre) {
        return ResponseEntity.ok(productoServicioService.getByCategoriaAceptadoActivo(nombre));
    }


    //User
    @GetMapping("/inicio/producto-servicio")
    public ResponseEntity<List<ProductoServicioResponse>> obtenerProductoServicioActivoAceptado() throws Exception {
        return new ResponseEntity<>(productoServicioService.getAllProductoServicioActivoAceptado(), HttpStatus.OK);
    }

    @GetMapping("/perfil/producto-servicio/mis-productos-y-servicios/{id}")
    public ResponseEntity<ProductoServicioCompleteResponse> obtenerMiproductoServicio(@PathVariable Long id){
        return ResponseEntity.ok(productoServicioService.getOwnProductoServicioById(id));
    }




    /*@PatchMapping("/{id}")
    public ResponseEntity<String> actualizarProveedor(@ModelAttribute ProductoServicioUpdateRequest proveedor, @PathVariable Long id, @RequestPart List<MultipartFile> imagenes) throws Exception {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(proveedorService.updateProveedor(authUser, proveedor, imagenes, id), HttpStatus.OK);
    }*/

    @PutMapping("/perfil/producto-servicio/{id}")
    public ResponseEntity<String> actualizarProductoServicio(@ModelAttribute ProductoServicioUpdateRequest productoServicioUpdateRequest, @PathVariable Long id, @RequestParam(required = false) MultipartFile imagen1,
                                                             @RequestParam(required = false) MultipartFile imagen2,
                                                             @RequestParam(required = false) MultipartFile imagen3) throws Exception {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        List<ImagenesRequestDTO> imagenes = new ArrayList<>();
        if (imagen1 != null) {
            imagenes.add(new ImagenesRequestDTO(imagen1, (byte) 1));
        }
        if (imagen2 != null) {
            imagenes.add(new ImagenesRequestDTO(imagen2, (byte) 2));
        }
        if (imagen3 != null) {
            imagenes.add(new ImagenesRequestDTO(imagen3, (byte) 3));
        }

        return new ResponseEntity<>(productoServicioService.updateProductoServicio(authUser, productoServicioUpdateRequest, imagenes, id), HttpStatus.OK);
    }
   /*public ResponseEntity<String> actualizarProductoServicio(@RequestPart ProductoServicioUpdateRequest productoServicioUpdateRequest, @RequestPart List<Multi>,@PathVariable Long id) throws Exception {
       Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
       return new ResponseEntity<>(productoServicioService.updateProductoServicio(authUser, productoServicioUpdateRequest, id), HttpStatus.OK);
        /*System.out.println(proveedor);
        System.out.println(proveedor.toString());
        return ResponseEntity.ok("ok");*/


    @DeleteMapping("/perfil/producto-servicio/{id}")
    public ResponseEntity<String> eliminarProductoServicioActivo(@PathVariable Long id) throws Exception {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        productoServicioService.bajaProveedorActivo(id, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/perfil/producto-servicio")
    public ResponseEntity<String> crearProductoServicio(@ModelAttribute @Valid ProductoServicioRequest productoServicioRequest, @RequestParam(required = false) MultipartFile imagen1,
                                                        @RequestParam(required = false) MultipartFile imagen2,
                                                        @RequestParam(required = false) MultipartFile imagen3) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        List<ImagenesRequestDTO> imagenes = new ArrayList<>();
        if (imagen1 != null && !imagen1.isEmpty()) {
            imagenes.add(new ImagenesRequestDTO(imagen1, 1));
        }

        if (imagen2 != null && !imagen2.isEmpty()) {
            imagenes.add(new ImagenesRequestDTO(imagen2, 2));
        }

        if (imagen3 != null && !imagen3.isEmpty()) {
            imagenes.add(new ImagenesRequestDTO(imagen3, 3));
        }


        return new ResponseEntity<>(productoServicioService.createProductoServicio(productoServicioRequest, imagenes, authUser), HttpStatus.CREATED);
    }

    @GetMapping("/perfil/producto-servicio/mis-productos-y-servicios")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> misProductosYServicios() {
        return ResponseEntity.ok(productoServicioService.misProductosYServicios());
    }



    // Admin

    @GetMapping("/admin/producto-servicio/obtener-todos")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> adminGetAllProductoServicio(){
        return ResponseEntity.ok(productoServicioService.adminObtenerTodos());
    }

    @GetMapping("/admin/producto-servicio/{id}")
    public ResponseEntity<ProductoServicioCompleteResponse> adminGetById(@PathVariable Long id){
        return ResponseEntity.ok(productoServicioService.obtenerPorIdCompleto(id));
    }

   /* @GetMapping("/admin/revision")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> getProveedoresByEstados() {
        return ResponseEntity.ok(productoServicioService.getProveedoresByEstados());
    }*/

    @PutMapping("/admin/producto-servicio/revision/{id}")
    public ResponseEntity<String> updateProductoServicioEstado(@PathVariable Long id, @RequestBody ProductoServicioAdminRevision revision) {
        return ResponseEntity.ok(productoServicioService.updateProductoServicioEstado(id, revision));
    }

    // Por estado
    @GetMapping("/admin/producto-servicio/estado/aceptado")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> obtenerProductoServicioAceptados(){
        return ResponseEntity.ok(productoServicioService.obtenerProductoServicioAceptados());
    }

    @GetMapping("/admin/producto-servicio/estado/denegado")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> allEstadoDenegado(){
        return ResponseEntity.ok(productoServicioService.estadoDenegado());
    }

    @GetMapping("/admin/producto-servicio/estado/requiere-cambios")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> allEstadoRequiereCambios(){
        return ResponseEntity.ok(productoServicioService.estadoRequiereCambios());
    }

    @GetMapping("/admin/producto-servicio/estado/revision-inicial")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> allEstadoRevisionInicial(){
        return ResponseEntity.ok(productoServicioService.estadoEnRevision());
    }

    @GetMapping("/admin/producto-servicio/estado/cambios-realizados")
    public ResponseEntity<List<ProductoServicioCompleteResponse>> allEstadoCambiosRealizados(){
        return ResponseEntity.ok(productoServicioService.estadoCambiosRealizados());
    }

    @GetMapping("/admin/producto-servicio/dashboard")
    public ResponseEntity<ProductoServicioDashboardDTO> dataDashboard(){
        return ResponseEntity.ok(productoServicioService.dataDashboard());
    }

}
