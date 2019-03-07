package co.l3co.spring5webfluxrest.controllers;

import co.l3co.spring5webfluxrest.domain.Category;
import co.l3co.spring5webfluxrest.domain.Vendor;
import co.l3co.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;


public class CategoryControllerTest {

    private static final String DESCRIPTION = "L3Co";
    public static final String API_V_1_CATEGORIES = "/api/v1/categories";
    private WebTestClient webTestClient;
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
        this.categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryController categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getAll() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category
                        .builder()
                        .description("test")
                        .id("1232343")
                        .build()));

        webTestClient.get().uri(API_V_1_CATEGORIES)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(1);
    }

    @Test
    public void getById() {
        BDDMockito.given(categoryRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Category
                        .builder()
                        .description(DESCRIPTION)
                        .id("1232343")
                        .build()));

        webTestClient.get().uri(API_V_1_CATEGORIES + "/1232343")
                .exchange()
                .expectBody(Category.class)
                .value(category -> category.getDescription().equals(DESCRIPTION));
    }

    @Test
    public void createCategory() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category
                        .builder()
                        .description("test")
                        .build()));

        Mono<Category> categoryMono = Mono.just(Category
                .builder()
                .description("test")
                .build());

        this.webTestClient
                .post()
                .uri(API_V_1_CATEGORIES)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }
}