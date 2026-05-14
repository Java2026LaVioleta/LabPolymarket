package com.polymarket.polymarket_backend.service;

import com.polymarket.polymarket_backend.model.MarketListResponse;
import com.polymarket.polymarket_backend.model.PolyRouterMarket;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;

@Service
public class PolyRouterMarketService {

    private final WebClient webClient;

    public PolyRouterMarketService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api-v2.polyrouter.io")
                .defaultHeader("X-API-Key", "pk_ddd423513e0d4a17c887934ec738ee248bc5d4f39f46d54a912765cf044daec6")
                .build();
    }

    public Flux<PolyRouterMarket> getAllMarkets() {
        return webClient.get()
                .uri("/markets?limit=50")
                .retrieve()
                .bodyToMono(MarketListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(
                        response.getMarkets() != null ? response.getMarkets() : Collections.emptyList()));
    }

    public Mono<PolyRouterMarket> getMarketById(String id) {
        return webClient.get()
                .uri("/markets/{id}", id)
                .retrieve()
                .bodyToMono(PolyRouterMarket.class);
    }

    public Flux<PolyRouterMarket> getMarketsByCategory(String category) {
        return webClient.get()
                .uri("/markets?category={category}&limit=50", category)
                .retrieve()
                .bodyToMono(MarketListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(
                        response.getMarkets() != null ? response.getMarkets() : Collections.emptyList()));
    }

    public Flux<PolyRouterMarket> getActiveMarkets() {
        return webClient.get()
                .uri("/markets?active=true&closed=false&limit=50")
                .retrieve()
                .bodyToMono(MarketListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(
                        response.getMarkets() != null ? response.getMarkets() : Collections.emptyList()));
    }
}