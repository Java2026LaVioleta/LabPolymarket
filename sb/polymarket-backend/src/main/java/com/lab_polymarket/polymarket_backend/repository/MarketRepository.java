package com.lab_polymarket.polymarket_backend.repository;

import com.lab_polymarket.polymarket_backend.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, String> {
    List<Market> findByCategory(String category);
    List<Market> findByActive(Boolean active);
    List<Market> findByCategoryAndActive(String category, Boolean active);
}
