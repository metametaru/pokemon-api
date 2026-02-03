package com.example.pokemon_api.pokemon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Sprites(
        @JsonProperty("front_default")
        String frontDefault
) {}
