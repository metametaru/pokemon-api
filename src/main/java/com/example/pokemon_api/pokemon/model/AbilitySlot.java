package com.example.pokemon_api.pokemon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AbilitySlot(
        @JsonProperty("is_hidden")
        boolean isHidden,
        int slot,
        NamedApiResource ability
) {}
