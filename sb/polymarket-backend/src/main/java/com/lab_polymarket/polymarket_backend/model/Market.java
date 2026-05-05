package com.lab_polymarket.polymarket_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Market {

    @Id
    private String id;
    private String question;
    private String conditionId;
    private String category;
    private String liquidity;
    private LocalDate endDate;
    private String outcomes;
    private String outcomePrices;
    private String volume;
    private Boolean active;

    public Market() {
    }

    public Market(String id, String question, String conditionId, String category, String liquidity, LocalDate endDate, String outcomes, String outcomePrices, String volume, Boolean active) {
        this.id = id;
        this.question = question;
        this.conditionId = conditionId;
        this.category = category;
        this.liquidity = liquidity;
        this.endDate = endDate;
        this.outcomes = outcomes;
        this.outcomePrices = outcomePrices;
        this.volume = volume;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
    }

    public String getOutcomePrices() {
        return outcomePrices;
    }

    public void setOutcomePrices(String outcomePrices) {
        this.outcomePrices = outcomePrices;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Market{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", conditionId='" + conditionId + '\'' +
                ", category='" + category + '\'' +
                ", liquidity='" + liquidity + '\'' +
                ", endDate=" + endDate +
                ", outcomes='" + outcomes + '\'' +
                ", outcomePrices='" + outcomePrices + '\'' +
                ", volume='" + volume + '\'' +
                ", active=" + active +
                '}';
    }
}
