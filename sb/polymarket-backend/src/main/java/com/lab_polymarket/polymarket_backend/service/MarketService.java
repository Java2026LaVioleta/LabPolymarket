package com.lab_polymarket.polymarket_backend.service;

import com.lab_polymarket.polymarket_backend.model.Market;
import com.lab_polymarket.polymarket_backend.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }

    public Optional<Market> getMarketById(String id) {
        return marketRepository.findById(id);
    }

    public Market createMarket(Market market) {
        return marketRepository.save(market);
    }

    public Market updateMarket(String id, Market marketDetails) {
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Market not found with id: " + id));
        
        market.setQuestion(marketDetails.getQuestion());
        market.setConditionId(marketDetails.getConditionId());
        market.setCategory(marketDetails.getCategory());
        market.setLiquidity(marketDetails.getLiquidity());
        market.setEndDate(marketDetails.getEndDate());
        market.setOutcomes(marketDetails.getOutcomes());
        market.setOutcomePrices(marketDetails.getOutcomePrices());
        market.setVolume(marketDetails.getVolume());
        market.setActive(marketDetails.getActive());
        
        return marketRepository.save(market);
    }

    public void deleteMarket(String id) {
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Market not found with id: " + id));
        marketRepository.delete(market);
    }

    public List<Market> getMarketsByCategory(String category) {
        return marketRepository.findByCategory(category);
    }

    public List<Market> getActiveMarkets() {
        return marketRepository.findByActive(true);
    }

    public List<Market> getMarketsByCategoryAndActive(String category, Boolean active) {
        return marketRepository.findByCategoryAndActive(category, active);
    }
}
