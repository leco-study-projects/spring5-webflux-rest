package co.l3co.spring5webfluxrest.controllers;

import co.l3co.spring5webfluxrest.domain.Category;
import co.l3co.spring5webfluxrest.domain.Vendor;
import co.l3co.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> getAll() {
        return this.vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return this.vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorPublisher) {
        return this.vendorRepository.saveAll(vendorPublisher).then();
    }


    @PutMapping("/{id}")
    ResponseEntity<Mono<Vendor>> updateCategory(@PathVariable String id, @RequestBody Vendor vendor) {

        if (this.vendorRepository.findById(id) == null ||
                this.vendorRepository.findById(id).block() == null) {
            return new ResponseEntity(this.vendorRepository.save(vendor), HttpStatus.CREATED);
        }
        vendor.setId(id);
        return ResponseEntity.ok(this.vendorRepository.save(vendor));
    }
}
