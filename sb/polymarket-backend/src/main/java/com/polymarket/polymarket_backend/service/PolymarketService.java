package com.polymarket.polymarket_backend.service;

import com.polymarket.polymarket_backend.model.Market;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PolymarketService {

    private final WebClient webClient;

    public PolymarketService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://gamma-api.polymarket.com")
                .build();
    }

    public Flux<Market> getAllMarkets() {
        return webClient.get()
                .uri("/markets")
                .retrieve()
                .bodyToFlux(Market.class);
    }

    public Mono<Market> getMarketById(String id) {
        return webClient.get()
                .uri("/markets/{id}", id)
                .retrieve()
                .bodyToMono(Market.class);
    }

    public Flux<Market> getMarketsByCategory(String category) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/markets")
                        .queryParam("category", category)
                        .build())
                .retrieve()
                .bodyToFlux(Market.class);
    }

    public Flux<Market> getActiveMarkets() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/markets")
                        .queryParam("active", true)
                        .build())
                .retrieve()
                .bodyToFlux(Market.class);
    }
}
