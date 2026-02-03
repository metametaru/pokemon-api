package com.example.pokemon_api.pokemon.model;

import java.util.List;

public record Pokemon(
        int id,
        String name,
        int height,
        int weight,
        List<TypeSlot> types,
        List<AbilitySlot> abilities,
        Sprites sprites
) {}
