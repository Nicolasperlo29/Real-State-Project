package org.example.proyectogestionstock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.proyectogestionstock.model.Property;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByCityContainingIgnoreCase(String city);
    List<Property> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Property> findByType(String type);
}
