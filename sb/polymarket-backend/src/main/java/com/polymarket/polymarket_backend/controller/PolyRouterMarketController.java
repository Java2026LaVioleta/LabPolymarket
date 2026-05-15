package com.polymarket.polymarket_backend.controller;

import com.polymarket.polymarket_backend.model.PolyRouterMarket;
import com.polymarket.polymarket_backend.service.PolyRouterMarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/polyrouter/markets")
public class PolyRouterMarketController {

    private final PolyRouterMarketService polyRouterMarketService;

    public PolyRouterMarketController(PolyRouterMarketService polyRouterMarketService) {
        this.polyRouterMarketService = polyRouterMarketService;
    }

    @GetMapping
    public Flux<PolyRouterMarket> getAllMarkets() {
        return polyRouterMarketService.getAllMarkets();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PolyRouterMarket>> getMarketById(@PathVariable String id) {
        return polyRouterMarketService.getMarketById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/category")
    public Flux<PolyRouterMarket> getMarketsByCategory(@RequestParam String category) {
        return polyRouterMarketService.getMarketsByCategory(category);
    }

    @GetMapping("/active")
    public Flux<PolyRouterMarket> getActiveMarkets() {
        return polyRouterMarketService.getActiveMarkets();
    }
}
