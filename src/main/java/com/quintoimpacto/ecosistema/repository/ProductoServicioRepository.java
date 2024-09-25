package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.Categoria;
import com.quintoimpacto.ecosistema.model.ProductoServicio;
import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoServicioRepository extends JpaRepository<ProductoServicio, Long>{

    @Query("SELECT p FROM ProductoServicio p WHERE REPLACE(p.nombre, ' ', '') LIKE %:nombre% AND p.isDeleted = false AND p.estado = ACEPTADO")
    List<ProductoServicio> findByNombre(@Param("nombre") String proveedorNombre);
    List<ProductoServicio> findByEstadoAndIsDeletedFalse(EstadoProductoServicio estado);
    List<ProductoServicio> findByIsDeletedFalseAndEstado(EstadoProductoServicio estado);

    Optional<ProductoServicio> findByIdAndIsDeletedFalse(Long id);
    Optional<ProductoServicio> findByIdAndIsDeletedFalseAndEstado(Long id, EstadoProductoServicio estado);
    List<ProductoServicio> findByCategoriaAndEstadoAndIsDeletedFalse(Categoria categoria, EstadoProductoServicio estado);
    List<ProductoServicio> findByEstadoInAndIsDeletedFalse(List<EstadoProductoServicio> estados);

    List<ProductoServicio> findByUsuarioAndIsDeletedFalse(Usuario usuario);

    //admin


    //@Query("SELECT c.nombre, COUNT(p) FROM ProductoServicio p JOIN p.categoria c GROUP BY c.nombre")
    @Query("SELECT c.nombre, COUNT(p) FROM Categoria c LEFT JOIN c.productoServicios p GROUP BY c.nombre")
    List<Object[]> contarProductosPorCategoria();

    @Query("SELECT p.estado, COUNT(p) FROM ProductoServicio p GROUP BY p.estado")
    List<Object[]> contarProductosPorEstado();

    List<ProductoServicio> findByIsDeletedFalse();

}