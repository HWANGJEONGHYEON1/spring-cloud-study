package com.example.catalogservice.service;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.utils.ModelMapperUtils;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public List<ResponseCatalog> catalogs() {
        List<ResponseCatalog> catalogs = new ArrayList<>();

        catalogRepository.findAll()
                .forEach(catalogEntity -> {
                    catalogs.add(ModelMapperUtils.modelMapper()
                            .map(catalogEntity, ResponseCatalog.class));
                });

        return catalogs;
    }

    public ResponseCatalog catalog(String productId) {
        return ModelMapperUtils.modelMapper()
                .map(catalogRepository.findByProductId(productId), ResponseCatalog.class);
    }

}
