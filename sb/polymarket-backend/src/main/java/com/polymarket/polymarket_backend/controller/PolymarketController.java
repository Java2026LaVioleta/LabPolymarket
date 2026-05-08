package com.polymarket.polymarket_backend.controller;

import com.polymarket.polymarket_backend.model.Market;
import com.polymarket.polymarket_backend.service.PolymarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/markets")
public class PolymarketController {

    private final PolymarketService polymarketService;

    public PolymarketController(PolymarketService polymarketService) {
        this.polymarketService = polymarketService;
    }

    @GetMapping
    public Flux<Market> getAllMarkets() {
        return polymarketService.getAllMarkets();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Market>> getMarketById(@PathVariable String id) {
        return polymarketService.getMarketById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/category")
    public Flux<Market> getMarketsByCategory(@RequestParam String category) {
        return polymarketService.getMarketsByCategory(category);
    }

    @GetMapping("/active")
    public Flux<Market> getActiveMarkets() {
        return polymarketService.getActiveMarkets();
    }
}
