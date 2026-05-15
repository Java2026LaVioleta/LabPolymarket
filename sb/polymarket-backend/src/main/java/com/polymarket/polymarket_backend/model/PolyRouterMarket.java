package com.polymarket.polymarket_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polymarket.polymarket_backend.enums.MarketType;
import com.polymarket.polymarket_backend.enums.Status;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolyRouterMarket {

    @JsonProperty("id")
    private String id;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("platform_id")
    private String platformId;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("series_id")
    private String seriesId;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("subcategory")
    private String subcategory;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("market_type")
    private MarketType marketType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("trading_start")
    private String tradingStart;

    @JsonProperty("trading_end")
    private String tradingEnd;

    @JsonProperty("resolution_date")
    private String resolutionDate;

    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("current_prices")
    private Map<String, PriceDetail> currentPrices;

    @JsonProperty("volume_24h")
    private Double volume24h;

    @JsonProperty("volume_total")
    private Double volumeTotal;

    @JsonProperty("liquidity_score")
    private Double liquidityScore;

    @JsonProperty("unique_traders")
    private Integer uniqueTraders;

    @JsonProperty("source_url")
    private String sourceUrl;

    public PolyRouterMarket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTradingStart() {
        return tradingStart;
    }

    public void setTradingStart(String tradingStart) {
        this.tradingStart = tradingStart;
    }

    public String getTradingEnd() {
        return tradingEnd;
    }

    public void setTradingEnd(String tradingEnd) {
        this.tradingEnd = tradingEnd;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String, PriceDetail> getCurrentPrices() {
        return currentPrices;
    }

    public void setCurrentPrices(Map<String, PriceDetail> currentPrices) {
        this.currentPrices = currentPrices;
    }

    public Double getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(Double volume24h) {
        this.volume24h = volume24h;
    }

    public Double getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(Double volumeTotal) {
        this.volumeTotal = volumeTotal;
    }

    public Double getLiquidityScore() {
        return liquidityScore;
    }

    public void setLiquidityScore(Double liquidityScore) {
        this.liquidityScore = liquidityScore;
    }

    public Integer getUniqueTraders() {
        return uniqueTraders;
    }

    public void setUniqueTraders(Integer uniqueTraders) {
        this.uniqueTraders = uniqueTraders;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        return "PolyRouterMarket{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", marketType=" + marketType +
                '}';
    }
}
