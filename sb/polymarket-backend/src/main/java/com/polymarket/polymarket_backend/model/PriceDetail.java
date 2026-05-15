package com.polymarket.polymarket_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDetail {

    @JsonProperty("price")
    private double price;

    @JsonProperty("bid")
    private Double bid;

    @JsonProperty("ask")
    private Double ask;

    public PriceDetail() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    @Override
    public String toString() {
        return "PriceDetail{" +
                "price=" + price +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
