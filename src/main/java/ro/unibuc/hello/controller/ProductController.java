package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.ProductDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.ProductService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    MeterRegistry metricsRegistry;

    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/addProduct")
    @Timed(value = "hello.addProduct.time", description = "Time taken to add a new product")
    @Counted(value = "hello.addProduct.count", description = "Times the endpoint /addProduct was called")
    public void addProduct(@RequestBody ProductDTO productDTO) throws EntityNotFoundException {
        productService.addProduct(productDTO);
    }

    @PutMapping("/editProduct")
    public void editProduct(@RequestBody ProductDTO productDTO) throws EntityNotFoundException {
        productService.editProduct(productDTO);
    }

    @GetMapping("/getProducts")
    @Timed(value = "hello.getProducts.time", description = "Time taken to return all existing products")
    @Counted(value = "hello.getProducts.count", description = "Times the list of all existing products was returned")
    public List<ProductDTO> getAllProducts() {
        metricsRegistry.counter("my_non_aop_getProducts_metric", "endpoint", "getProducts").increment(counter.incrementAndGet());
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable String productId) throws EntityNotFoundException {
        return productService.getProductById(productId);
    }

    @GetMapping("/filterByNameContains")
    public List<ProductDTO> getProductsByNameContains(@RequestParam String name) {
        return productService.getProductsByNameContains(name);
    }

    @GetMapping("/filterByCategory")
    public List<ProductDTO> getProductsByCategory(@RequestParam String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }

    @GetMapping("/filterByPriceBetween")
    public List<ProductDTO> getProductsByPriceBetween(@RequestParam Float lowerBoundPrice, @RequestParam Float upperBoundPrice) {
        return productService.getProductsByPriceBetween(lowerBoundPrice, upperBoundPrice);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProductById(@PathVariable String id) {
        productService.deleteProductById(id);
    }
}
