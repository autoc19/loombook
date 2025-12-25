package com.loombook.api.rest;

import com.loombook.application.command.CreateProductCommand;
import com.loombook.application.command.CreateProductResult;
import com.loombook.application.command.CreateProductUseCase;
import com.loombook.application.query.GetProductByIdUseCase;
import com.loombook.application.query.ProductResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 商品 REST Controller
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                             GetProductByIdUseCase getProductByIdUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        try {
            CreateProductCommand command = new CreateProductCommand(request.name(), request.priceCents());
            CreateProductResult result = createProductUseCase.execute(command);
            ProductResponse response = new ProductResponse(result.id(), result.name(), result.priceCents());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        return getProductByIdUseCase.execute(id)
                .map(result -> new ProductResponse(result.id(), result.name(), result.priceCents()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
