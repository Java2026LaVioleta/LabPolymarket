package com.polymarket.polymarket_backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MarketType {
    @JsonProperty("binary") BINARY,
    @JsonProperty("categorical") CATEGORICAL,
    @JsonProperty("scalar") SCALAR
}
