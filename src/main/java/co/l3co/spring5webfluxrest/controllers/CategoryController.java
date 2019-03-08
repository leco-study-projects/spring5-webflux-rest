package co.l3co.spring5webfluxrest.controllers;

import co.l3co.spring5webfluxrest.domain.Category;
import co.l3co.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    Flux<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> createCategory(@RequestBody Publisher<Category> category) {
        return categoryRepository.saveAll(category).then();
    }

    @PutMapping("/{id}")
    ResponseEntity<Mono<Category>> updateCategory(@PathVariable String id, @RequestBody Category category) {

        if (this.categoryRepository.findById(id) == null ||
                this.categoryRepository.findById(id).block() == null) {
            return new ResponseEntity(this.categoryRepository.save(category), HttpStatus.CREATED);
        }
        category.setId(id);
        return ResponseEntity.ok(this.categoryRepository.save(category));
    }
}
