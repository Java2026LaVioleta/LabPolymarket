package com.lab_polymarket.polymarket_backend.controller;

import com.lab_polymarket.polymarket_backend.model.Market;
import com.lab_polymarket.polymarket_backend.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping
    public ResponseEntity<List<Market>> getAllMarkets() {
        return new ResponseEntity<>(marketService.getAllMarkets(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Market> getMarketById(@PathVariable String id) {
        return marketService.getMarketById(id)
                .map(market -> new ResponseEntity<>(market, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Market> createMarket(@RequestBody Market market) {
        return new ResponseEntity<>(marketService.createMarket(market), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Market> updateMarket(@PathVariable String id, @RequestBody Market marketDetails) {
        return new ResponseEntity<>(marketService.updateMarket(id, marketDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable String id) {
        marketService.deleteMarket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Market>> getMarketsByCategory(@PathVariable String category) {
        return new ResponseEntity<>(marketService.getMarketsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Market>> getActiveMarkets() {
        return new ResponseEntity<>(marketService.getActiveMarkets(), HttpStatus.OK);
    }

    @GetMapping("/category/{category}/active/{active}")
    public ResponseEntity<List<Market>> getMarketsByCategoryAndActive(
            @PathVariable String category, @PathVariable Boolean active) {
        return new ResponseEntity<>(marketService.getMarketsByCategoryAndActive(category, active), HttpStatus.OK);
    }
}
