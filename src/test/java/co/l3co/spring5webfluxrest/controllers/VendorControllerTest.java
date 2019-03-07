package co.l3co.spring5webfluxrest.controllers;

import co.l3co.spring5webfluxrest.domain.Vendor;
import co.l3co.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;


public class VendorControllerTest {

    private static final String FIRST_NAME = "l3co";
    private static final String LAST_NAME = "co";
    public static final String API_V_1_VENDORS = "/api/v1/vendors";
    private VendorRepository repository;
    private WebTestClient webTestClient;

    @Before
    public void setUp() {
        this.repository = Mockito.mock(VendorRepository.class);

        VendorController vendorController = new VendorController(repository);
        this.webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getAll() {

        BDDMockito.given(repository.findAll())
                .willReturn(Flux.just(Vendor
                        .builder()
                        .firstName("l3co")
                        .lastName("co")
                        .build()));

        this.webTestClient.get()
                .uri(API_V_1_VENDORS)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }

    @Test
    public void getById() {
        BDDMockito.given(repository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Vendor
                        .builder()
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .build()));

        this.webTestClient.get()
                .uri(API_V_1_VENDORS + "/1231")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class)
                .value(vendor -> FIRST_NAME.equals(vendor.getFirstName()));
    }

    @Test
    public void createVendor() {
        BDDMockito.given(repository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor
                        .builder()
                        .firstName("l3co")
                        .lastName("co")
                        .build()));

        Mono<Vendor> createVendor = Mono.just(Vendor
                .builder()
                .firstName("l3co")
                .lastName("co")
                .build());

        this.webTestClient
                .post()
                .uri(API_V_1_VENDORS)
                .body(createVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }
}