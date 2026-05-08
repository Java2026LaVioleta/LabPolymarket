package com.polymarket.polymarket_backend.model;

public class Market {

    private String id;
    private String question;
    private String slug;
    private Double volume;
    private Double liquidity;
    private String endDate;
    private Boolean active;
    private String category;
    private String outcomes;
    private String outcomePrices;

    public Market() {}

    public Market(String id, String question, String slug, Double volume, Double liquidity, String endDate, Boolean active, String category, String outcomes, String outcomePrices) {
        this.id = id;
        this.question = question;
        this.slug = slug;
        this.volume = volume;
        this.liquidity = liquidity;
        this.endDate = endDate;
        this.active = active;
        this.category = category;
        this.outcomes = outcomes;
        this.outcomePrices = outcomePrices;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(Double liquidity) {
        this.liquidity = liquidity;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    @Override
    public String toString() {
        return "Market{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", slug='" + slug + '\'' +
                ", volume=" + volume +
                ", liquidity=" + liquidity +
                ", endDate='" + endDate + '\'' +
                ", active=" + active +
                ", category='" + category + '\'' +
                ", outcomes=" + outcomes +
                ", outcomePrices=" + outcomePrices +
                '}';
    }
}
