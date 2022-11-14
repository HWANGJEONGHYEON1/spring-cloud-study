package com.example.catalogservice.controller;

import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service/")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("{productId}")
    public ResponseEntity<ResponseCatalog> catalog(@PathVariable String productId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(catalogService.catalog(productId));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> users() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(catalogService.catalogs());
    }


    @GetMapping("health_check")
    public String status(HttpServletRequest request) {

        return String.format("It's working in User service on port %s", request.getServerPort());
    }
}
