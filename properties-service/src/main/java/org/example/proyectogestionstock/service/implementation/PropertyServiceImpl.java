package org.example.proyectogestionstock.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.proyectogestionstock.DTO.PropertyRequest;
import org.example.proyectogestionstock.model.Property;
import org.example.proyectogestionstock.model.PropertyImage;
import org.example.proyectogestionstock.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl {

    private final PropertyRepository repository;

    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    public Optional<Property> getPropertyById(Long id) {
        return repository.findById(id);
    }

    public Property saveProperty(PropertyRequest request) {
        Property property = new Property();

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setCity(request.getCity());
        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());
        property.setAreaCubierta(request.getAreaCubierta());
        property.setAreaTotal(request.getAreaTotal());
        property.setBathrooms(request.getBathrooms());
        property.setRooms(request.getRooms());
        property.setType(request.getType());
        property.setStatus(request.getStatus());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setUserId(request.getUserId());

        // Mapear imágenes
        if (request.getImages() != null) {
            List<PropertyImage> images = request.getImages().stream()
                    .map(url -> {
                        PropertyImage img = new PropertyImage();
                        img.setUrl(url);
                        img.setProperty(property); // setear la relación
                        return img;
                    })
                    .collect(Collectors.toList());

            property.setImages(images);
        }

        return repository.save(property);
    }

    public void deleteProperty(Long id) {
        repository.deleteById(id);
    }

    public List<Property> findByCity(String city) {
        return repository.findByCityContainingIgnoreCase(city);
    }
}
