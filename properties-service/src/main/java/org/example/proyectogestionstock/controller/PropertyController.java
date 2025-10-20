package org.example.proyectogestionstock.controller;

import lombok.RequiredArgsConstructor;
import org.example.proyectogestionstock.DTO.PropertyRequest;
import org.example.proyectogestionstock.model.Property;
import org.example.proyectogestionstock.service.PropertyService;
import org.example.proyectogestionstock.service.implementation.PropertyServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "*") // permitir llamadas desde Angular
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyServiceImpl service;

    @GetMapping
    public List<Property> getAll() {
        return service.getAllProperties();
    }

    @GetMapping("/{id}")
    public Property getById(@PathVariable Long id) {
        return service.getPropertyById(id).orElse(null);
    }

    @PostMapping("/create")
    public Property create(@RequestBody PropertyRequest property) {
        return service.saveProperty(property);
    }

    @PutMapping("/{id}")
    public Property update(@PathVariable Long id, @RequestBody PropertyRequest property) {
        return service.saveProperty(property);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteProperty(id);
    }

    @GetMapping("/search")
    public List<Property> searchByCity(@RequestParam String city) {
        return service.findByCity(city);
    }
}
