package com.polymarket.polymarket_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketListResponse {

    private List<PolyRouterMarket> markets;
    private Pagination pagination;

    public List<PolyRouterMarket> getMarkets() {
        return markets;
    }

    public void setMarkets(List<PolyRouterMarket> markets) {
        this.markets = markets;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagination {
        private int total;
        private int limit;
        @JsonProperty("has_more")
        private boolean hasMore;
        @JsonProperty("next_cursor")
        private String nextCursor;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public String getNextCursor() {
            return nextCursor;
        }

        public void setNextCursor(String nextCursor) {
            this.nextCursor = nextCursor;
        }
    }
}
