package com.polymarket.polymarket_backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("open") OPEN,
    @JsonProperty("closed") CLOSED,
    @JsonProperty("paused") PAUSED,
    @JsonProperty("resolved") RESOLVED,
    @JsonProperty("cancelled") CANCELLED
}
