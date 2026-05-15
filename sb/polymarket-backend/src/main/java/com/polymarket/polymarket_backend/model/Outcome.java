package com.polymarket.polymarket_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Outcome {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    public Outcome() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Outcome{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
