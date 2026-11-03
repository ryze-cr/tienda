package com.tienda.repository;

import com.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();
    
    //Consulta derivada que recupera los producto de un rango de precio y los ordena por precio ascendentemente
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioInf, BigDecimal precioSup);
    
    
    //Consulta JPQL que recupera los producto de un rango de precio y los ordena por precio ascendentemente
    @Query(value = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(BigDecimal precioInf, BigDecimal precioSup);
    
    
    //Consulta SQL que recupera los producto de un rango de precio y los ordena por precio ascendentemente
    @Query(nativeQuery = true,
            value = "SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(BigDecimal precioInf, BigDecimal precioSup);

    // Practica 2: combina categoria, precio y existencias usando las entidades
    // JOIN FETCH obtiene la categoria junto con cada producto en una sola consulta
    @Query("""
            SELECT p FROM Producto p JOIN FETCH p.categoria c
            WHERE p.activo = true AND c.activo = true
              AND (:idCategoria IS NULL OR c.idCategoria = :idCategoria)
              AND p.precio BETWEEN :precioMinimo AND :precioMaximo
              AND p.existencias > 0
            ORDER BY p.precio ASC, p.descripcion ASC, p.idProducto ASC
            """)
    public List<Producto> consultaAmpliada(
            @Param("idCategoria") Integer idCategoria,
            @Param("precioMinimo") BigDecimal precioMinimo,
            @Param("precioMaximo") BigDecimal precioMaximo);

}
